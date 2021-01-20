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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code ByClassLoaderI18nContextProviderStrategy}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see ByClassLoaderI18nContextProviderStrategy
 */
@Tag("ut")
class ByClassLoaderI18nContextProviderStrategyTest {

    private @Mock I18nContextProvider mockDefaultProvider;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nContextProvider mockAltProvider;
    private @Mock Map<ClassLoader, I18nContextProvider> mockProviderMap;
    private @Mock ClassLoader mockClassLoader;
    private @Mock Thread mockThread;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor() {
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider);
        assertSame(mockDefaultProvider, strategy.getDefaultContextProvider());
        assertNotNull(strategy.getContextProviders());
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new ByClassLoaderI18nContextProviderStrategy(null);
        });
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map() {
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap);
        assertSame(mockDefaultProvider, strategy.getDefaultContextProvider());
        assertSame(mockProviderMap, strategy.getContextProviders());
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_NullProvider() {
        assertThrows(NullPointerException.class, () -> {
            new ByClassLoaderI18nContextProviderStrategy(null, mockProviderMap);
        });
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_NullMap() {
        assertThrows(NullPointerException.class, () -> {
            new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, null);
        });
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#ByClassLoaderI18nContextProviderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_Nulls() {
        assertThrows(NullPointerException.class, () -> {
            new ByClassLoaderI18nContextProviderStrategy(null, null);
        });
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Unset() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(mockClassLoader);
        assertSame(mockDefaultProvider, result);
        assertEquals(1, map.size());
        assertSame(mockDefaultProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Set() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        map.put(mockClassLoader, mockProvider);
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(mockClassLoader);
        assertSame(mockProvider, result);
        assertEquals(1, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Unset() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockDefaultProvider, result);
        assertEquals(2, map.size());
        assertSame(mockDefaultProvider, map.get(classLoader));
        assertSame(mockDefaultProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Set() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        map.put(mockClassLoader, mockProvider);
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockProvider, result);
        assertEquals(2, map.size());
        assertSame(mockProvider, map.get(classLoader));
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Set_Alt() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        map.put(mockClassLoader, mockProvider);
        map.put(classLoader, mockAltProvider);
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockAltProvider, result);
        assertEquals(2, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
        assertSame(mockAltProvider, map.get(classLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider(Thread)}.
     */
    @Test
    void testGetContextProvider_Thread() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        willReturn(mockClassLoader).given(mockThread).getContextClassLoader();
        willReturn(mockProvider).given(strategy).getContextProvider(mockClassLoader);
        final I18nContextProvider result = strategy.getContextProvider(mockThread);
        assertSame(mockProvider, result);
        then(mockThread).should().getContextClassLoader();
        then(mockThread).shouldHaveNoMoreInteractions();
        then(strategy).should().getContextProvider(mockThread);
        then(strategy).should().getContextProvider(mockClassLoader);
        then(strategy).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#getContextProvider()}.
     */
    @Test
    void testGetContextProvider() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        willReturn(mockProvider).given(strategy).getContextProvider(Thread.currentThread());
        final I18nContextProvider result = strategy.getContextProvider();
        assertSame(mockProvider, result);
        then(strategy).should().getContextProvider();
        then(strategy).should().getContextProvider(Thread.currentThread());
        then(strategy).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, map);
        strategy.setContextProvider(mockClassLoader, mockProvider);
        assertEquals(1, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_NullClassLoader() {
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((ClassLoader) null, mockProvider);
        });
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_NullProvider() {
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider(mockClassLoader, null);
        });
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_Nulls() {
        final ByClassLoaderI18nContextProviderStrategy strategy =
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((ClassLoader) null, null);
        });
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        willReturn(mockClassLoader).given(mockThread).getContextClassLoader();
        willDoNothing().given(strategy).setContextProvider(mockClassLoader, mockProvider);
        strategy.setContextProvider(mockThread, mockProvider);
        then(mockThread).should().getContextClassLoader();
        then(mockThread).shouldHaveNoMoreInteractions();
        then(strategy).should().setContextProvider(mockThread, mockProvider);
        then(strategy).should().setContextProvider(mockClassLoader, mockProvider);
        then(strategy).shouldHaveNoMoreInteractions();
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_NullThread() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((Thread) null, mockProvider);
        });
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_NullProvider() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        final NullPointerException mockEx = new NullPointerException();
        willReturn(mockClassLoader).given(mockThread).getContextClassLoader();
        willThrow(mockEx).given(strategy).setContextProvider(mockClassLoader, null);
        final NullPointerException result = assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider(mockThread, null);
        });
        assertSame(mockEx, result);
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        willDoNothing().given(strategy).setContextProvider(Thread.currentThread(), mockProvider);
        strategy.setContextProvider(mockProvider);
        then(strategy).should().setContextProvider(mockProvider);
        then(strategy).should().setContextProvider(Thread.currentThread(), mockProvider);
        then(strategy).shouldHaveNoMoreInteractions();
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    /**
     * Test {@link ByClassLoaderI18nContextProviderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_Nulls() {
        final ByClassLoaderI18nContextProviderStrategy strategy = spy(
                new ByClassLoaderI18nContextProviderStrategy(mockDefaultProvider, mockProviderMap));
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((Thread) null, null);
        });
        then(mockProviderMap).shouldHaveNoInteractions();
    }

    static class MockClassLoader
    extends ClassLoader {

        public MockClassLoader() {
            super();
        }

        public MockClassLoader(ClassLoader parent) {
            super(parent);
        }
    }
}
