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
import static org.mockito.Mockito.inOrder;

import java.util.Locale;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code I18nContext}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-12
 * @since 0.1
 * @see I18nContext
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nContextTest {

    private static I18nContextProviderStrategy preTestsStrategy;
    private @Mock I18nContextProviderStrategy mockStrategy;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    @BeforeAll
    static void saveDefaultStrategy() {
        preTestsStrategy = I18nContextProviderStrategy.getInstance();
    }

    @AfterEach
    void restoreDefaultStrategy() {
        I18nContextProviderStrategy.setInstance(preTestsStrategy);
    }

    /**
     * Test {@link I18nContext#getInstance()}.
     */
    @Test
    void testGetInstance() {
        I18nContextProviderStrategy.setInstance(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        final I18nContext result = I18nContext.getInstance();
        assertSame(mockContext, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nContext#getAvailableLocales()}.
     */
    @Test
    void testGetAvailableLocales() {
        I18nContextProviderStrategy.setInstance(mockStrategy);
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(locales).given(mockProvider).getAvailableLocales();
        final I18nContext context = spy(new TestContext());
        final Locale[] result = context.getAvailableLocales();
        assertSame(locales, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getAvailableLocales();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContext#getI18nResources()}.
     */
    @Test
    void testGetDefaultI18nResources() {
        I18nContextProviderStrategy.setInstance(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockResources).given(mockProvider).getDefaultI18nResources();
        final I18nContext context = spy(new TestContext());
        final I18nResources result = context.getI18nResources();
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getDefaultI18nResources();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContext#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        I18nContextProviderStrategy.setInstance(mockStrategy);
        final String key = "mock resources key";
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockResources).given(mockProvider).getI18nResources(key);
        final I18nContext context = spy(new TestContext());
        final I18nResources result = context.getI18nResources(key);
        assertSame(mockResources, result);
        final InOrder order = inOrder(mockStrategy, mockProvider);
        then(mockStrategy).should(order).getContextProvider();
        then(mockStrategy).shouldHaveNoMoreInteractions();
        then(mockProvider).should(order).getI18nResources(key);
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    private static class TestContext
    implements I18nContext {
        @Override
        public @NotNull UUID getProviderUUID() {
            throw new UnsupportedOperationException();
        }
        @Override
        public @NotNull Locale getLocale() {
            throw new UnsupportedOperationException();
        }
        @Override
        public void setLocale(Locale language) {
            throw new UnsupportedOperationException();
        }
    }
}
