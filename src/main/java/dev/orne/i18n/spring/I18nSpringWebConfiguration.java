package dev.orne.i18n.spring;

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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * I18N context provider configuration for Spring Web.
 * Configures a listener for clearing I18N context after handling the HTTP
 * requests.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-05
 * @see I18nSpringConfiguration
 * @see I18nSpringWebContextClearer
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Configuration
public class I18nSpringWebConfiguration extends I18nSpringConfiguration {

    /** The I18N context clearer  */
    public static final String CLEARER_BEAN_NAME =
            "i18nSpringWebContextCleaner";

    /**
     * Creates a new instance.
     */
    public I18nSpringWebConfiguration() {
        super();
    }

    /**
     * Creates the Spring listener for clearing the I18N context after
     * handling the HTTP requests.
     * 
     * @return The Spring listener.
     */
    @Bean(name=CLEARER_BEAN_NAME)
    public I18nSpringWebContextClearer orneBeansI18nContextClearer() {
        return new I18nSpringWebContextClearer();
    }
}
