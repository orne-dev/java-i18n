package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023-2024 Orne Developments
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
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code I18nContextProvider.Configurer}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2024-09
 * @since 0.1
 * @see I18nContextProvider.Configurer
 */
@Tag("ut")
class I18nContextProviderConfigurerTest {

    @BeforeAll
    public static void resetPreviousConfiguration() {
        ContextTestUtils.reset();
    }

    @AfterEach
    public void resetI18N() {
        ContextTestUtils.reset();
    }

    /**
     * Test {@link I18nContextProvider.Configurer#set(I18nContextProvider)}.
     */
    @Test
    void testSet() {
        final I18nContextProvider provider = mock(I18nContextProvider.class);
        I18nContextProvider.Registry.set(provider);
        assertSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader()));
        assertNotSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
    }

    /**
     * Test {@link I18nContextProvider.Configurer#setI18nContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetClassLoader() {
        final I18nContextProvider provider = mock(I18nContextProvider.class);
        I18nContextProvider.Registry.set(
                Thread.currentThread().getContextClassLoader(),
                provider);
        assertSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader()));
        assertNotSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
    }
}
