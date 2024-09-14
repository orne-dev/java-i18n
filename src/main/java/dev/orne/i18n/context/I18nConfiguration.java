package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023-2024 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.orne.i18n.I18nConfigurationException;

/**
 * Utility class with I18N configuration constants and retrieval methods.
 * <p>
 * Configuration is loaded from application provided {@value #FILE}
 * properties file. Only one configuration file is allowed by class loader.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-11
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public final class I18nConfiguration {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(I18nConfiguration.class);

    /** The I18N configuration resource. */
    public static final String FILE = "dev.orne.i18n.config.properties";

    /**
     * The configuration properties prefix.
     */
    public static final String PREFIX = "dev.orne.i18n.";
    /**
     * The configuration property for {@code I18nContextProvider}
     * implementation to use.
     * Takes {@code DEFAULT} by default.
     */
    public static final String CONTEXT_PROVIDER = PREFIX + "context.provider";
    /**
     * The configuration property for {@code I18nContex} inheritance by
     * child threads in per-thread based context providers.
     * Takes {@code true} by default.
     */
    public static final String CONTEXT_INHERITED = PREFIX + "context.inherited";
    /**
     * The configuration property for default language.
     * Takes {@code Locale.getDefault()} by default.
     */
    public static final String DEFAULT_LANGUAGE = PREFIX + "language.default";
    /**
     * The configuration property for comma separated available languages.
     * Takes {@code Locale.getAvailableLocales()} by default.
     */
    public static final String AVAILABLE_LANGUAGES = PREFIX + "language.available";
    /**
     * The configuration property for default {@code I18nResources}
     * bundle base name.
     * Takes {@code messages} by default.
     */
    public static final String DEFAULT_RESOURCES = PREFIX + "resources";
    /**
     * The configuration properties prefix for named {@code I18nResources}
     * bundle base name.
     * <p>
     * To be concatenated with bundle name. For example:
     * {@value NAMED_RESOURCES_PREFIX}{@code .alt-messages}
     * configures resource with name {@code alt-messages}.
     */
    public static final String NAMED_RESOURCES_PREFIX = PREFIX + "resources.named.";

    /** The default configuration resource, relative to this class. */
    @API(status=Status.INTERNAL, since="0.1")
    static final String DEFAULT_CFG = "default-config.properties";
    /** The by ClassLoader I18N configuration cache. */
    @API(status=Status.INTERNAL, since="0.1")
    static final WeakHashMap<ClassLoader, Pair<Properties, Set<String>>> CACHE =
            new WeakHashMap<>();

    /**
     * Private constructor.
     */
    private I18nConfiguration() {
        // Utility class
    }

    /**
     * Returns the I18N configuration for the current class loader.
     * 
     * @return The I18N configuration properties
     * @see #get(ClassLoader)
     * @throws I18nConfigurationException If an error occurs loading the
     * configuration.
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static @NotNull Properties get() {
        return get(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Returns the I18N configuration for the specified class loader.
     * If the class loader has a {@value #FILE} resource loads the
     * configuration from the file.
     * Otherwise inherits the configuration from the parent class loader and,
     * if none has a custom configuration, applies the default configuration.
     * <p>
     * Note that having more that one {@value #FILE} in a class loader
     * throws a {@code I18nConfigurationException}.
     * 
     * @param cl The class loader to retrieve the configuration for.
     * @return The configuration properties.
     * @see #FILE
     * @throws I18nConfigurationException If an error occurs loading the
     * configuration.
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static @NotNull Properties get(
            final @NotNull ClassLoader cl) {
        Validate.notNull(cl);
        final Properties copy = new Properties();
        copy.putAll(getCache(cl).getLeft());
        return copy;
    }

    /**
     * Sets the specified configuration for the specified class loader
     * and all its children.
     * 
     * @param cl The class loader.
     * @param config The configuration to apply.
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    static synchronized void set(
            final @NotNull ClassLoader cl,
            final @NotNull Properties config) {
        Validate.notNull(cl);
        Validate.notNull(config);
        final Properties copy = new Properties();
        copy.putAll(config);
        final Set<String> clRes = CACHE.get(cl).getRight();
        CACHE.put(cl, Pair.of(copy, clRes));
        CACHE.forEach((key, cache) -> {
            boolean child = key.getParent() != null &&
                    key.getParent().equals(cl);
            if (child && cache.getRight().equals(clRes)) {
                set(key, config);
            }
        });
    }

    /**
     * Resets the I18N configuration cache.
     */
    @API(status=Status.INTERNAL, since="0.1")
    static synchronized void reset() {
        CACHE.clear();
    }

    /**
     * Returns or computes the cached tuple of I18N configuration and applied
     * file URLs for the specified class loader.
     * 
     * @param cl The class loader.
     * @return The tuple of I18N configuration and applied file URLs.
     * @throws I18nConfigurationException If an error occurs loading the
     * configuration.
     */
    @API(status=Status.INTERNAL, since="0.1")
    static synchronized @NotNull Pair<Properties, Set<String>> getCache(
            final @NotNull ClassLoader cl) {
        return CACHE.computeIfAbsent(cl, I18nConfiguration::loadConfiguration);
    }

    /**
     * Computes the cached tuple of I18N configuration and applied file URLs
     * for the specified class loader.
     * <p>
     * If the class loader has a configuration file not inherited from the
     * parent class loader loads the configuration from the file and returns
     * a set with parent class loader's file URLs plus the class loader file
     * URL to detect new configuration files in child class loaders.
     * <p>
     * If the class loader has not a configuration file inherits the
     * configuration and the set of detected file URLs from the parent class
     * loader.
     * <p>
     * If the class loader has multiple configuration files throws a
     * {@code I18nConfigurationException}.
     * <p>
     * Finally if the class loader has no parent (bootstrap class loader)
     * loads the default configuration and returns an empty set of detected
     * configuration files.
     * 
     * @param cl The class loader.
     * @return The tuple of I18N configuration and applied file URLs.
     * @throws I18nConfigurationException If an error occurs loading the
     * configuration.
     */
    @API(status=Status.INTERNAL, since="0.1")
    static @NotNull Pair<Properties, Set<String>> loadConfiguration(
            final @NotNull ClassLoader cl) {
        final Properties config = new Properties();
        final Set<String> resources = new HashSet<>();
        if (cl.getParent() == null) {
            LOG.debug("Loading default I18N configuration for ClassLoader {}", cl);
            try (final InputStream is = I18nConfiguration.class.getResourceAsStream(DEFAULT_CFG)) {
                config.load(is);
            } catch (final IOException e) {
                throw new I18nConfigurationException(
                        "Error loading I18N default configuration",
                        e);
            }
        } else {
            final Pair<Properties, Set<String>> parent = getCache(cl.getParent());
            resources.addAll(parent.getRight());
            final URL file = getClassLoaderConfigFile(cl, parent.getRight());
            if (file == null) {
                LOG.debug("Inheriting parent I18N configuration for ClassLoader {}", cl);
                config.putAll(parent.getLeft());
            } else {
                LOG.debug("Loading I18N configuration for ClassLoader {}", cl);
                try (final InputStream is = file.openStream()) {
                    config.load(is);
                } catch (final IOException e) {
                    throw new I18nConfigurationException(
                            "Error loading custom I18N configuration",
                            e);
                }
                resources.add(file.toString());
            }
        }
        return Pair.of(config, resources);
    }

    /**
     * Finds a new I18N configuration file to apply to the specified class
     * loader.
     * If the class loader has multiple configuration files throws a
     * {@code I18nConfigurationException}.
     * 
     * @param cl The class loader.
     * @param prev The set of configuration files detected in parent class
     * loaders.
     * @return The configuration file URL, if any.
     * @throws I18nConfigurationException If an error occurs loading the
     * configuration or multiple files are detected.
     */
    @API(status=Status.INTERNAL, since="0.1")
    static URL getClassLoaderConfigFile(
            final @NotNull ClassLoader cl,
            final @NotNull Set<String> prev) {
        final List<URL> result = new ArrayList<>();
        try {
            for (final URL resource : Collections.list(cl.getResources(I18nConfiguration.FILE))) {
                if (!prev.contains(resource.toString())) {
                    result.add(resource);
                }
            }
        } catch (final IOException e) {
            throw new I18nConfigurationException("Error loading I18N configuration", e);
        }
        if (result.isEmpty()) {
            return null;
        } else if (result.size() == 1) {
            return result.get(0);
        } else {
            throw new I18nConfigurationException(
                    "Error loading I18N configuration: ClassLoader" + cl +
                    " contain multiple I18N configuration resources: " + result);
        }
    }
}
