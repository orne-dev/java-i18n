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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Annotation for I18N context provider configuration on Spring contexts Java
 * configurations.
 * <p>
 * Provides simple configuration of basic properties.
 * <p>
 * For more complex configurations use {@code I18nSpringConfigurer}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nSpringConfigurer
 * @since 0.1
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Import(I18nSpringConfigurer.class)
public @interface EnableI18N {

    /** @return The {@code ClassLoader} level to apply the configuration to. */
    Class<?> targetClass() default Void.class;
    /** @return The supported languages. */
    String[] availableLanguages() default {};
}
