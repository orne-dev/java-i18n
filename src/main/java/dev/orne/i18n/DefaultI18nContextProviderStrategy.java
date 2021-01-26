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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Default implementation of {@code I18nContextProviderStrategy} that returns
 * same {@code I18nContextProvider} for all calls.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProviderStrategy
 * @since 0.1
 */
public class DefaultI18nContextProviderStrategy
implements I18nContextProviderStrategy {

    /** The default {@code I18nContextProvider}. */
    private @NotNull I18nContextProvider defaultContextProvider;

    /**
     * Creates a new instance with an default instance of
     * {@code DefaultI18nContextProvider} as default
     * {@code I18nContextProvider}.
     */
    public DefaultI18nContextProviderStrategy() {
        this(new DefaultI18nContextProvider());
    }

    /**
     * Creates a new instance with the specified default
     * {@code I18nContextProvider}.
     * 
     * @param defaultContextProvider The default {@code I18nContextProvider}
     */
    public DefaultI18nContextProviderStrategy(
            final @NotNull I18nContextProvider defaultContextProvider) {
        this.defaultContextProvider = Validate.notNull(defaultContextProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nContextProvider getDefaultContextProvider() {
        return this.defaultContextProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultContextProvider(
            final @NotNull I18nContextProvider provider) {
        this.defaultContextProvider = Validate.notNull(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nContextProvider getContextProvider() {
        return this.defaultContextProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidate() {
        this.defaultContextProvider.invalidate();
    }
}
