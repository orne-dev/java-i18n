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
import static org.mockito.BDDMockito.*;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import dev.orne.i18n.DummyI18nResources;
import dev.orne.i18n.I18nContext;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringContextProvider
 */
@Tag("ut")
class I18nSpringContextProviderTest {

    private @Mock MessageSource source;
    private @Mock I18nContext mockContext;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider();
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isFullModeByDefault());
        assertTrue(provider.isInheritable());
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource)}.
     */
    @Test
    void testConstructor_MessageSource() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider(source);
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(source, ((I18nSpringResources) provider.getDefaultI18nResources()).getSource());
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isFullModeByDefault());
        assertTrue(provider.isInheritable());
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource)}.
     */
    @Test
    void testConstructor_MessageSource_Null() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider((MessageSource) null);
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isFullModeByDefault());
        assertTrue(provider.isInheritable());
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(boolean)}.
     */
    @Test
    void testConstructor_Inheritable() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider(false);
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isFullModeByDefault());
        assertFalse(provider.isInheritable());
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource, boolean)}.
     */
    @Test
    void testConstructor_MessageSource_Inheritable() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider(source, false);
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(source, ((I18nSpringResources) provider.getDefaultI18nResources()).getSource());
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isFullModeByDefault());
        assertFalse(provider.isInheritable());
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource, boolean)}.
     */
    @Test
    void testConstructor_MessageSource_Inheritable_Null() {
        assertThrows(NullPointerException.class, () -> {
            new I18nSpringContextProvider(null, false);
        });
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext()}.
     */
    @Test
    void testCreateContext() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider();
        final I18nContext result = provider.createContext();
        assertTrue(result instanceof I18nSpringContext);
        assertFalse(result.isFullMode());
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext()}.
     */
    @Test
    void testCreateContext_FullMode() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider();
        provider.setFullModeByDefault(true);
        final I18nContext result = provider.createContext();
        assertTrue(result instanceof I18nSpringContext);
        assertTrue(result.isFullMode());
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider();
        willReturn(true).given(mockContext).isFullMode();
        final I18nContext result = provider.createContext(mockContext);
        assertTrue(result instanceof I18nSpringContext);
        assertTrue(result.isFullMode());
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent_Null() {
        final I18nSpringContextProvider provider = new I18nSpringContextProvider(false);
        assertThrows(NullPointerException.class, () -> {
            provider.createContext(null);
        });
    }
}
