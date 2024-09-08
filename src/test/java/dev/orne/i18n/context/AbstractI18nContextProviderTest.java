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
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code AbstractI18nContextProvider}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see AbstractI18nContextProvider
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class AbstractI18nContextProviderTest {

    private static final Locale MOCK_LOCALE = new Locale("xx");

    private @Mock I18nResources mockDefaultResources;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    /**
     * Test {@link AbstractI18nContextProvider#AbstractI18nContextProvider()}.
     */
    @Test
    void testConstructor() {
        final TestImpl provider = new TestImpl();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider#AbstractI18nContextProvider(Properties)}.
     */
    @Test
    void testConfigConstructor() {
        final Properties config = new Properties();
        TestImpl provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.AVAILABLE_LANGUAGES, "en,fr");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(new Locale[] { Locale.ENGLISH, Locale.FRENCH }, provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.DEFAULT_RESOURCES, "dev.orne.i18n.test-messages");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        I18nBundleResources bundle = assertInstanceOf(I18nBundleResources.class, provider.getDefaultI18nResources());
        assertEquals("dev.orne.i18n.test-messages", bundle.getBundle().getBaseBundleName());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.DEFAULT_RESOURCES, "dev.orne.i18n.missing-messages");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt", "dev.orne.i18n.test-messages-alt");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        bundle = assertInstanceOf(I18nBundleResources.class, provider.getI18nResources("alt"));
        assertEquals("dev.orne.i18n.test-messages-alt", bundle.getBundle().getBaseBundleName());
        assertEquals(1, provider.getI18nResources().size());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt", "dev.orne.i18n.missing-messages");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertSame(provider.getDefaultI18nResources(), provider.getI18nResources("alt"));
        assertEquals(1, provider.getI18nResources().size());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt1", "dev.orne.i18n.test-messages");
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt2", "dev.orne.i18n.test-messages-alt");
        provider = new TestImpl(config);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        bundle = assertInstanceOf(I18nBundleResources.class, provider.getI18nResources("alt1"));
        assertEquals("dev.orne.i18n.test-messages", bundle.getBundle().getBaseBundleName());
        bundle = assertInstanceOf(I18nBundleResources.class, provider.getI18nResources("alt2"));
        assertEquals("dev.orne.i18n.test-messages-alt", bundle.getBundle().getBaseBundleName());
        assertEquals(2, provider.getI18nResources().size());
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
        public TestImpl() {
            super();
        }
        public TestImpl(@NotNull Properties config) {
            super(config);
        }
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
