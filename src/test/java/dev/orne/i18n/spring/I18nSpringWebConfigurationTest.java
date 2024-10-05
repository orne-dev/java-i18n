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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code I18nSpringWebConfiguration}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see I18nSpringWebConfiguration
 */
@Tag("ut")
class I18nSpringWebConfigurationTest extends I18nSpringConfigurationTest {

    /**
     * Test {@link I18nSpringWebConfiguration#orneBeansI18nContextClearer()}.
     */
    @Test
    void testI18NContextClearer() {
        final I18nSpringWebConfiguration configurer = new I18nSpringWebConfiguration();
        final I18nSpringWebContextClearer result = configurer.orneBeansI18nContextClearer();
        assertNotNull(result);
    }
}
