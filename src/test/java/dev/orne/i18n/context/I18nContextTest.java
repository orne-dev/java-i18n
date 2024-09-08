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
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code I18nContext}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-12
 * @since 0.1
 * @see I18nContext
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nContextTest {

    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    @BeforeAll
    static void resetConfiguration() {
        ContextTestUtils.reset();
    }

    @AfterEach
    void cleanConfiguration() {
        ContextTestUtils.reset();
    }

    /**
     * Test {@link I18nContext#getInstance()}.
     */
    @Test
    void testGetInstance() {
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        final I18nContext result = I18nContext.getInstance();
        assertSame(mockContext, result);
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nContext#getAvailableLocales()}.
     */
    @Test
    void testGetAvailableLocales() {
        ContextTestUtils.setProvider(mockProvider);
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        willReturn(locales).given(mockProvider).getAvailableLocales();
        final I18nContext context = new TestContext();
        final Locale[] result = context.getAvailableLocales();
        assertSame(locales, result);
        then(mockProvider).should().getAvailableLocales();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContext#getI18nResources()}.
     */
    @Test
    void testGetDefaultI18nResources() {
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockResources).given(mockProvider).getDefaultI18nResources();
        final I18nContext context = new TestContext();
        final I18nResources result = context.getI18nResources();
        assertSame(mockResources, result);
        then(mockProvider).should().getDefaultI18nResources();
        then(mockProvider).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nContext#getI18nResources(String)}.
     */
    @Test
    void testGetI18nResources() {
        ContextTestUtils.setProvider(mockProvider);
        final String key = "mock resources key";
        willReturn(mockResources).given(mockProvider).getI18nResources(key);
        final I18nContext context = new TestContext();
        final I18nResources result = context.getI18nResources(key);
        assertSame(mockResources, result);
        then(mockProvider).should().getI18nResources(key);
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
