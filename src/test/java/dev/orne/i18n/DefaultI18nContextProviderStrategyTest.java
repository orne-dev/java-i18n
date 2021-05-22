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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code DefaultI18nContextProviderStrategy}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see DefaultI18nContextProviderStrategy
 */
@Tag("ut")
class DefaultI18nContextProviderStrategyTest {

    private @Mock I18nContextProvider mockDefaultProvider;
    private @Mock I18nContextProvider mockProvider;
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
     * Test {@link DefaultI18nContextProviderStrategy#DefaultI18nContextProviderStrategy()}.
     */
    @Test
    void testConstructor() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy();
        assertTrue(strategy.getDefaultContextProvider() instanceof DefaultI18nContextProvider);
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#DefaultI18nContextProviderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor_Provider() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy(mockDefaultProvider);
        assertSame(mockDefaultProvider, strategy.getDefaultContextProvider());
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#DefaultI18nContextProviderStrategy(I18nContextProvider)}.
     */
    @Test
    void testConstructor_Provider_Null() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultI18nContextProviderStrategy(null);
        });
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#setDefaultContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetDefaultContextProvider() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy(mockDefaultProvider);
        strategy.setDefaultContextProvider(mockProvider);
        assertSame(mockProvider, strategy.getContextProvider());
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#setDefaultContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetDefaultContextProvider_Null() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy(mockDefaultProvider);
        assertThrows(NullPointerException.class, () -> {
            strategy.setDefaultContextProvider(null);
        });
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#getContextProvider()}.
     */
    @Test
    void testGetContextProvider() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy(mockDefaultProvider);
        assertSame(mockDefaultProvider, strategy.getContextProvider());
        strategy.setDefaultContextProvider(mockProvider);
        assertSame(mockProvider, strategy.getContextProvider());
    }

    /**
     * Test {@link DefaultI18nContextProviderStrategy#invalidate()}.
     */
    @Test
    void testInvalidate() {
        final DefaultI18nContextProviderStrategy strategy =
                new DefaultI18nContextProviderStrategy(mockDefaultProvider);
        strategy.invalidate();
        then(mockDefaultProvider).should().invalidate();
    }
}
