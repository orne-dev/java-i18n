package dev.orne.i18n.context;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.i18n.I18nResources;
import jakarta.validation.constraints.NotNull;

/**
 * Unit tests for {@code AbstractI18nContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see AbstractI18nContextProvider
 */
@Tag("ut")
class AbstractI18nContextProviderTest {

    private static final Locale MOCK_LOCALE = new Locale("xx");

    private @Mock I18nResources mockDefaultResources;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;
    protected AutoCloseable mocks;

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    /**
     * Test {@link AbstractI18nContextProvider#AbstractI18nContextProvider}.
     */
    @Test
    void testConstructor() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#setAvailableLocales(Locale[])}.
     */
    @Test
    void testSetAvailableLocales() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        provider.setAvailableLocales(locales);
        assertArrayEquals(locales, provider.getAvailableLocales());
    }

    /**
     * Test {@link AbstractI18nContextProvider#setAvailableLocales(Locale[])}.
     */
    @Test
    void testSetAvailableLocales_Null() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        assertThrows(NullPointerException.class, () -> {
            provider.setAvailableLocales(null);
        });
    }

    /**
     * Test {@link AbstractI18nContextProvider#setDefaultI18nResources(I18nResources)}.
     */
    @Test
    void testSetDefaultI18nResources() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        provider.setDefaultI18nResources(mockResources);
        assertSame(mockResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#setDefaultI18nResources(I18nResources)}.
     */
    @Test
    void testSetDefaultI18nResources_Null() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        assertThrows(NullPointerException.class, () -> {
            provider.setDefaultI18nResources(null);
        });
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#addI18nResources(String, I18nResources)}.
     */
    @Test
    void testAddI18nResources() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        provider.addI18nResources(key, mockResources);
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertEquals(1, provider.getI18nResources().size());
        assertEquals(mockResources, provider.getI18nResources().get(key));
    }

    /**
     * Test {@link AbstractI18nContextProvider#addI18nResources(String, I18nResources)}.
     */
    @Test
    void testAddI18nResources_NullKey() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        provider.setDefaultI18nResources(mockDefaultResources);
        assertThrows(NullPointerException.class, () -> {
            provider.addI18nResources(null, mockResources);
        });
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#addI18nResources(String, I18nResources)}.
     */
    @Test
    void testAddI18nResources_NullResources() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        assertThrows(NullPointerException.class, () -> {
            provider.addI18nResources(key, null);
        });
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#addI18nResources(String, I18nResources)}.
     */
    @Test
    void testAddI18nResources_Nulls() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        provider.setDefaultI18nResources(mockDefaultResources);
        assertThrows(NullPointerException.class, () -> {
            provider.addI18nResources(null, null);
        });
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        provider.addI18nResources(key, mockResources);
        final I18nResources result = provider.getI18nResources(key);
        assertSame(mockResources, result);
    }

    /**
     * Test {@link AbstractI18nContextProvider#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources_Missing() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        final I18nResources result = provider.getI18nResources(key);
        assertSame(mockDefaultResources, result);
    }

    /**
     * Test {@link AbstractI18nContextProvider#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources_Null() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        provider.addI18nResources(key, mockResources);
        final I18nResources result = provider.getI18nResources(null);
        assertSame(mockDefaultResources, result);
    }

    /**
     * Test {@link AbstractI18nContextProvider#clearI18nResources()}.
     */
    @Test
    void testClearI18nResources() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        provider.addI18nResources(key, mockResources);
        provider.clearI18nResources();
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext()}.
     */
    @Test
    void testCreateContext() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        final I18nContext result = provider.createContext();
        assertTrue(result instanceof DefaultI18nContext);
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        willReturn(MOCK_LOCALE).given(mockContext).getLocale();
        final I18nContext result = provider.createContext(mockContext);
        assertTrue(result instanceof DefaultI18nContext);
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent_Null() {
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        assertThrows(NullPointerException.class, () -> {
            provider.createContext(null);
        });
    }

    /**
     * Test {@link AbstractI18nContextProvider#invalidate()}.
     */
    @Test
    void testInvalidate() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final AbstractI18nContextProvider provider = spy(AbstractI18nContextProvider.class);
        provider.setAvailableLocales(locales);
        final String key = "mock key";
        provider.setDefaultI18nResources(mockDefaultResources);
        provider.addI18nResources(key, mockResources);
        final I18nContext context = provider.getContext();
        provider.invalidate();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isContextValid(context));
        provider.clearContext();
    }

    /**
     * Test {@link AbstractI18nContextProvider#equals(Object)} and
     * {@link AbstractI18nContextProvider#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final AbstractI18nContextProvider provider = new TestImpl();
        assertFalse(provider.equals(null));
        assertEquals(provider, provider);
        assertEquals(provider.hashCode(), provider.hashCode());
        assertNotEquals(provider, new Object());
        assertNotEquals(provider, spy(AbstractI18nContextProvider.class));
        final AbstractI18nContextProvider other = new TestImpl();
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
    }

    private static class TestImpl
    extends AbstractI18nContextProvider {
        @Override
        public @NotNull I18nContext getContext() {
            return null;
        }
        @Override
        public boolean isContextValid(@NotNull I18nContext context) {
            return false;
        }
        @Override
        public void clearContext() {
            // NOP
        }
    }
}
