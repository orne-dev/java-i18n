package dev.orne.i18n;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code I18N}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18N
 */
@Tag("ut")
class I18nTest {

    private static I18nContextProviderStrategy preTestsStrategy;

    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);
    private @Mock I18nContextProviderStrategy mockStrategy;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    @BeforeAll
    static void saveDefaultStrategy() {
        preTestsStrategy = I18N.getContextProviderStrategy();
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void restoreDefaultStrategy() {
        I18N.setContextProviderStrategy(preTestsStrategy);
    }

    /**
     * Test {@link I18N#getContextProviderStrategy()}.
     */
    @Test
    void testInitialContextProviderStrategy() {
        final I18nContextProviderStrategy defaultStrategy = I18N.getContextProviderStrategy();
        assertNotNull(defaultStrategy);
        assertTrue(defaultStrategy instanceof DefaultI18nContextProviderStrategy);
        final DefaultI18nContextProviderStrategy byClStrategy =
                (DefaultI18nContextProviderStrategy) defaultStrategy;
        final I18nContextProvider defaultProvider = byClStrategy.getDefaultContextProvider();
        assertNotNull(defaultProvider);
        assertTrue(defaultProvider instanceof DefaultI18nContextProvider);
    }

    /**
     * Test {@link I18N#setContextProviderStrategy(I18nContextProviderStrategy)}.
     */
    @Test
    void testSetContextProviderStrategy() {
        I18N.setContextProviderStrategy(mockStrategy);
        assertSame(mockStrategy, I18N.getContextProviderStrategy());
    }

    /**
     * Test {@link I18N#setContextProviderStrategy(I18nContextProviderStrategy)}.
     */
    @Test
    void testSetContextProviderStrategy_Null() {
        assertThrows(NullPointerException.class, () -> {
            I18N.setContextProviderStrategy(null);
        });
    }

    /**
     * Test {@link I18N#getContextProvider()}.
     */
    @Test
    void testGetContextProvider() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        final I18nContextProvider result = I18N.getContextProvider();
        assertSame(mockProvider, result);
        then(mockStrategy).should().getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18N#getAvailableLocales()}.
     */
    @Test
    void testGetAvailableLocales() {
        I18N.setContextProviderStrategy(mockStrategy);
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(locales).given(mockProvider).getAvailableLocales();
        final Locale[] result = I18N.getAvailableLocales();
        assertSame(locales, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getAvailableLocales();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getDefaultI18nResources()}.
     */
    @Test
    void testGetDefaultI18nResources() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockResources).given(mockProvider).getDefaultI18nResources();
        final I18nResources result = I18N.getDefaultI18nResources();
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getDefaultI18nResources();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        I18N.setContextProviderStrategy(mockStrategy);
        final String key = "mock resources key";
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockResources).given(mockProvider).getI18nResources(key);
        final I18nResources result = I18N.getI18nResources(key);
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getI18nResources(key);
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getContext()}.
     */
    @Test
    void testGetContext() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        final I18nContext result = I18N.getContext();
        assertSame(mockContext, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(true).given(mockProvider).isContextAlive(mockContext);
        final boolean result = I18N.isContextAlive(mockContext);
        assertTrue(result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).isContextAlive(mockContext);
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive_Null() {
        I18N.setContextProviderStrategy(mockStrategy);
        final NullPointerException mockEx = new NullPointerException();
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willThrow(mockEx).given(mockProvider).isContextAlive(null);
        final NullPointerException result = assertThrows(NullPointerException.class, () -> {
            I18N.isContextAlive(null);
        });
        assertSame(mockEx, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).isContextAlive(null);
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#clearContext()}.
     */
    @Test
    void testClearContext() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        I18N.clearContext();
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).clearContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getLocale()}.
     */
    @Test
    void testGetLocale() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(MOCK_LOCALE).given(mockContext).getLocale();
        final Locale result = I18N.getLocale();
        assertSame(MOCK_LOCALE, result);
        final InOrder order = inOrder(mockStrategy, mockProvider, mockContext);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).getLocale();
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#setLocale(Locale)}.
     */
    @Test
    void testSetLocale() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        I18N.setLocale(MOCK_LOCALE);
        final InOrder order = inOrder(mockStrategy, mockProvider, mockContext);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).setLocale(MOCK_LOCALE);
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#setLocale(Locale)}.
     */
    @Test
    void testSetLocale_Null() {
        I18N.setContextProviderStrategy(mockStrategy);
        final NullPointerException mockEx = new NullPointerException();
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        willThrow(mockEx).given(mockContext).setLocale(null);
        final NullPointerException result = assertThrows(NullPointerException.class, () -> {
            I18N.setLocale(null);
        });
        assertSame(mockEx, result);
        final InOrder order = inOrder(mockStrategy, mockProvider, mockContext);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).setLocale(null);
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#isFullMode()}.
     */
    @Test
    void testIsFullMode() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(true).given(mockContext).isFullMode();
        final boolean result = I18N.isFullMode();
        assertTrue(result);
        final InOrder order = inOrder(mockStrategy, mockProvider, mockContext);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).isFullMode();
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#isFullMode()}.
     */
    @Test
    void testSetFullMode() {
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        I18N.setFullMode(true);
        final InOrder order = inOrder(mockStrategy, mockProvider, mockContext);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).setFullMode(true);
        then(mockContext).shouldHaveNoMoreInteractions();
    }
}
