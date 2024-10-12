package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2024 Orne Developments
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

import java.util.Locale;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nConfigurationException;
import dev.orne.i18n.I18nResources;

/**
 * Provider of {@code I18nContext} instances. Provides methods to create new
 * I18n contexts, to check if a given context is still alive and to access
 * default I18N resources of the application.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @see I18nResources
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nContextProvider {

    /**
     * Returns the I18N context provider.
     * 
     * @return The I18N context provider.
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static @NotNull I18nContextProvider getInstance() {
        return Registry.get();
    }

    /**
     * Returns the supported languages.
     * 
     * @return The supported languages
     */
    @NotNull Locale[] getAvailableLocales();

    /**
     * Returns the default I18N resources.
     * 
     * @return The default I18N resources
     */
    @NotNull I18nResources getDefaultI18nResources();

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     */
    @NotNull I18nResources getI18nResources(String key);

    /**
     * Return the {@code I18nContext} associated with the current
     * {@code Thread}.
     * <p>
     * If no {@code I18nContext} exists for the current {@code Thread} or the
     * existing one is not alive anymore a new one is created.
     * 
     * @return The current {@code I18nContext}. Never {@code null}.
     */
    @NotNull I18nContext getContext();

    /**
     * Returns {@code true} if the specified context is valid or should be
     * discarded.
     * 
     * @param context The I18N context
     * @return {@code true} if the specified context is valid
     */
    boolean isContextValid(@NotNull I18nContext context);

    /**
     * Clears the I18N context for the current thread.
     */
    void clearContext();

    /**
     * Invalidates this context provider, thus invalidating
     * any previously created I18N contexts.
     * <p>
     * Effects of calling any other method after this one is left to
     * implementations choice, and thus is discouraged.
     */
    void invalidate();

    /**
     * Builder of I18N context provider instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @since 0.1
     */
    @API(status=Status.STABLE, since="0.1")
    public interface Builder {

        /**
         * Configures the builder with I18N configuration for the current class loader.
         * 
         * @return This builder, for method chaining.
         * @see I18nConfiguration#get()
         */
        default @NotNull Builder configure() {
            return configure(I18nConfiguration.get());
        }

        /**
         * Configures the builder with specified I18N configuration.
         * 
         * @param config The I18N configuration.
         * @return This instance, for method chaining.
         */
        @NotNull Builder configure(
                @NotNull Properties config);

        /**
         * Sets the default locale supplier.
         * 
         * @param supplier The default locale supplier.
         * @return This instance, for method chaining.
         */
        @NotNull Builder setDefaultLocaleSupplier(
                @NotNull Supplier<@NotNull Locale> supplier);

        /**
         * Sets the available locales.
         * 
         * @param locales The available locales.
         * @return This instance, for method chaining.
         */
        @NotNull Builder setAvailableLocales(
                @NotNull Locale[] locales);

        /**
         * Sets the default I18N resources.
         * 
         * @param resources The default I18N resources
         * @return This instance, for method chaining.
         */
        @NotNull Builder setDefaultI18nResources(
                @NotNull I18nResources resources);

        /**
         * Adds alternative I18N resources to be used when the specified key is
         * used.
         * 
         * @param key The key of the alternative I18N resources
         * @param resource The alternative I18N resources
         * @return This instance, for method chaining.
         */
        @NotNull Builder addI18nResources(
                @NotNull String key,
                @NotNull I18nResources resource);

        /**
         * Builds an immutable I18N context provider instance with the current
         * configuration of this builder. Further modifications to the builder
         * will have no effect in the returned instance.
         * 
         * @return The I18N context provider instance.
         */
        @NotNull I18nContextProvider build();
    }

    /**
     * The I18N context provider registry.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2021-01
     * @since 0.1
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    final class Registry {

        /** The per class loader I18N context provider cache. */
        private static final WeakHashMap<ClassLoader, I18nContextProvider> CACHE = new WeakHashMap<>();

        /**
         * Private constructor.
         */
        private Registry() {
            // Utility class
        }

        /**
         * Returns the I18N context provider for current thread context class loader.
         * 
         * @return The I18N context provider.
         */
        public static @NotNull I18nContextProvider get() {
            return get(Thread.currentThread().getContextClassLoader());
        }

        /**
         * Returns the I18N context provider for the specified class loader.
         * 
         * @param cl The class loader.
         * @return The I18N context provider.
         */
        public static synchronized @NotNull I18nContextProvider get(
                final @NotNull ClassLoader cl) {
            return CACHE.computeIfAbsent(cl, Registry::configure);
        }

        /**
         * Resets the I18N context provider cache.
         */
        static void reset() {
            CACHE.clear();
        }

        /**
         * Sets the I18N context provider for the current thread's context class loader.
         * 
         * @param provider The I18N context provider.
         */
        static void set(
                final @NotNull I18nContextProvider provider) {
            set(Thread.currentThread().getContextClassLoader(), provider);
        }

        /**
         * Sets the I18N context provider for the specified class loader.
         * 
         * @param cl The class loader.
         * @param provider The I18N context provider.
         */
        static synchronized void set(
                final @NotNull ClassLoader cl,
                final @NotNull I18nContextProvider provider) {
            Validate.notNull(cl);
            Validate.notNull(provider);
            CACHE.put(cl, provider);
        }

        /**
         * Configures the I18N context provider based on the
         * I18N configuration for the specified class loader.
         * <p>
         * If the class loader inherits configuration from parent then
         * parent class loader's context provider is assigned.
         * Otherwise a new context provider is created based on the
         * I18N configuration.
         * 
         * @param cl The class loader.
         * @return The configured I18N context provider.
         */
        static @NotNull I18nContextProvider configure(
                final @NotNull ClassLoader cl) {
            final Properties config = I18nConfiguration.get(cl);
            if (cl.getParent() != null) {
                final Properties parentConfig = I18nConfiguration.get(cl.getParent());
                if (config.equals(parentConfig)) {
                    return get(cl.getParent());
                }
            }
            return configure(config);
        }

        /**
         * Creates and configures a I18N context provider based on the
         * specified application I18N configuration.
         * 
         * @param config The I18N configuration.
         * @return The configured I18N context provider.
         */
        static @NotNull I18nContextProvider configure(
                final @NotNull Properties config) {
            String type = config.getProperty(I18nConfiguration.CONTEXT_PROVIDER);
            if (type == null) {
                type = ThreadI18nContextProvider.TYPE;
            }
            final ServiceLoader<I18nContextProviderFactory> loader =
                    ServiceLoader.load(I18nContextProviderFactory.class);
            for (final I18nContextProviderFactory configurer : loader) {
                if (type.equals(configurer.getType())) {
                    return configurer.create(config);
                }
            }
            throw new I18nConfigurationException("No I18N context provider found for configured type: " + type);
        }
    }

    /**
     * Interface for configuration classes that need to set I18N context
     * provider instances to use.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-08
     * @since 0.1
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    interface Configurer {

        /**
         * Sets the I18N configuration for the current thread's context class loader.
         * 
         * @param config The I18N configuration.
         */
        default void setI18nConfiguration(
                final @NotNull Properties config) {
            setI18nConfiguration(Thread.currentThread().getContextClassLoader(), config);
        }

        /**
         * Sets the I18N configuration for the specified class loader.
         * 
         * @param cl The class loader to configure.
         * @param config The I18N configuration.
         */
        default void setI18nConfiguration(
                final @NotNull ClassLoader cl,
                final @NotNull Properties config) {
            I18nConfiguration.set(cl, config);
        }

        /**
         * Sets the I18N context provider for the current thread's context class loader.
         * 
         * @param provider The I18N context provider.
         */
        default void setI18nContextProvider(
                final @NotNull I18nContextProvider provider) {
            setI18nContextProvider(Thread.currentThread().getContextClassLoader(), provider);
        }

        /**
         * Sets the I18N context provider for the specified class loader.
         * 
         * @param cl The class loader.
         * @param provider The I18N context provider.
         */
        default void setI18nContextProvider(
                final @NotNull ClassLoader cl,
                final @NotNull I18nContextProvider provider) {
            Registry.set(cl, provider);
        }
    }
}
