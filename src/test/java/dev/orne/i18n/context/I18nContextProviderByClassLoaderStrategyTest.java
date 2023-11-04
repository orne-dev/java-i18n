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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code I18nContextProviderByClassLoaderStrategy}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nContextProviderByClassLoaderStrategy
 */
@Tag("ut")
class I18nContextProviderByClassLoaderStrategyTest {

    private @Mock I18nContextProvider mockDefaultProvider;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nContextProvider mockAltProvider;
    private @Mock Map<ClassLoader, I18nContextProvider> mockProviderMap;
    private @Mock ClassLoader mockClassLoader;
    private @Mock Thread mockThread;
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
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy()}.
     */
    @Test
    void testConstructor() {
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy();
        assertTrue(strategy.getDefaultContextProvider() instanceof DefaultI18nContextProvider);
        assertNotNull(strategy.getContextProviders());
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor_Provider() {
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider);
        assertSame(mockDefaultProvider, strategy.getDefaultContextProvider());
        assertNotNull(strategy.getContextProviders());
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor_Provider_Null() {
        assertThrows(NullPointerException.class, () -> {
            new I18nContextProviderByClassLoaderStrategy(null);
        });
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map() {
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap);
        assertSame(mockDefaultProvider, strategy.getDefaultContextProvider());
        assertSame(mockProviderMap, strategy.getContextProviders());
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_NullProvider() {
        assertThrows(NullPointerException.class, () -> {
            new I18nContextProviderByClassLoaderStrategy(null, mockProviderMap);
        });
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_NullMap() {
        assertThrows(NullPointerException.class, () -> {
            new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, null);
        });
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#I18nContextProviderByClassLoaderStrategy(I18nContextProvider, Map)}.
     */
    @Test
    void testConstructor_Map_Nulls() {
        assertThrows(NullPointerException.class, () -> {
            new I18nContextProviderByClassLoaderStrategy(null, null);
        });
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Unset() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(mockClassLoader);
        assertSame(mockDefaultProvider, result);
        assertEquals(1, map.size());
        assertSame(mockDefaultProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Set() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        map.put(mockClassLoader, mockProvider);
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(mockClassLoader);
        assertSame(mockProvider, result);
        assertEquals(1, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Unset() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockDefaultProvider, result);
        assertEquals(2, map.size());
        assertSame(mockDefaultProvider, map.get(classLoader));
        assertSame(mockDefaultProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Set() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        map.put(mockClassLoader, mockProvider);
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockProvider, result);
        assertEquals(2, map.size());
        assertSame(mockProvider, map.get(classLoader));
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(ClassLoader)}.
     */
    @Test
    void testGetContextProvider_ClassLoader_Parent_Set_Alt() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final MockClassLoader classLoader = new MockClassLoader(mockClassLoader);
        map.put(mockClassLoader, mockProvider);
        map.put(classLoader, mockAltProvider);
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        final I18nContextProvider result = strategy.getContextProvider(classLoader);
        assertSame(mockAltProvider, result);
        assertEquals(2, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
        assertSame(mockAltProvider, map.get(classLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider(Thread)}.
     */
    @Test
    void testGetContextProvider_Thread() {
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
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
     * Test {@link I18nContextProviderByClassLoaderStrategy#getContextProvider()}.
     */
    @Test
    void testGetContextProvider() {
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        willReturn(mockProvider).given(strategy).getContextProvider(Thread.currentThread());
        final I18nContextProvider result = strategy.getContextProvider();
        assertSame(mockProvider, result);
        then(strategy).should().getContextProvider();
        then(strategy).should().getContextProvider(Thread.currentThread());
        then(strategy).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader() {
        final Map<ClassLoader, I18nContextProvider> map = new HashMap<>();
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, map);
        strategy.setContextProvider(mockClassLoader, mockProvider);
        assertEquals(1, map.size());
        assertSame(mockProvider, map.get(mockClassLoader));
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_NullClassLoader() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((ClassLoader) null, mockProvider);
        });
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_NullProvider() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider(mockClassLoader, null);
        });
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(ClassLoader, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_ClassLoader_Nulls() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap);
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((ClassLoader) null, null);
        });
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        willReturn(mockClassLoader).given(mockThread).getContextClassLoader();
        willDoNothing().given(strategy).setContextProvider(mockClassLoader, mockProvider);
        strategy.setContextProvider(mockThread, mockProvider);
        then(mockThread).should().getContextClassLoader();
        then(mockThread).shouldHaveNoMoreInteractions();
        then(strategy).should().setContextProvider(mockThread, mockProvider);
        then(strategy).should().setContextProvider(mockClassLoader, mockProvider);
        then(strategy).shouldHaveNoMoreInteractions();
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_NullThread() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((Thread) null, mockProvider);
        });
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_NullProvider() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        final NullPointerException mockEx = new NullPointerException();
        willReturn(mockClassLoader).given(mockThread).getContextClassLoader();
        willThrow(mockEx).given(strategy).setContextProvider(mockClassLoader, null);
        final NullPointerException result = assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider(mockThread, null);
        });
        assertSame(mockEx, result);
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        willDoNothing().given(strategy).setContextProvider(Thread.currentThread(), mockProvider);
        strategy.setContextProvider(mockProvider);
        then(strategy).should().setContextProvider(mockProvider);
        then(strategy).should().setContextProvider(Thread.currentThread(), mockProvider);
        then(strategy).shouldHaveNoMoreInteractions();
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#setContextProvider(Thread, I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Thread_Nulls() {
        willReturn(Collections.emptySet()).given(mockProviderMap).keySet();
        willReturn(Collections.emptyList()).given(mockProviderMap).values();
        final I18nContextProviderByClassLoaderStrategy strategy = spy(
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider, mockProviderMap));
        assertThrows(NullPointerException.class, () -> {
            strategy.setContextProvider((Thread) null, null);
        });
        then(mockProviderMap).should().keySet();
        then(mockProviderMap).should().values();
        then(mockProviderMap).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContextProviderByClassLoaderStrategy#invalidate()}.
     */
    @Test
    void testInvalidate() {
        final I18nContextProviderByClassLoaderStrategy strategy =
                new I18nContextProviderByClassLoaderStrategy(mockDefaultProvider);
        strategy.setContextProvider(mockClassLoader, mockProvider);
        strategy.invalidate();
        then(mockDefaultProvider).should().invalidate();
        then(mockProvider).should().invalidate();
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
