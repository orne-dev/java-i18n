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
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Interface for I18N context. Contains the data required to return the I18N
 * texts in user's language.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nContext {

    /**
     * Return the {@code I18nContext} associated with the current
     * {@code Thread}.
     * 
     * @return The I18N context.
     */
    @API(status=Status.EXPERIMENTAL, since="0.1")
    public static @NotNull I18nContext getInstance() {
        return I18nContextProvider.getInstance().getContext();
    }

    /**
     * Returns the UUID of the provider owner of this context.
     * 
     * @return The UUID of the provider owner of this context
     */
    @NotNull UUID getProviderUUID();

    /**
     * Returns the user's language.
     * 
     * @return The user's language
     */
    @NotNull Locale getLocale();

    /**
     * Sets the user's language. If the argument is {@code null} the JVM
     * default language is set.
     * 
     * @param language The user's language to set
     */
    void setLocale(Locale language);

    /**
     * Returns the languages supported by the application.
     * 
     * @return The languages supported by the application
     */
    default @NotNull Locale[] getAvailableLocales() {
        return I18nContextProvider.getInstance().getAvailableLocales();
    }

    /**
     * Returns the default I18N resources of the application.
     * 
     * @return The default I18N resources
     */
    default @NotNull I18nResources getI18nResources() {
        return I18nContextProvider.getInstance().getDefaultI18nResources();
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     */
    default @NotNull I18nResources getI18nResources(
            final String key) {
        return I18nContextProvider.getInstance().getI18nResources(key);
    }
}
