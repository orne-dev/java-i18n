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

import java.util.Map;
import java.util.WeakHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code I18nContextProviderStrategy} that determines
 * the {@code I18nContextProvider} based on the context {@code ClassLoader}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProviderStrategy
 * @since 0.1
 */
public class I18nContextProviderByClassLoaderStrategy
extends DefaultI18nContextProviderStrategy
implements I18nContextProviderConfigurableStrategy {

    /** The {@code I18nContextProvider} mapping per {@code ClassLoader}. */
    private final @NotNull Map<ClassLoader, I18nContextProvider> contextProviders;

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
            final @NotNull Map<ClassLoader, I18nContextProvider> contextProviders) {
        super(defaultContextProvider);
        this.contextProviders = Validate.notNull(contextProviders);
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
    protected Map<ClassLoader, I18nContextProvider> getContextProviders() {
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
        return this.contextProviders.computeIfAbsent(
                Validate.notNull(classLoader),
                key -> {
                    I18nContextProvider inheritedResult;
                    final ClassLoader parentClassLoader = classLoader.getParent();
                    if (parentClassLoader == null) {
                        inheritedResult = getDefaultContextProvider();
                    } else {
                        inheritedResult = getContextProvider(parentClassLoader);
                    }
                    return inheritedResult;
                });
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
}
