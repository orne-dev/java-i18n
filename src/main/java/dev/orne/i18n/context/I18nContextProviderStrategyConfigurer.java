package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
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
import java.util.ServiceLoader;

import javax.validation.constraints.NotNull;

import dev.orne.i18n.I18nConfigurationException;

/**
 * I18N context provider strategy configuration SPI.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-11
 * @see I18nContextProviderStrategy
 * @since 0.1
 */
public interface I18nContextProviderStrategyConfigurer {

    /**
     * Creates and configures a I18N context provider strategy based on the
     * default application I18N configuration.
     * 
     * @return The configured I18N context provider strategy.
     * @see I18nConfiguration#load()
     */
    public static @NotNull I18nContextProviderStrategy configure() {
        return configure(I18nConfiguration.load());
    }

    /**
     * Creates and configures a I18N context provider strategy based on the
     * specified application I18N configuration.
     * 
     * @param config The I18N configuration.
     * @return The configured I18N context provider strategy.
     */
    public static @NotNull I18nContextProviderStrategy configure(
            final @NotNull Properties config) {
        String type = config.getProperty(I18nConfiguration.STRATEGY);
        if (type == null) {
            type = DefaultI18nContextProviderStrategy.TYPE;
        }
        final ServiceLoader<I18nContextProviderStrategyConfigurer> loader =
                ServiceLoader.load(I18nContextProviderStrategyConfigurer.class);
        for (final I18nContextProviderStrategyConfigurer configurer : loader) {
            if (type.equals(configurer.getType())) {
                return configurer.create(config);
            }
        }
        throw new I18nConfigurationException("No I18N context provider strategy found for configured code: " + type);
    }

    /**
     * Returns the code of I18N context provider strategy type this instance
     * configures.
     * <p>
     * Used to locate service provider based on configuration value.
     * 
     * @return The context provider strategy type code.
     * @see I18nConfiguration#STRATEGY
     */
    @NotNull String getType();

    /**
     * Creates a configures a new I18N context provider strategy based on the
     * specified configuration.
     * 
     * @param config The I18N configuration.
     * @return The I18N context provider strategy
     * @throws I18nConfigurationException If the configuration is not valid.
     */
    @NotNull I18nContextProviderStrategy create(
            @NotNull Properties config);
}
