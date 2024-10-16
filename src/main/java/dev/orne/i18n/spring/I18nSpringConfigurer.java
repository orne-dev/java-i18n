package dev.orne.i18n.spring;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2024 Orne Developments
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

import dev.orne.i18n.context.I18nContextProvider;

/**
 * Interface to be implemented by {@literal @}{@link org.springframework.context.annotation.Configuration}
 * classes annotated with {@literal @}{@link EnableI18N} that wish to customize the
 * {@link I18nContextProvider} instance used by the application
 * <p>
 * See {@literal @}{@link EnableI18N} for usage examples.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @see EnableI18N
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nSpringConfigurer {

    /**
     * Returns the  I18N context provider builder to use to configure
     * I18N context provider.
     * 
     * @return The I18N context provider builder.
     */
    default @NotNull I18nSpringContextProvider.Builder getI18nContextProviderBuilder() {
        return I18nSpringContextProvider.builder();
    }

    /**
     * Configure the I18N context provider configuration.
     * 
     * @param builder The I18N context provider builder.
     */
    default void configureI18nContextProvider(
            final @NotNull I18nSpringContextProvider.Builder builder) {
        builder.configure();
    }
}
