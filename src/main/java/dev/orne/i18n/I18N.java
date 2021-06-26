package dev.orne.i18n;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 Orne Developments
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
import java.lang.reflect.Constructor;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotNull;

/**
 * Main entry point to I18N framework. Provides methods for configuration and
 * manipulation of I18N contexts.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @since 0.1
 */
public final class I18N {

    /** The configuration file path. */
    public static final String CONFIG_FILE = "dev.orne.i18n.config.properties";
    /**
     * The configuration file property for {@code I18nContextProviderStrategy}
     * implementation to use.
     */
    public static final String STRATEGY_PROP = "strategy";

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(I18N.class);
    /** The I18N context provider selection strategy. */
    private static I18nContextProviderStrategy contextProviderStrategy;

    /**
     * Private constructor.
     */
    private I18N() {
        // Utility class
    }

    /**
     * Returns the I18N context provider selection strategy.
     * 
     * @return The I18N context provider selection strategy
     */
    public static @NotNull I18nContextProviderStrategy getContextProviderStrategy() {
        synchronized (I18N.class) {
            if (I18N.contextProviderStrategy == null) {
                I18N.contextProviderStrategy = createStrategy();
            }
            return I18N.contextProviderStrategy;
        }
    }

    /**
     * Sets the I18N context provider selection strategy.
     * 
     * @param strategy The I18N context provider selection strategy
     */
    public static void setContextProviderStrategy(
            final @NotNull I18nContextProviderStrategy strategy) {
        synchronized (I18N.class) {
            reconfigure();
            I18N.contextProviderStrategy = Validate.notNull(strategy);
        }
    }

    /**
     * Reconfigures the full I18N system, reseting the I18N context provider
     * selection strategy.
     * <p>
     * The old strategy and context providers will be invalidated, thus
     * invalidating all previously created I18N contexts.
     * <p>
     * If no custom new I18N context provider selection strategy is set after
     * calling this method any method call will fire the I18N system
     * configuration based on configuration file (if present).
     */
    public static void reconfigure() {
        synchronized (I18N.class) {
            if (I18N.contextProviderStrategy != null) {
                I18N.contextProviderStrategy.invalidate();
                I18N.contextProviderStrategy = null;
            }
        }
    }

