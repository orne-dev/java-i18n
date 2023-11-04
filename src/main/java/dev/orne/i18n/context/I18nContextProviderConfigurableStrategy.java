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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import jakarta.validation.constraints.NotNull;

/**
 * Configurable strategy of selection of {@code I18nContextProvider}.
 * Provides methods for retrieving or setting the provider for the current
 * {@code Thread}, a specified {@code Thread} and a specified
 * {@code ClassLoader}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nContextProviderConfigurableStrategy
extends I18nContextProviderStrategy {


    /**
     * Returns the {@code I18nContextProvider} to be used for the specified
     * {@code Thread}.
     * 
     * @param thread The {@code Thread}
     * @return The {@code I18nContextProvider} for the specified
     * {@code Thread}
     */
    @NotNull I18nContextProvider getContextProvider(
            final @NotNull Thread thread);

    /**
     * Returns the {@code I18nContextProvider} to be used for the specified
     * {@code ClassLoader}.
     * 
     * @param classLoader The {@code ClassLoader}
     * @return The {@code I18nContextProvider} for the specified
     * {@code ClassLoader}
     */
    @NotNull I18nContextProvider getContextProvider(
            @NotNull ClassLoader classLoader);

    /**
     * Sets the {@code I18nContextProvider} to be used for the current
     * {@code Thread}.
     * 
     * @param provider The {@code I18nContextProvider} for the current
     * {@code Thread}
     */
    void setContextProvider(
            @NotNull I18nContextProvider provider);

    /**
     * Set the {@code I18nContextProvider} to be used for the specified
     * {@code Thread}.
     * 
     * @param thread The {@code Thread}
     * @param provider The {@code I18nContextProvider} for the specified
     * {@code Thread}
     */
    void setContextProvider(
            @NotNull Thread thread,
            @NotNull I18nContextProvider provider);

    /**
     * Set the {@code I18nContextProvider} to be used for the specified
     * {@code ClassLoader}.
     * 
     * @param classLoader The {@code ClassLoader}
     * @param provider The {@code I18nContextProvider} for the specified
     * {@code ClassLoader}
     */
    void setContextProvider(
            @NotNull ClassLoader classLoader,
            @NotNull I18nContextProvider provider);
}
