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

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Provider of {@code I18nContext} instances. Provides methods to create new
 * I18n contexts, to check if a given context is still alive and to access
 * default I18N resources of the application.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
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
        return I18nContextProviderStrategy.getInstance().getContextProvider();
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
}
