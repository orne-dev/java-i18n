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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.I18nContext;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Unit tests for {@code I18N}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18N
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nTest {

    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    @AfterEach
    void resetI18N() {
        ContextTestUtils.reset();
    }

    /**
     * Test {@link I18N#getAvailableLocales()}.
     */
    @Test
    void testGetAvailableLocales() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(locales).given(mockContext).getAvailableLocales();
        final Locale[] result = I18N.getAvailableLocales();
        assertSame(locales, result);
        final InOrder order = inOrder(mockProvider, mockContext);
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).getAvailableLocales();
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getResources()}.
     */
    @Test
    void testGetDefaultI18nResources() {
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources();
        final I18nResources result = I18N.getResources();
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockProvider, mockContext);
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).getI18nResources();
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        final String key = "mock resources key";
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(key);
        final I18nResources result = I18N.getResources(key);
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockProvider, mockContext);
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).getI18nResources(key);
        then(mockContext).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18N#getLocale()}.
     */
    @Test
    void testGetLocale() {
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(MOCK_LOCALE).given(mockContext).getLocale();
        final Locale result = I18N.getLocale();
        assertSame(MOCK_LOCALE, result);
        final InOrder order = inOrder(mockProvider, mockContext);
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
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        I18N.setLocale(MOCK_LOCALE);
        final InOrder order = inOrder(mockProvider, mockContext);
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
        final NullPointerException mockEx = new NullPointerException();
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willThrow(mockEx).given(mockContext).setLocale(null);
        final NullPointerException result = assertThrows(NullPointerException.class, () -> {
            I18N.setLocale(null);
        });
        assertSame(mockEx, result);
        final InOrder order = inOrder(mockProvider, mockContext);
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should(order).setLocale(null);
        then(mockContext).shouldHaveNoMoreInteractions();
    }
}
