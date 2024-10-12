package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2024 Orne Developments
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

import java.util.Properties;

import javax.validation.constraints.NotNull;

/**
 * Utility class for I18N context tests.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2024-08
 * @since 0.1
 */
public final class ContextTestUtils {

    /**
     * Private constructor.
     */
    private ContextTestUtils() {
        // Utility class
    }

    /**
     * Resets the I18N configuration and context provider registry.
     */
    public static void reset() {
        I18nConfiguration.reset();
        I18nContextProvider.Registry.reset();
    }

    /**
     * Sets the I18N configuration for current thread context class loader.
     * 
     * @param config The I18N configuration.
     */
    public static void setConfiguration(
            final @NotNull Properties config) {
        setConfiguration(Thread.currentThread().getContextClassLoader(), config);
    }

    /**
     * Sets the I18N configuration for the specified class loader.
     * 
     * @param cl The class loader.
     * @param config The I18N configuration.
     */
    public static void setConfiguration(
            final @NotNull ClassLoader cl,
            final @NotNull Properties config) {
        I18nConfiguration.set(cl, config);
    }

    /**
     * Sets the I18N context provider for current thread context class loader.
     * 
     * @param provider The I18N context provider.
     */
    public static void setProvider(
            final @NotNull I18nContextProvider provider) {
        setProvider(Thread.currentThread().getContextClassLoader(), provider);
    }

    /**
     * Sets the I18N context provider for the specified class loader.
     * 
     * @param cl The class loader.
     * @param provider The I18N context provider.
     */
    public static void setProvider(
            final @NotNull ClassLoader cl,
            final @NotNull I18nContextProvider provider) {
        I18nContextProvider.Registry.set(cl, provider);
    }
}
