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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nConfigurationException;

/**
 * SPI based I18N context provider factory.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2024-01
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.EXPERIMENTAL, since="0.1")
public interface I18nContextProviderFactory {

    /**
     * Returns the code of I18N context provider strategy type this instance
     * creates.
     * <p>
     * Used to locate service provider based on configuration value.
     * 
     * @return The context provider strategy type code.
     * @see I18nConfiguration#CONTEXT_PROVIDER
     */
    @NotNull String getType();

    /**
     * Creates and configures a new I18N context provider based on the
     * specified configuration.
     * 
     * @param config The I18N configuration.
     * @return The I18N context provider.
     * @throws I18nConfigurationException If the configuration is not valid.
     */
    @NotNull I18nContextProvider create(
            @NotNull Properties config);
}
