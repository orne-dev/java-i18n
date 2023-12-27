package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code SharedI18nContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see SharedI18nContextProvider
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class SharedI18nContextProviderTest {

    private @Mock I18nResources mockDefaultResources;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    /**
     * Test {@link SharedI18nContextProvider#SharedI18nContextProvider()}.
     */
    @Test
    void testConstructor() {
        final SharedI18nContextProvider provider = new SharedI18nContextProvider();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link SharedI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext() {
        final SharedI18nContextProvider provider = spy(new SharedI18nContextProvider());
        willReturn(mockContext).given(provider).createContext();
        final I18nContext result = provider.getContext();
        assertSame(mockContext, result);
        then(provider).should().createContext();
        assertSame(mockContext, provider.getContext());
        then(provider).should().createContext();
    }

    /**
     * Test {@link SharedI18nContextProvider#isContextValid(I18nContext)}.
     */
    @Test
    void testIsContextAlive() {
        final I18nContext otherContext = mock(I18nContext.class);
        final SharedI18nContextProvider provider = spy(new SharedI18nContextProvider());
        willReturn(mockContext).given(provider).createContext();
        assertFalse(provider.isContextValid(mockContext));
        assertFalse(provider.isContextValid(otherContext));
        I18nContext context = provider.getContext();
        assertSame(mockContext, context);
        assertTrue(provider.isContextValid(mockContext));
        assertFalse(provider.isContextValid(otherContext));
    }

    /**
     * Test {@link SharedI18nContextProvider#clearContext()}.
     */
    @Test
    void testClearContext() {
        final I18nContext otherContext = mock(I18nContext.class);
        final SharedI18nContextProvider provider = spy(new SharedI18nContextProvider());
        willReturn(mockContext, otherContext).given(provider).createContext();
        I18nContext context = provider.getContext();
        assertSame(mockContext, context);
        assertTrue(provider.isContextValid(mockContext));
        assertFalse(provider.isContextValid(otherContext));
        provider.clearContext();
        assertFalse(provider.isContextValid(mockContext));
        assertFalse(provider.isContextValid(otherContext));
        context = provider.getContext();
        assertSame(otherContext, context);
        assertFalse(provider.isContextValid(mockContext));
        assertTrue(provider.isContextValid(otherContext));
    }

    /**
     * Test {@link SharedI18nContextProvider#equals(Object)} and
     * {@link SharedI18nContextProvider#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final SharedI18nContextProvider provider = new SharedI18nContextProvider();
        assertNotEquals(provider, (Object) null);
        assertEquals(provider, provider);
        assertEquals(provider.hashCode(), provider.hashCode());
        assertNotEquals(provider, new Object());
        final SharedI18nContextProvider other = new SharedI18nContextProvider();
        assertNotEquals(provider.getSessionUUID(), other.getSessionUUID());
        assertEquals(provider, other);
        other.setAvailableLocales(locales);
        assertNotEquals(provider, other);
        provider.setAvailableLocales(locales);
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider.setDefaultI18nResources(mockDefaultResources);
        assertNotEquals(provider, other);
        other.setDefaultI18nResources(mockDefaultResources);
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider.addI18nResources("mock key", mockResources);
        assertNotEquals(provider, other);
        other.addI18nResources("other mock key", mockResources);
        assertNotEquals(provider, other);
        other.clearI18nResources();
        other.addI18nResources("mock key", mockResources);
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider.getContext();
        assertNotEquals(provider, other);
        other.getContext();
        assertNotEquals(provider, other);
    }
}
