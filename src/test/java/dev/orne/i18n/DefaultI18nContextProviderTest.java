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
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code DefaultI18nContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see DefaultI18nContextProvider
 */
@Tag("ut")
class DefaultI18nContextProviderTest {

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
     * Test {@link DefaultI18nContextProvider#DefaultI18nContextProvider(boolean)}.
     */
    @Test
    void testConstructor() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isInheritable());
    }

    /**
     * Test {@link DefaultI18nContextProvider#DefaultI18nContextProvider(boolean)}.
     */
    @Test
    void testConstructor_Inheritable() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(true);
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
    }

    /**
     * Test {@link DefaultI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext() {
        final DefaultI18nContextProvider provider = spy(new DefaultI18nContextProvider(false));
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final I18nContext result = provider.getContext();
        assertSame(mockContext, result);
        assertSame(mockContext, provider.getContexts().get());
        then(provider).should(never()).createContext();
    }

    /**
     * Test {@link DefaultI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Missing() {
        final DefaultI18nContextProvider provider = spy(new DefaultI18nContextProvider(false));
        willReturn(mockContext).given(provider).createContext();
        final I18nContext result = provider.getContext();
        assertSame(mockContext, result);
        assertSame(mockContext, provider.getContexts().get());
        then(provider).should().createContext();
    }

    /**
     * Test {@link DefaultI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Inheritable()
    throws InterruptedException {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(true);
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final InheritableGetContextChild childTest =
                new InheritableGetContextChild(provider);
        final Thread child = new Thread(childTest);
        child.start();
        child.join();
        assertSame(mockContext, childTest.context);
    }

    /**
     * Test {@link DefaultI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Invalid() {
        final DefaultI18nContextProvider provider = spy(new DefaultI18nContextProvider(false));
        provider.getContexts().set(mockContext);
        willReturn(false).given(provider).isContextAlive(mockContext);
        final I18nContext result = provider.getContext();
        assertNotSame(mockContext, result);
        assertSame(result, provider.getContexts().get());
        then(provider).should().createContext();
    }

    /**
     * Test {@link DefaultI18nContextProvider#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
        assertFalse(provider.isContextAlive(mockContext));
    }

    /**
     * Test {@link DefaultI18nContextProvider#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive_Same() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        assertTrue(provider.isContextAlive(mockContext));
    }

    /**
     * Test {@link DefaultI18nContextProvider#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive_WrongUUID() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
        willReturn(UUID.randomUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        assertFalse(provider.isContextAlive(mockContext));
    }

    /**
     * Test {@link DefaultI18nContextProvider#isContextAlive(I18nContext)}.
     */
    @Test
    void testIsContextAlive_Null() {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
        assertThrows(NullPointerException.class, () -> {
            provider.isContextAlive(null);
        });
    }

    /**
     * Test {@link DefaultI18nContextProvider#clearContext()}.
     */
    @Test
    void testClearContext() {
        final DefaultI18nContextProvider provider = spy(new DefaultI18nContextProvider(false));
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final I18nContext tmp = provider.getContext();
        assertSame(mockContext, tmp);
        assertSame(mockContext, provider.getContexts().get());
        provider.clearContext();
        assertNull(provider.getContexts().get());
    }

    /**
     * Test {@link DefaultI18nContextProvider#clearContext()}.
     */
    @Test
    void testClearContext_Inheritable()
    throws InterruptedException {
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(true);
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final InheritableGetContextChild preTest =
                new InheritableGetContextChild(provider);
        final Thread preChild = new Thread(preTest);
        preChild.start();
        preChild.join();
        assertSame(mockContext, preTest.storedContext);
        provider.clearContext();
        final InheritableGetContextChild childTest =
                new InheritableGetContextChild(provider);
        final Thread child = new Thread(childTest);
        child.start();
        child.join();
        assertNull(childTest.storedContext);
    }

    /**
     * Test {@link DefaultI18nContextProvider#invalidate()}.
     */
    @Test
    void testInvalidate() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(false);
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
        assertFalse(provider.isInheritable());
        assertFalse(provider.isContextAlive(context));
        provider.clearContext();
    }

    /**
     * Test {@link DefaultI18nContextProvider#invalidate()}.
     */
    @Test
    void testInvalidate_Inheritable() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(true);
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
        assertTrue(provider.isInheritable());
        assertFalse(provider.isContextAlive(context));
        provider.clearContext();
    }

    /**
     * Test {@link DefaultI18nContextProvider#equals(Object)} and
     * {@link DefaultI18nContextProvider#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider(true);
        assertNotEquals(provider, (Object) null);
        assertEquals(provider, provider);
        assertEquals(provider.hashCode(), provider.hashCode());
        assertNotEquals(provider, new Object());
        assertNotEquals(provider, new DefaultI18nContextProvider(false));
        final DefaultI18nContextProvider other = new DefaultI18nContextProvider(true);
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

    class InheritableGetContextChild
    implements Runnable {

        private final DefaultI18nContextProvider provider;
        private I18nContext storedContext;
        private I18nContext context;

        public InheritableGetContextChild(
                final DefaultI18nContextProvider provider) {
            super();
            this.provider = provider;
        }

        @Override
        public void run() {
            storedContext = provider.getContexts().get();
            context = provider.getContext();
        }
    }
}
