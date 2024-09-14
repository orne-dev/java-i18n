package dev.orne.i18n.spring;

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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

import dev.orne.i18n.context.DummyI18nResources;
import dev.orne.i18n.context.I18nContext;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringContextProvider
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nSpringContextProviderTest {

    private @Mock MessageSource source;
    private @Mock I18nContext mockContext;

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .build();
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource)}.
     */
    @Test
    void testConstructor_MessageSource() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .setDefaultI18nResources(source)
                .build();
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(source, ((I18nSpringResources) provider.getDefaultI18nResources()).getSource());
        assertTrue(provider.getI18nResources().isEmpty());
        assertTrue(provider.isInheritable());
        then(source).shouldHaveNoInteractions();
    }


    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(boolean)}.
     */
    @Test
    void testConstructor_Inheritable() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isInheritable());
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource, boolean)}.
     */
    @Test
    void testConstructor_MessageSource_Inheritable() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .setDefaultI18nResources(source)
                .setInheritableContexts(false)
                .build();
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(source, ((I18nSpringResources) provider.getDefaultI18nResources()).getSource());
        assertTrue(provider.getI18nResources().isEmpty());
        assertFalse(provider.isInheritable());
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nSpringContextProvider#I18nSpringContextProvider(MessageSource, boolean)}.
     */
    @Test
    void testConstructor_MessageSource_Inheritable_Null() {
        assertThrows(NullPointerException.class, () -> {
            I18nSpringContextProvider.builder()
                    .setDefaultI18nResources((MessageSource) null);
        });
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext()}.
     */
    @Test
    void testCreateContext() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .build();
        final I18nContext result = provider.createContext();
        assertTrue(result instanceof I18nSpringContext);
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .build();
        final I18nContext result = provider.createContext(mockContext);
        assertTrue(result instanceof I18nSpringContext);
    }

    /**
     * Test {@link I18nSpringContextProvider#createContext(I18nContext)}.
     */
    @Test
    void testCreateContext_Parent_Null() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .setInheritableContexts(false)
                .build();
        assertThrows(NullPointerException.class, () -> {
            provider.createContext(null);
        });
    }

    /**
     * Test {@link I18nSpringContextProvider#clearContext()}.
     */
    @Test
    void testClearContext() {
        final I18nSpringContextProvider provider = I18nSpringContextProvider.builder()
                .build();
        final I18nContext context = provider.getContext();
        final Locale locale = new Locale("zz");
        context.setLocale(locale);
        assertEquals(locale, context.getLocale());
        assertEquals(locale, LocaleContextHolder.getLocale());
        final LocaleContext springContext = LocaleContextHolder.getLocaleContext();
        assertNotNull(springContext);
        assertEquals(locale, springContext.getLocale());
        provider.clearContext();
        assertFalse(provider.isContextValid(context));
        assertNull(LocaleContextHolder.getLocaleContext());
    }
}
