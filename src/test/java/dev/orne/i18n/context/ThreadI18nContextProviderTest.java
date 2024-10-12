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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code ThreadI18nContextProvider}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see ThreadI18nContextProvider
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class ThreadI18nContextProviderTest {

    private @Mock I18nResources mockDefaultResources;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    /**
     * Test {@link ThreadI18nContextProvider#ThreadI18nContextProvider()}.
     */
    @Test
    void testConstructor() {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
    }

    /**
     * Test {@link ThreadI18nContextProvider#ThreadI18nContextProvider(boolean)}.
     */
    @Test
    void testConstructor_Inheritable() {
        ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
        provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isInheritable());
    }

    /**
     * Test {@link ThreadI18nContextProvider#ThreadI18nContextProvider(Properties)}.
     */
    @Test
    void testFactory() {
        final Properties config = new Properties();
        ThreadI18nContextProvider provider = assertInstanceOf(
                ThreadI18nContextProvider.class,
                new ThreadI18nContextProvider.Factory().create(config));
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
        config.clear();
        config.setProperty(I18nConfiguration.CONTEXT_INHERITED, "true");
        provider = assertInstanceOf(
                ThreadI18nContextProvider.class,
                new ThreadI18nContextProvider.Factory().create(config));
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
        config.clear();
        config.setProperty(I18nConfiguration.CONTEXT_INHERITED, "false");
        provider = assertInstanceOf(
                ThreadI18nContextProvider.class,
                new ThreadI18nContextProvider.Factory().create(config));
        assertNotNull(provider.getSessionUUID());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isInheritable());
    }

    /**
     * Test {@link ThreadI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext() {
        final ThreadI18nContextProvider provider = spy(ThreadI18nContextProvider.builder()
                .build());
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final I18nContext result = provider.getContext();
        assertSame(mockContext, result);
        assertSame(mockContext, provider.getContexts().get());
        then(provider).should(never()).createContext();
    }

    /**
     * Test {@link ThreadI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Missing() {
        final ThreadI18nContextProvider provider = spy(ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build());
        willReturn(mockContext).given(provider).createContext();
        final I18nContext result = provider.getContext();
        assertSame(mockContext, result);
        assertSame(mockContext, provider.getContexts().get());
        then(provider).should().createContext();
    }

    /**
     * Test {@link ThreadI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Inheritable()
    throws InterruptedException {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .build();
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
     * Test {@link ThreadI18nContextProvider#getContext()}.
     */
    @Test
    void testGetContext_Invalid() {
        final ThreadI18nContextProvider provider = spy(ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build());
        provider.getContexts().set(mockContext);
        willReturn(false).given(provider).isContextValid(mockContext);
        final I18nContext result = provider.getContext();
        assertNotSame(mockContext, result);
        assertSame(result, provider.getContexts().get());
        then(provider).should().createContext();
    }

    /**
     * Test {@link ThreadI18nContextProvider#isContextValid(I18nContext)}.
     */
    @Test
    void testIsContextAlive() {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        assertFalse(provider.isContextValid(mockContext));
    }

    /**
     * Test {@link ThreadI18nContextProvider#isContextValid(I18nContext)}.
     */
    @Test
    void testIsContextAlive_Same() {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        assertTrue(provider.isContextValid(mockContext));
    }

    /**
     * Test {@link ThreadI18nContextProvider#isContextValid(I18nContext)}.
     */
    @Test
    void testIsContextAlive_WrongUUID() {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        willReturn(UUID.randomUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        assertFalse(provider.isContextValid(mockContext));
    }

    /**
     * Test {@link ThreadI18nContextProvider#isContextValid(I18nContext)}.
     */
    @Test
    void testIsContextAlive_Null() {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        assertThrows(NullPointerException.class, () -> {
            provider.isContextValid(null);
        });
    }

    /**
     * Test {@link ThreadI18nContextProvider#clearContext()}.
     */
    @Test
    void testClearContext() {
        final ThreadI18nContextProvider provider = spy(ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build());
        willReturn(provider.getSessionUUID()).given(mockContext).getProviderUUID();
        provider.getContexts().set(mockContext);
        final I18nContext tmp = provider.getContext();
        assertSame(mockContext, tmp);
        assertSame(mockContext, provider.getContexts().get());
        provider.clearContext();
        assertNull(provider.getContexts().get());
    }

    /**
     * Test {@link ThreadI18nContextProvider#clearContext()}.
     */
    @Test
    void testClearContext_Inheritable()
    throws InterruptedException {
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .build();
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
     * Test {@link ThreadI18nContextProvider#invalidate()}.
     */
    @Test
    void testInvalidate() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final String key = "mock key";
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build();
        final UUID prevUUID = provider.getSessionUUID();
        final I18nContext context = provider.getContext();
        provider.invalidate();
        assertNotNull(provider.getSessionUUID());
        assertNotEquals(prevUUID, provider.getSessionUUID());
        assertFalse(provider.isInheritable());
        assertFalse(provider.isContextValid(context));
        provider.clearContext();
    }

    /**
     * Test {@link ThreadI18nContextProvider#invalidate()}.
     */
    @Test
    void testInvalidate_Inheritable() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final String key = "mock key";
        final ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources(key, mockResources)
                .build();
        final UUID prevUUID = provider.getSessionUUID();
        final I18nContext context = provider.getContext();
        provider.invalidate();
        assertNotNull(provider.getSessionUUID());
        assertNotEquals(prevUUID, provider.getSessionUUID());
        assertTrue(provider.isInheritable());
        assertFalse(provider.isContextValid(context));
        provider.clearContext();
    }

    /**
     * Test {@link ThreadI18nContextProvider#equals(Object)} and
     * {@link ThreadI18nContextProvider#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        ThreadI18nContextProvider provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .build();
        assertNotEquals(provider, (Object) null);
        assertEquals(provider, provider);
        assertEquals(provider.hashCode(), provider.hashCode());
        assertNotEquals(provider, new Object());
        assertNotEquals(provider, ThreadI18nContextProvider.builder()
                .setInheritableContexts(false)
                .build());
        ThreadI18nContextProvider other = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .build();
        assertNotEquals(provider.getSessionUUID(), other.getSessionUUID());
        assertEquals(provider, other);
        other = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .build();
        assertNotEquals(provider, other);
        provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .build();
        assertNotEquals(provider, other);
        other = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
        provider = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources("mock key", mockResources)
                .build();
        assertNotEquals(provider, other);
        other = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources("other mock key", mockResources)
                .build();
        assertNotEquals(provider, other);
        other = ThreadI18nContextProvider.builder()
                .setInheritableContexts(true)
                .setAvailableLocales(locales)
                .setDefaultI18nResources(mockDefaultResources)
                .addI18nResources("mock key", mockResources)
                .build();
        assertEquals(provider, other);
        assertEquals(provider.hashCode(), other.hashCode());
    }

    class InheritableGetContextChild
    implements Runnable {

        private final ThreadI18nContextProvider provider;
        private I18nContext storedContext;
        private I18nContext context;

        public InheritableGetContextChild(
                final ThreadI18nContextProvider provider) {
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
