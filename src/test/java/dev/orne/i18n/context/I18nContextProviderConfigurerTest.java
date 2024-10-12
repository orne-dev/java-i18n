package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 - 2024 Orne Developments
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

import java.util.Properties;

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
     * Test {@link I18nContextProvider.Configurer#setI18nConfiguration(Properties)}.
     */
    @Test
    void testSetI18nConfiguration() {
        final Properties config = new Properties();
        new I18nContextProvider.Configurer() {{
            setI18nConfiguration(config);
        }};
        final Properties result = I18nConfiguration.get(
                Thread.currentThread().getContextClassLoader());
        assertNotSame(config, result);
        assertEquals(config, result);
        assertNotSame(
                config,
                I18nConfiguration.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
    }

    /**
     * Test {@link I18nContextProvider.Configurer#setI18nConfiguration(Properties)}.
     */
    @Test
    void testSetI18nConfiguration_ClassLoader() {
        final Properties config = new Properties();
        new I18nContextProvider.Configurer() {{
            setI18nConfiguration(Thread.currentThread().getContextClassLoader(), config);
        }};
        final Properties result = I18nConfiguration.get(
                Thread.currentThread().getContextClassLoader());
        assertNotSame(config, result);
        assertEquals(config, result);
        assertNotSame(
                config,
                I18nConfiguration.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
    }

    /**
     * Test {@link I18nContextProvider.Configurer#setI18nContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetI18nContextProvider() {
        final I18nContextProvider provider = mock(I18nContextProvider.class);
        new I18nContextProvider.Configurer() {{
            setI18nContextProvider(provider);
        }};
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
    void testSetI18nContextProvider_ClassLoader() {
        final I18nContextProvider provider = mock(I18nContextProvider.class);
        new I18nContextProvider.Configurer() {{
            setI18nContextProvider(Thread.currentThread().getContextClassLoader(), provider);
        }};
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
