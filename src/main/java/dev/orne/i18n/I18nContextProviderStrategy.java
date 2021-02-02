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
public interface I18nContextProviderStrategy {

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
}
