package dev.orne.i18n.it.common;

/*-
 * #%L
 * i18n-it-common
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

/**
 * Suite with Spring Java configuration support tests.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-11
 * @since 0.1
 */
public class SpringJavaConfigurationTests {

    /**
     * Detault configuration test.
     */
    @Nested
    public class Default
    extends SpringJavaConfigurationIT {}

    /**
     * Alternative configuration test.
     */
    @Nested
    public class Alternative
    extends SpringJavaConfigurationAltIT {}
}
