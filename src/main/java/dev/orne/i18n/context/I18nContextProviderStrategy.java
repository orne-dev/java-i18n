package dev.orne.i18n.context;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.commons.lang3.Validate;

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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.orne.i18n.I18nConfigurationException;
import jakarta.validation.constraints.NotNull;

/**
 * Strategy of selection of {@code I18nContextProvider}.
 * Provides a method to retrieve the provider for the current
 * {@code Thread}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nContextProviderStrategy {

    /**
     * Returns the I18N context provider selection strategy.
     * 
     * @return The I18N context provider selection strategy
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static @NotNull I18nContextProviderStrategy getInstance() {
        synchronized (I18nContextProviderStrategyLoader.class) {
            if (I18nContextProviderStrategyLoader.instance == null) {
                I18nContextProviderStrategyLoader.instance = I18nContextProviderStrategyLoader.createStrategy();
            }
            return I18nContextProviderStrategyLoader.instance;
        }
    }

    /**
     * Sets the I18N context provider selection strategy, invalidating
     * current strategy if any.
     * <p>
     * If {@code strategy} is {@code null} next call to {@code getInstance()}
     * will create strategy based on application configuration. 
     * 
     * @param strategy The I18N context provider selection strategy
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static void setInstance(
            final I18nContextProviderStrategy strategy) {
        synchronized (I18nContextProviderStrategyLoader.class) {
            if (I18nContextProviderStrategyLoader.instance != null) {
                I18nContextProviderStrategyLoader.instance.invalidate();
            }
            I18nContextProviderStrategyLoader.instance = strategy;
        }
    }

    /**
     * Returns the default {@code I18nContextProvider}.
     * 
     * @return The default {@code I18nContextProvider}
     */
    @NotNull I18nContextProvider getDefaultContextProvider();

    /**
     * Sets the default {@code I18nContextProvider}.
     * 
     * @param provider The default {@code I18nContextProvider}
     */
    void setDefaultContextProvider(
            @NotNull I18nContextProvider provider);

    /**
     * Returns the {@code I18nContextProvider} to be used for the current
     * {@code Thread}.
     * 
     * @return The {@code I18nContextProvider} for the current {@code Thread}
     */
    @NotNull I18nContextProvider getContextProvider();

    /**
     * Invalidates this strategy and all context providers, thus invalidating
     * any previously created I18N contexts.
     * <p>
     * Effects of calling any other method after this one is left to
     * implementations choice, and thus is discouraged.
     */
    void invalidate();

    /**
     * Private container of the configured context provider selection strategy.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-11
     * @see I18nContextProviderStrategy
     * @since 0.1
     */
    final class I18nContextProviderStrategyLoader {

        /** The class logger. */
        private static final Logger LOG = LoggerFactory.getLogger(I18nContextProviderStrategyLoader.class);

        /**
         * Private constructor.
         */
        private I18nContextProviderStrategyLoader() {
            // Utility class
        }

        /** The I18N context provider selection strategy. */
        private static I18nContextProviderStrategy instance;

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
            try (final InputStream input = cl.getResourceAsStream(I18nConfiguration.FILE)) {
                if (input != null) {
                    config.load(input);
                }
            } catch (final IOException ioe) {
                LOG.error("Error loading I18N configuration file. Using default configuration.", ioe);
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
                    throw new I18nConfigurationException(
                            "Error instantiating custom I18N context provider strategy class with Properties constructor",
                            e);
                }
                if (result == null) {
                    try {
                        result = clazz.getDeclaredConstructor().newInstance();
                    } catch (final Exception e) {
                        throw new I18nConfigurationException(
                                "Error instantiating custom I18N context provider strategy class with default constructor.",
                                e);
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
            if (config.containsKey(I18nConfiguration.STRATEGY)) {
                final String strategyClassName = config.getProperty(I18nConfiguration.STRATEGY);
                try {
                    final Class<?> clazz = Class.forName(strategyClassName, true, cl);
                    if (I18nContextProviderStrategy.class.isAssignableFrom(clazz)) {
                        result = (Class<? extends I18nContextProviderStrategy>) clazz;
                    } else {
                        throw new I18nConfigurationException(String.format(
                                "Configured custom I18N context provider strategy class '%s' is not valid",
                                strategyClassName));
                    }
                } catch (final ClassNotFoundException e) {
                    throw new I18nConfigurationException(
                            "Cannot found configured custom I18N context provider strategy class",
                            e);
                }
            }
            return result;
        }
    }
}
