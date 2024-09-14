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
import java.util.UUID;

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
        final TestImpl provider = new TestBuilder().build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link AbstractI18nContextProvider.Factory}.
     */
    @Test
    void testFactory() {
        final Properties config = new Properties();
        TestImpl provider = new TestBuilder().build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.AVAILABLE_LANGUAGES, "en,fr");
        provider = new TestBuilder()
                .configure(config)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(new Locale[] { Locale.ENGLISH, Locale.FRENCH }, provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.DEFAULT_RESOURCES, "dev.orne.i18n.test-messages");
        provider = new TestBuilder()
                .configure(config)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        I18nBundleResources bundle = assertInstanceOf(I18nBundleResources.class, provider.getDefaultI18nResources());
        assertEquals("dev.orne.i18n.test-messages", bundle.getBundle().getBaseBundleName());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.DEFAULT_RESOURCES, "dev.orne.i18n.missing-messages");
        provider = new TestBuilder()
                .configure(config)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt", "dev.orne.i18n.test-messages-alt");
        provider = new TestBuilder()
                .configure(config)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        bundle = assertInstanceOf(I18nBundleResources.class, provider.getI18nResources("alt"));
        assertEquals("dev.orne.i18n.test-messages-alt", bundle.getBundle().getBaseBundleName());
        assertEquals(1, provider.getI18nResources().size());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt", "dev.orne.i18n.missing-messages");
        provider = new TestBuilder()
                .configure(config)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertInstanceOf(DummyI18nResources.class, provider.getDefaultI18nResources());
        assertSame(provider.getDefaultI18nResources(), provider.getI18nResources("alt"));
        assertEquals(1, provider.getI18nResources().size());
        config.clear();
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt1", "dev.orne.i18n.test-messages");
        config.setProperty(I18nConfiguration.NAMED_RESOURCES_PREFIX + "alt2", "dev.orne.i18n.test-messages-alt");
        provider = new TestBuilder()
                .configure(config)
                .build();
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
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setAvailableLocales(locales)
                .build());
        assertArrayEquals(locales, provider.getAvailableLocales());
        assertThrows(NullPointerException.class, () -> {
            new TestBuilder()
                    .setAvailableLocales(null);
        });
    }

    /**
     * Test {@link AbstractI18nContextProvider#setDefaultI18nResources(I18nResources)}.
     */
    @Test
    void testSetDefaultI18nResources() {
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setDefaultI18nResources(mockResources)
                .build());
        assertSame(mockResources, provider.getDefaultI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        assertThrows(NullPointerException.class, () -> {
            new TestBuilder()
            .setDefaultI18nResources(null);
        });
    }

    /**
     * Test {@link AbstractI18nContextProvider#addI18nResources(String, I18nResources)}.
     */
    @Test
    void testAddI18nResources() {
        final String key = "mock key";
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build());
        assertSame(mockDefaultResources, provider.getDefaultI18nResources());
        assertEquals(1, provider.getI18nResources().size());
        assertEquals(mockResources, provider.getI18nResources().get(key));
        assertThrows(NullPointerException.class, () -> {
            new TestBuilder()
                    .addI18nResources(null, mockResources);
        });
        assertThrows(NullPointerException.class, () -> {
            new TestBuilder()
                    .addI18nResources(key, null);
        });
        assertThrows(NullPointerException.class, () -> {
            new TestBuilder()
            .addI18nResources(null, null);
        });
    }

    /**
     * Test {@link AbstractI18nContextProvider#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        final String key = "mock key";
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build());
        final I18nResources result = provider.getI18nResources(key);
        assertSame(mockResources, result);
    }

    /**
     * Test {@link AbstractI18nContextProvider#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources_Missing() {
        final String key = "mock key";
        AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .build());
        I18nResources result = provider.getI18nResources(key);
        assertSame(mockDefaultResources, result);
        provider = spy(new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build());
        result = provider.getI18nResources(null);
        assertSame(mockDefaultResources, result);
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext()}.
     */
    @Test
    void testCreateContext() {
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .build());
        final I18nContext result = provider.createContext();
        assertTrue(result instanceof DefaultI18nContext);
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent() {
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .build());
        willReturn(MOCK_LOCALE).given(mockContext).getLocale();
        final I18nContext result = provider.createContext(mockContext);
        assertTrue(result instanceof DefaultI18nContext);
    }

    /**
     * Test {@link AbstractI18nContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent_Null() {
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .build());
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
        final String key = "mock key";
        final AbstractI18nContextProvider provider = spy(new TestBuilder()
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build());
        final UUID prevUUID = provider.getSessionUUID();
        final I18nContext context = provider.getContext();
        provider.invalidate();
        assertNotNull(provider.getSessionUUID());
        assertNotEquals(prevUUID, provider.getSessionUUID());
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
        AbstractI18nContextProvider provider = new TestBuilder()
                .build();
        assertFalse(provider.equals(null));
        assertEquals(provider, provider);
        assertEquals(provider.hashCode(), provider.hashCode());
        assertNotEquals(provider, new Object());
        AbstractI18nContextProvider other = new TestBuilder()
                .build();
        assertNotEquals(provider.getSessionUUID(), other.getSessionUUID());
        assertEquals(provider, other);
        other = new TestBuilder()
                .setAvailableLocales(locales)
                .build();
        assertNotEquals(provider, other);
        provider = new TestBuilder()
                .setAvailableLocales(locales)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider = new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .build();
        assertNotEquals(provider, other);
        other = new TestBuilder()
                .setDefaultI18nResources(mockDefaultResources)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider = new TestBuilder()
                .addI18nResources("mock key", mockResources)
                .build();
        assertNotEquals(provider, other);
        other = new TestBuilder()
                .addI18nResources("other mock key", mockResources)
                .build();
        assertNotEquals(provider, other);
        other = new TestBuilder()
                .addI18nResources("mock key", mockResources)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
    }

    private static class TestImpl
    extends AbstractI18nContextProvider {
        public TestImpl(
                final @NotNull AbstractI18nContextProvider.BuilderImpl<?, ?> builder) {
            super(builder);
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
    private static class TestBuilder
    extends AbstractI18nContextProvider.BuilderImpl<TestImpl, TestBuilder>
    implements AbstractI18nContextProvider.Builder {
        public TestBuilder() {
            super();
        }
        @Override
        public @NotNull TestImpl build() {
            return new TestImpl(this);
        }
    }
}