    /**
     * Creates a I18N context provider selection strategy based on
     * configuration file in current {@code Thread} context class loader.
     * If no configuration file is found a default I18N context provider
     * selection strategy is created.
     * 
     * @return The I18N context provider selection strategy
     */
    static @NotNull I18nContextProviderStrategy createStrategy() {
        return createStrategy(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Creates a I18N context provider selection strategy based on
     * configuration file in specified class loader. If no configuration file
     * is found a default I18N context provider selection strategy is created.
     * 
     * @param cl The class loader to load the configuration from
     * @return The I18N context provider selection strategy
     */
    static @NotNull I18nContextProviderStrategy createStrategy(
            final @NotNull ClassLoader cl) {
        final Properties config = loadConfiguration(cl);
        I18nContextProviderStrategy result = getCustomStrategy(cl, config);
        if (result == null) {
            result = new DefaultI18nContextProviderStrategy();
        }
        return result;
    }

    /**
     * Loads the configuration properties from the configuration file
     * {@code CONFIG_FILE}. If no such file exists returns an empty
     * {@code Properties}.
     * 
     * @param cl The class loader to load the configuration from
     * @return The configuration properties
     * @see #CONFIG_FILE
     */
    static @NotNull Properties loadConfiguration(
            final @NotNull ClassLoader cl) {
        Validate.notNull(cl);
        final Properties config = new Properties();
        try (final InputStream input = cl.getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                config.load(input);
            }
        } catch (final IOException ioe) {
            LOG.error("Error loading Orne I18N configuration file.", ioe);
        }
        return config;
    }

    /**
     * Creates the configured I18N context provider selection strategy.
     * <p>
     * <ol>
     * <li>If no custom strategy class is configured returns {@code null}.</li>
     * <li>If a custom strategy class is configured tries to call a
     * constructor with configuration {@code Properties} parameter.</li>
     * <li>If no such constructor exists or an error occurs tries to call
     * default empty constructor.</li>
     * <li>If both constructor fail or are missing returns {@code null}.</li>
     * 
     * @param cl The class loader used to find the custom strategy class
     * @param config The configuration properties
     * @return The configured I18N context provider selection strategy, or
     * {@code null} if no custom strategy is configured or cannot be created
     */
    static I18nContextProviderStrategy getCustomStrategy(
            final @NotNull ClassLoader cl,
            final @NotNull Properties config) {
        I18nContextProviderStrategy result = null;
        final Class<? extends I18nContextProviderStrategy> clazz = getCustomStrategyClass(cl, config);
        if (clazz != null) {
            try {
                final Constructor<? extends I18nContextProviderStrategy> configCtr =
                        clazz.getConstructor(Properties.class);
                result = configCtr.newInstance(config);
            } catch (final NoSuchMethodException ignore) {
                // Ignored
            } catch (final Exception e) {
                LOG.warn("Error calling custom I18N context provider strategy class Properties constructor", e);
            }
            if (result == null) {
                try {
                    result = clazz.getDeclaredConstructor().newInstance();
                } catch (final Exception e) {
                    LOG.error("Error instantiating custom I18N context provider strategy class with default constructor.", e);
                }
            }
        }
        return result;
    }

    /**
     * Finds the configured custom I18N context provider selection strategy
     * class. If no class is configured, cannot be found or doesn't implement
     * {@code I18nContextProviderStrategy} returns {@code null}.
     * 
     * @param cl The class loader used to find the custom strategy class
     * @param config The configuration properties
     * @return The valid configured custom strategy class, or {@code null}
     * @see #STRATEGY_PROP
     */
    @SuppressWarnings("unchecked")
    static Class<? extends I18nContextProviderStrategy> getCustomStrategyClass(
            final @NotNull ClassLoader cl,
            final @NotNull Properties config) {
        Class<? extends I18nContextProviderStrategy> result = null;
        if (config.containsKey(STRATEGY_PROP)) {
            final String strategyClassName = config.getProperty(STRATEGY_PROP);
            try {
                final Class<?> clazz = Class.forName(strategyClassName, true, cl);
                if (I18nContextProviderStrategy.class.isAssignableFrom(clazz)) {
                    result = (Class<? extends I18nContextProviderStrategy>) clazz;
                } else {
                    LOG.error(
                            "Configured custom I18N context provider strategy class {} is not valid",
                            strategyClassName);
                }
            } catch (final ClassNotFoundException cnfe) {
                LOG.error("Cannot found configured custom I18N context provider strategy class", cnfe);
            }
        }
        return result;
    }

    /**
     * Returns the {@code I18nContextProvider} to be used by current
     * {@code Thread}.
     * <p>
     * Shortcut for {@code getContextProviderStrategy().getContextProvider()}.
     * 
     * @return The {@code I18nContextProvider} to be used by current
     * {@code Thread}
     * @see I18nContextProviderStrategy#getContextProvider()
     */
    public static @NotNull I18nContextProvider getContextProvider() {
        return getContextProviderStrategy().getContextProvider();
    }

    /**
     * Returns the languages supported by the application.
     * <p>
     * Shortcut for {@code getContextProvider().getAvailableLocales()}.
     * 
     * @return The languages supported by the application
     * @see I18nContextProvider#getAvailableLocales()
     */
    public static @NotNull Locale[] getAvailableLocales() {
        return getContextProvider().getAvailableLocales();
    }

    /**
     * Returns the default I18N resources of the application.
     * <p>
     * Shortcut for {@code getContextProvider().getDefaultI18nResources()}.
     * 
     * @return The default I18N resources
     * @see I18nContextProvider#getDefaultI18nResources()
     */
    public static @NotNull I18nResources getDefaultI18nResources() {
        return getContextProvider().getDefaultI18nResources();
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * <p>
     * Shortcut for {@code getContextProvider().getI18nResources(key)}.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     * @see I18nContextProvider#getI18nResources(String)
     */
    public static @NotNull I18nResources getI18nResources(
            final String key) {
        return getContextProvider().getI18nResources(key);
    }

    /**
     * Return the {@code I18nContext} associated with the current
     * {@code Thread}.
     * <p>
     * If no {@code I18nContext} exists for the current {@code Thread} or the
     * existing one is not alive anymore a new one is created.
     * <p>
     * Shortcut for {@code getContextProvider().getContext()}.
     * 
     * @return The current {@code I18nContext}. Never {@code null}.
     * @see I18nContextProvider#getContext()
     */
    public static @NotNull I18nContext getContext() {
        return getContextProvider().getContext();
    }

    /**
     * Returns {@code true} if the specified context is alive or should be
     * discarded.
     * <p>
     * Shortcut for {@code getContextProvider().isContextAlive(context)}.
     * 
     * @param context The I18N context
     * @return {@code true} if the specified context is alive
     * @see I18nContextProvider#isContextAlive(I18nContext)
     */
    public static boolean isContextAlive(
            final @NotNull I18nContext context) {
        return getContextProvider().isContextAlive(context);
    }

    /**
     * Clears the I18nContext for the current thread.
     * <p>
     * Shortcut for {@code getContextProvider().getLocale()}.
     * @see I18nContextProvider#clearContext()
     */
    public static void clearContext() {
        getContextProvider().clearContext();
    }

    /**
     * Returns the current {@code I18nContext} locale.
     * <p>
     * Shortcut for {@code getContext().getLocale()}.
     * 
     * @return The current {@code I18nContext} locale
     * @see I18nContext#getLocale()
     */
    public static @NotNull Locale getLocale() {
        return getContext().getLocale();
    }

    /**
     * Sets the locale of the current {@code I18nContext}.
     * <p>
     * Shortcut for {@code getContext().setLocale(locale)}.
     * 
     * @param locale The current {@code I18nContext} locale
     * @see I18nContext#setLocale(Locale)
     */
    public static void setLocale(final Locale locale) {
        getContext().setLocale(locale);
    }
}
