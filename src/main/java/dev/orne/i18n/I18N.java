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

import org.apache.commons.lang3.Validate;

/**
 * Main entry point to I18N framework. Provides methods for configuration and
 * manipulation of I18N contexts.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @since 0.1
 */
public final class I18N {

    /** The I18N context provider selection strategy. */
    private static @NotNull I18nContextProviderStrategy contextProviderStrategy =
            new ByClassLoaderI18nContextProviderStrategy(
                new DefaultI18nContextProvider(true));

    /**
     * Private constructor.
     */
    private I18N() {
        // Utility class
    }

    /**
     * Returns the I18N context provider selection strategy.
     * 
     * @return The I18N context provider selection strategy
     */
    public static @NotNull I18nContextProviderStrategy getContextProviderStrategy() {
        return I18N.contextProviderStrategy;
    }

    /**
     * Sets the I18N context provider selection strategy.
     * 
     * @param strategy The I18N context provider selection strategy
     */
    public static void setContextProviderStrategy(
            final @NotNull I18nContextProviderStrategy strategy) {
        I18N.contextProviderStrategy = Validate.notNull(strategy);
    }

    /**
     * Returns the {@code I18nContextProvider} to be used by current
     * {@code Thread}.
     * <p>
     * Shortcut for {@code getContextProviderStrategy().getContextProvider()}.
     * 
     * @return The {@code I18nContextProvider} to be used by current
     * {@code Thread}
     * @see I18nContextProviderStrategy#getContextProvider()
     */
    public static @NotNull I18nContextProvider getContextProvider() {
        return I18N.contextProviderStrategy.getContextProvider();
    }

    /**
     * Returns the languages supported by the application.
     * <p>
     * Shortcut for {@code getContextProvider().getAvailableLocales()}.
     * 
     * @return The languages supported by the application
     * @see I18nContextProvider#getAvailableLocales()
     */
    public static @NotNull Locale[] getAvailableLocales() {
        return getContextProvider().getAvailableLocales();
    }

    /**
     * Returns the default I18N resources of the application.
     * <p>
     * Shortcut for {@code getContextProvider().getDefaultI18nResources()}.
     * 
     * @return The default I18N resources
     * @see I18nContextProvider#getDefaultI18nResources()
     */
    public static @NotNull I18nResources getDefaultI18nResources() {
        return getContextProvider().getDefaultI18nResources();
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * <p>
     * Shortcut for {@code getContextProvider().getI18nResources(key)}.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     * @see I18nContextProvider#getI18nResources(String)
     */
    public static @NotNull I18nResources getI18nResources(
            final String key) {
        return getContextProvider().getI18nResources(key);
    }

    /**
     * Return the {@code I18nContext} associated with the current
     * {@code Thread}.
     * <p>
     * If no {@code I18nContext} exists for the current {@code Thread} or the
     * existing one is not alive anymore a new one is created.
     * <p>
     * Shortcut for {@code getContextProvider().getContext()}.
     * 
     * @return The current {@code I18nContext}. Never {@code null}.
     * @see I18nContextProvider#getContext()
     */
    public static @NotNull I18nContext getContext() {
        return getContextProvider().getContext();
    }

    /**
     * Returns {@code true} if the specified context is alive or should be
     * discarded.
     * <p>
     * Shortcut for {@code getContextProvider().isContextAlive(context)}.
     * 
     * @param context The I18N context
     * @return {@code true} if the specified context is alive
     * @see I18nContextProvider#isContextAlive(I18nContext)
     */
    public static boolean isContextAlive(
            final @NotNull I18nContext context) {
        return getContextProvider().isContextAlive(context);
    }

    /**
     * Clears the I18nContext for the current thread.
     * <p>
     * Shortcut for {@code getContextProvider().getLocale()}.
     * @see I18nContextProvider#clearContext()
     */
    public static void clearContext() {
        getContextProvider().clearContext();
    }

    /**
     * Returns the current {@code I18nContext} locale.
     * <p>
     * Shortcut for {@code getContext().getLocale()}.
     * 
     * @return The current {@code I18nContext} locale
     * @see I18nContext#getLocale()
     */
    public static @NotNull Locale getLocale() {
        return getContext().getLocale();
    }

    /**
     * Sets the locale of the current {@code I18nContext}.
     * <p>
     * Shortcut for {@code getContext().setLocale(locale)}.
     * 
     * @param locale The current {@code I18nContext} locale
     * @see I18nContext#setLocale(Locale)
     */
    public static void setLocale(final Locale locale) {
        getContext().setLocale(locale);
    }

    /**
     * Returns {@code true} if the current {@code I18nContext} requests the
     * full {@code I18nString} translations sets. If {@code false} only the
     * text in the current {@code I18nContext} locale must be returned.
     * <p>
     * Shortcut for {@code getContext().isFullMode()}.
     * 
     * @return If the full {@code I18nString} translations must be returned
     * @see I18nContext#isFullMode()
     */
    public static boolean isFullMode() {
        return getContext().isFullMode();
    }

    /**
     * Sets if the current {@code I18nContext} requests the full
     * {@code I18nString} translations sets. If {@code false} only the
     * text in the current {@code I18nContext} locale must be returned.
     * <p>
     * Shortcut for {@code getContext().setFullMode(value)}.
     * 
     * @return If the full {@code I18nString} translations must be returned
     * @see I18nContext#setFullMode(boolean)
     */
    public static void setFullMode(final boolean value) {
        getContext().setFullMode(value);
    }
}
