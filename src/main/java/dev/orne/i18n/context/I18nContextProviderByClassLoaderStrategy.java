package dev.orne.i18n.context;

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

import java.util.Map;
import java.util.Properties;
import java.util.WeakHashMap;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import jakarta.validation.constraints.NotNull;

/**
 * Implementation of {@code I18nContextProviderStrategy} that determines
 * the {@code I18nContextProvider} based on the context {@code ClassLoader}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProviderStrategy
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class I18nContextProviderByClassLoaderStrategy
extends DefaultI18nContextProviderStrategy
implements I18nContextProviderConfigurableStrategy {

    /** The I18N context provider strategy type. */
    public static final String TYPE = "CLASSLOADER";

    /** The {@code I18nContextProvider} mapping per {@code ClassLoader}. */
    private final @NotNull Map<@NotNull ClassLoader, dev.orne.i18n.context.I18nContextProvider> contextProviders;

    /**
     * Creates a new instance with an default instance of
     * {@code DefaultI18nContextProvider} as default
     * {@code I18nContextProvider}.
     */
    public I18nContextProviderByClassLoaderStrategy() {
        this(new DefaultI18nContextProvider());
    }

    /**
     * Creates a new instance with the specified default
     * {@code I18nContextProvider}.
     * 
     * @param defaultContextProvider The default {@code I18nContextProvider}
     */
    public I18nContextProviderByClassLoaderStrategy(
            final @NotNull I18nContextProvider defaultContextProvider) {
        this(defaultContextProvider, new WeakHashMap<>());
    }

    /**
     * Creates a new instance with the specified default
     * {@code I18nContextProvider} that uses the passed {@code Map} as the
     * internal {@code I18nContextProvider} mapping per {@code ClassLoader}.
     * <p>
     * The {@code Map} is used as is (not a copy). Use with caution.
     * 
     * @param defaultContextProvider The default {@code I18nContextProvider}
     * @param contextProviders The {@code I18nContextProvider} mapping per
     * {@code ClassLoader}
     */
    protected I18nContextProviderByClassLoaderStrategy(
            final @NotNull I18nContextProvider defaultContextProvider,
            final @NotNull Map<@NotNull ClassLoader, dev.orne.i18n.context.I18nContextProvider> contextProviders) {
        super(defaultContextProvider);
        Validate.notNull(contextProviders);
        Validate.noNullElements(contextProviders.keySet());
        Validate.noNullElements(contextProviders.values());
        this.contextProviders = contextProviders;
    }

    /**
     * Returns the internal {@code I18nContextProvider} mapping per
     * {@code ClassLoader}.
     * <p>
     * The internal {@code Map} instance is returned (not a copy).
     * Use with caution.
     * 
     * @return The {@code I18nContextProvider} mapping per {@code ClassLoader}
     */
    protected @NotNull Map<@NotNull ClassLoader, dev.orne.i18n.context.I18nContextProvider> getContextProviders() {
        return this.contextProviders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull I18nContextProvider getContextProvider() {
        return getContextProvider(Thread.currentThread());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull I18nContextProvider getContextProvider(
            final @NotNull Thread thread) {
        return getContextProvider(
                Validate.notNull(thread).getContextClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull I18nContextProvider getContextProvider(
            final @NotNull ClassLoader classLoader) {
        if (this.contextProviders.containsKey(Validate.notNull(classLoader))) {
            return this.contextProviders.get(classLoader);
        } else {
            I18nContextProvider inheritedResult;
            final ClassLoader parentClassLoader = classLoader.getParent();
            if (parentClassLoader == null) {
                inheritedResult = getDefaultContextProvider();
            } else {
                inheritedResult = getContextProvider(parentClassLoader);
            }
            this.contextProviders.put(classLoader, inheritedResult);
            return inheritedResult;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setContextProvider(
            final @NotNull I18nContextProvider provider) {
        setContextProvider(
                Thread.currentThread(),
                provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setContextProvider(
            final @NotNull Thread thread,
            final @NotNull I18nContextProvider provider) {
        setContextProvider(
                Validate.notNull(thread).getContextClassLoader(),
                provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void setContextProvider(
            final @NotNull ClassLoader classLoader,
            final @NotNull I18nContextProvider provider) {
        this.contextProviders.put(
                Validate.notNull(classLoader),
                Validate.notNull(provider));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void invalidate() {
        for (final I18nContextProvider provider : this.contextProviders.values()) {
            provider.invalidate();
        }
        super.invalidate();
    }

    /**
     * Class loader based I18N context provider strategy configuration
     * service provider.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2023-12
     * @since 0.1
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static class Configurer
    implements I18nContextProviderStrategyConfigurer {

        /**
         * Creates a new instance.
         */
        public Configurer() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getType() {
            return TYPE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull I18nContextProviderStrategy create(
                @NotNull Properties config) {
            return new I18nContextProviderByClassLoaderStrategy();
        }
    }
}
