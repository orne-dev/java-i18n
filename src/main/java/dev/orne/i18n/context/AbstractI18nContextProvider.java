package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2022 Orne Developments
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Abstract implementation of {@code I18nContextProvider}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2022-12
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public abstract class AbstractI18nContextProvider
implements I18nContextProvider {

    /** The UUID of this provider instance. */
    private @NotNull UUID sessionUUID = UUID.randomUUID();
    /** The default locale supplier. */
    private final @NotNull Supplier<@NotNull Locale> defaultLocaleSupplier;
    /** The available locales. */
    private final @NotNull @NotNull Locale[] availableLocales;
    /** The default I18N resources. */
    private final @NotNull I18nResources defaultI18nResources;
    /** The alternative I18N resources by key. */
    private final @NotNull Map<@NotNull String, @NotNull I18nResources> i18nResources;

    /**
     * Creates a new instance based on specified builder.
     * 
     * @param builder The I18N context provider builder.
     */
    protected AbstractI18nContextProvider(
            final @NotNull BuilderImpl<?, ?> builder) {
        super();
        this.defaultLocaleSupplier = builder.defaultLocaleSupplier;
        this.availableLocales = builder.availableLocales;
        this.defaultI18nResources = builder.defaultI18nResources;
        this.i18nResources = Collections.unmodifiableMap(
                new HashMap<>(builder.i18nResources));
    }

    /**
     * Returns the UUID of this provider instance and session.
     * Used to check contexts validity. Constant from instance creation to
     * call to {@code invalidate()}
     * 
     * @return The UUID of this provider instance
     */
    public @NotNull UUID getSessionUUID() {
        return this.sessionUUID;
    }

    /**
     * Returns the default locale supplier.
     * 
     * @return The default locale supplier.
     */
    protected @NotNull Supplier<@NotNull Locale> getDefaultLocaleSupplier() {
        return this.defaultLocaleSupplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Locale[] getAvailableLocales() {
        return Arrays.copyOf(availableLocales, availableLocales.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nResources getDefaultI18nResources() {
        return this.defaultI18nResources;
    }

    /**
     * Returns the alternative I18N resources by key.
     * 
     * @return The alternative I18N resources by key
     */
    public @NotNull Map<@NotNull String, @NotNull I18nResources> getI18nResources() {
        return Collections.unmodifiableMap(this.i18nResources);
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     */
    public @NotNull I18nResources getI18nResources(
            final String key) {
        if (key == null) {
            return this.defaultI18nResources;
        } else {
            return this.i18nResources.getOrDefault(key, this.defaultI18nResources);
        }
    }

    /**
     * Creates a new I18N context with default values.
     * 
     * @return The new I18N context
     */
    public @NotNull I18nContext createContext() {
        final I18nContext context = new DefaultI18nContext(this.sessionUUID);
        context.setLocale(getDefaultLocaleSupplier().get());
        return context;
    }

    /**
     * Creates a new I18N context with values inherited from the specified
     * parent I18N context.
     * 
     * @param parent The parent I18N context
     * @return The new I18N context
     */
    public @NotNull I18nContext createContext(
            final @NotNull I18nContext parent) {
        Validate.notNull(parent);
        final I18nContext context = createContext();
        context.setLocale(parent.getLocale());
        return context;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation resets available languages, and I18N resources to
     * defaults and generates a new session UUID to invalidate any existing
     * contexts.
     */
    @Override
    public synchronized void invalidate() {
        this.sessionUUID = UUID.randomUUID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.defaultLocaleSupplier.get())
                .append(this.availableLocales)
                .append(this.defaultI18nResources)
                .append(this.i18nResources)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (!getClass().equals(obj.getClass())) { return false; }
        final AbstractI18nContextProvider other = (AbstractI18nContextProvider) obj;
        return new EqualsBuilder()
                .append(this.defaultLocaleSupplier.get(), other.defaultLocaleSupplier.get())
                .append(this.availableLocales, other.availableLocales)
                .append(this.defaultI18nResources, other.defaultI18nResources)
                .append(this.i18nResources, other.i18nResources)
                .isEquals();
    }

    /**
     * Abstract builder of I18N context provider instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @param <T> The type of I18N context provider build by the builder.
     * @param <B> The type of builder returned for method chaining.
     * @since 0.1
     */
    protected abstract static class BuilderImpl<
            T extends AbstractI18nContextProvider,
            B extends BuilderImpl<T, B>> {

        /** The default language supplier. */
        protected @NotNull Supplier<@NotNull Locale> defaultLocaleSupplier =
                Locale::getDefault;
        /** The supported languages. */
        protected @NotNull Locale[] availableLocales =
                Locale.getAvailableLocales();
        /** The default I18N resources. */
        protected @NotNull I18nResources defaultI18nResources =
                DummyI18nResources.getInstance();
        /** The alternative I18N resources by key. */
        protected final @NotNull Map<@NotNull String, @NotNull I18nResources> i18nResources =
                new HashMap<>();

        /**
         * Creates a new instance.
         */
        protected BuilderImpl() {
            super();
        }

        /**
         * Configures the builder with specified I18N configuration.
         * 
         * @param config The I18N configuration.
         * @return This builder, for method chaining.
         * @see I18nContextProvider.Builder#configure(Properties)
         */
        @SuppressWarnings("unchecked")
        public @NotNull B configure(
                @NotNull Properties config) {
            configureDefaultLocaleSupplier(config);
            configureAvailableLocalesSupplier(config);
            configureDefaultI18nResources(config);
            configureAlternativeI18nResources(config);
            return (B) this;
        }

        /**
         * Configures the default language based on specified configuration.
         * 
         * @param config The I18N configuration.
         */
        protected void configureDefaultLocaleSupplier(
                final @NotNull Properties config) {
            if (config.containsKey(I18nConfiguration.DEFAULT_LANGUAGE)) {
                final Locale locale = new Locale(config.getProperty(I18nConfiguration.DEFAULT_LANGUAGE));
                setDefaultLocaleSupplier(() -> locale);
            }
        }

        /**
         * Configures the supported languages based on specified configuration.
         * 
         * @param config The I18N configuration.
         */
        protected void configureAvailableLocalesSupplier(
                final @NotNull Properties config) {
            if (config.containsKey(I18nConfiguration.AVAILABLE_LANGUAGES)) {
                final String[] langs = StringUtils.split(
                        config.getProperty(I18nConfiguration.AVAILABLE_LANGUAGES),
                        ",");
                final Locale[] locales = new Locale[langs.length];
                for (int i = 0; i < langs.length; i++) {
                    locales[i] = new Locale(langs[i]);
                }
                setAvailableLocales(locales);
            }
        }

        /**
         * Configures the default I18N resources based on specified configuration.
         * 
         * @param config The I18N configuration.
         */
        protected void configureDefaultI18nResources(
                final @NotNull Properties config) {
            if (config.containsKey(I18nConfiguration.DEFAULT_RESOURCES)) {
                setDefaultI18nResources(I18nBundleResources.forBasename(
                        config.getProperty(I18nConfiguration.DEFAULT_RESOURCES)));
            }
        }

        /**
         * Configures the alternative I18N resources based on specified configuration.
         * 
         * @param config The I18N configuration.
         */
        protected void configureAlternativeI18nResources(
                final @NotNull Properties config) {
            for (final String prop : config.stringPropertyNames()) {
                if (prop.startsWith(I18nConfiguration.NAMED_RESOURCES_PREFIX)) {
                    final String resourceName = prop.substring(I18nConfiguration.NAMED_RESOURCES_PREFIX.length());
                    addI18nResources(
                            resourceName,
                            I18nBundleResources.forBasename(config.getProperty(prop)));
                }
            }
        }

        /**
         * Sets the default locale supplier.
         * 
         * @param supplier The default locale supplier.
         * @return This instance, for method chaining.
         * @see I18nContextProvider.Builder#setDefaultLocaleSupplier(Supplier)
         */
        @SuppressWarnings("unchecked")
        public @NotNull B setDefaultLocaleSupplier(
                final @NotNull Supplier<@NotNull Locale> supplier) {
            this.defaultLocaleSupplier = supplier;
            return (B) this;
        }

        /**
         * Sets the available locales.
         * 
         * @param locales The available locales.
         * @return This instance, for method chaining.
         * @see I18nContextProvider.Builder#setAvailableLocales(Locale[])
         */
        @SuppressWarnings("unchecked")
        public @NotNull B setAvailableLocales(
                final @NotNull Locale[] locales) {
            this.availableLocales = Validate.notNull(locales);
            return (B) this;
        }

        /**
         * Sets the default I18N resources.
         * 
         * @param resources The default I18N resources
         * @return This instance, for method chaining.
         * @see I18nContextProvider.Builder#setDefaultI18nResources(I18nResources)
         */
        @SuppressWarnings("unchecked")
        public @NotNull B setDefaultI18nResources(
                final @NotNull I18nResources resources) {
            this.defaultI18nResources = Validate.notNull(resources);
            return (B) this;
        }

        /**
         * Adds alternative I18N resources to be used when the specified key is
         * used.
         * 
         * @param key The key of the alternative I18N resources
         * @param resource The alternative I18N resources
         * @return This instance, for method chaining.
         * @see I18nContextProvider.Builder#addI18nResources(String, I18nResources)
         */
        @SuppressWarnings("unchecked")
        public @NotNull B addI18nResources(
                final @NotNull String key,
                final @NotNull I18nResources resource) {
            this.i18nResources.put(
                    Validate.notNull(key),
                    Validate.notNull(resource));
            return (B) this;
        }
    }
}
