package dev.orne.i18n.spring;

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

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Selector of Spring configuration based on presence of {@code spring-web}
 * on the classpath.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-05
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Configuration
public class I18nSpringConfigurationSelector
implements ImportSelector {

    /** Class to use detect {@code spring-web}. */
    public static final String SPRING_WEB_DETECTOR = "org.springframework.web.context.support.RequestHandledEvent";
    /** Class of {@code spring-web} based configuration. */
    private static final String WEB_CONFIGURATION = "dev.orne.i18n.spring.I18nSpringWebConfiguration";
    /** Class of default configuration. */
    private static final String BASE_CONFIGURATION = "dev.orne.i18n.spring.I18nSpringConfiguration";

    /**
     * Creates a new instance.
     */
    public I18nSpringConfigurationSelector() {
        super();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Loads {@code I18nSpringWebContextCleaner} if {@code spring-web} is on
     * the classpth. {@code I18nSpringBaseConfigurer} otherwise.
     * .
     */
    @Override
    public String[] selectImports(
            final AnnotationMetadata importingClassMetadata) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(SPRING_WEB_DETECTOR);
            return new String[] { WEB_CONFIGURATION };
        } catch (ClassNotFoundException e) {
            return new String[] { BASE_CONFIGURATION };
        }
    }
}
