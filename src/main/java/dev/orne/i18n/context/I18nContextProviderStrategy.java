package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2023 Orne Developments
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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

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
        synchronized (InstanceHolder.class) {
            if (InstanceHolder.instance == null) {
                InstanceHolder.instance = I18nContextProviderStrategyConfigurer.configure();
            }
            return InstanceHolder.instance;
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
        synchronized (InstanceHolder.class) {
            if (InstanceHolder.instance != null) {
                InstanceHolder.instance.invalidate();
            }
            InstanceHolder.instance = strategy;
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
    final class InstanceHolder {

        /**
         * Private constructor.
         */
        private InstanceHolder() {
            // Utility class
        }

        /** The I18N context provider selection strategy. */
        private static I18nContextProviderStrategy instance;
    }
}
