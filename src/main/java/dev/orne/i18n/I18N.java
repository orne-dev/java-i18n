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

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.context.I18nContext;

/**
 * Main entry point to I18N framework. Provides methods for configuration and
 * manipulation of I18N contexts.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public final class I18N {

    /**
     * Private constructor.
     */
    private I18N() {
        // Utility class
    }

    /**
     * Returns the languages supported by the application.
     * <p>
     * Shortcut for {@code I18nContext.getInstance().getAvailableLocales()}.
     * 
     * @return The languages supported by the application
     * @see I18nContext#getAvailableLocales()
     */
    public static @NotNull Locale[] getAvailableLocales() {
        return I18nContext.getInstance().getAvailableLocales();
    }

    /**
     * Returns the default I18N resources of the application.
     * <p>
     * Shortcut for {@code I18nContext.getInstance().getI18nResources()}.
     * 
     * @return The default I18N resources
     * @see I18nContext#getI18nResources()
     */
    public static @NotNull I18nResources getResources() {
        return I18nContext.getInstance().getI18nResources();
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * <p>
     * Shortcut for {@code I18nContext.getInstance().getI18nResources(key)}.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     * @see I18nContext#getI18nResources(String)
     */
    public static @NotNull I18nResources getResources(
            final String key) {
        return I18nContext.getInstance().getI18nResources(key);
    }

    /**
     * Returns the current locale.
     * 
     * @return The current locale
     */
    public static @NotNull Locale getLocale() {
        return I18nContext.getInstance().getLocale();
    }

    /**
     * Sets the current locale.
     * 
     * @param locale The current locale
     */
    public static void setLocale(final Locale locale) {
        I18nContext.getInstance().setLocale(locale);
    }
}
