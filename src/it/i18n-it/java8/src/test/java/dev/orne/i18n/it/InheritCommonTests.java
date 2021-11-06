package dev.orne.i18n.it;

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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;

import dev.orne.i18n.it.common.JacksonTests;
import dev.orne.i18n.it.common.JavaxJaxbTest;
import dev.orne.i18n.it.common.JavaxValidationTest;
import dev.orne.i18n.it.common.SpringClassLoaderI18nConfigurerTest;
import dev.orne.i18n.it.common.SpringTests;

/**
 * Suite with tests from {@code dev.orne:i18n-it-common}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @since 0.1
 */
@Tag("it")
public class InheritCommonTests {

    /**
     * JAXB support tests.
     */
    @Nested
    public class Jaxb
    extends JavaxJaxbTest {}

    /**
     * Validation support tests.
     */
    @Nested
    public class Validation
    extends JavaxValidationTest {}

    /**
     * Jackson support tests.
     */
    @Nested
    public class Jackson
    extends JacksonTests {}

    /**
     * Spring support tests.
     */
    @Nested
    public class Spring
    extends SpringTests {}

    /**
     * Integrity test for {@code I18nSpringConfigurer.targetClass}.
     */
    @Nested
    public class SpringTargetClassIT
    extends SpringClassLoaderI18nConfigurerTest {}
}
