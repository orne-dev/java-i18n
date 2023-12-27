package dev.orne.i18n.it.common;

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

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nResources;
import dev.orne.i18n.context.I18nContext;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.i18n.context.I18nContextProviderStrategy;
import dev.orne.i18n.spring.I18nSpringContext;
import dev.orne.i18n.spring.I18nSpringContextProvider;
import dev.orne.i18n.spring.I18nSpringResources;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 0.1
 * @see I18nSpringContextProvider
 */
abstract class AbstractSpringConfigurationIT {

    protected static final String ALT_I18N_RESOURCES_KEY = "alt";
    protected static final String FAIL_VALUE = "Message not found";

    @Autowired
    protected ApplicationContext appContext;

    @AfterEach
    void clearContext() {
        I18nContextProviderStrategy.setInstance(null);
    }

    /**
     * Tests {@code MessageSource} configuration.
     */
    @Test
    void testMessageSource() {
        final MessageSource messageSource = appContext.getBean(MessageSource.class);
        assertNotNull(messageSource);
        assertEquals(TestMessages.BUNDLE_ID_DEFAULT_VALUE, messageSource.getMessage(
                TestMessages.Entries.BUNDLE_ID,
                new Object[0],
                TestMessages.DEFAULT_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_DEFAULT_VALUE, messageSource.getMessage(
                TestMessages.Entries.BUNDLE_ID,
                new Object[0],
                Locale.ENGLISH));
        assertEquals(TestMessages.BUNDLE_ID_YY_VALUE, messageSource.getMessage(
                TestMessages.Entries.BUNDLE_ID,
                new Object[0],
                TestMessages.YY_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_ZZ_VALUE, messageSource.getMessage(
                TestMessages.Entries.BUNDLE_ID,
                new Object[0],
                TestMessages.ZZ_LOCALE));
    }

    /**
     * Tests default {@code I18nContextProvider} configuration.
     */
    @Test
    void testDefaultProvider() {
        final I18nContextProvider provider = I18nContextProviderStrategy.getInstance().getDefaultContextProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof I18nSpringContextProvider);
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertSame(provider, I18nContextProvider.getInstance());
    }

    /**
     * Tests default {@code I18nContextProvider} configuration.
     */
    @Test
    void testAvailableLocales() {
        assertArrayEquals(Locale.getAvailableLocales(), I18N.getAvailableLocales());
    }

    /**
     * Tests default {@code I18nResources} configuration.
     */
    @Test
    void testDefaultI18nResources() {
        final I18nResources resources = I18N.getResources();
        assertNotNull(resources);
        assertTrue(resources instanceof I18nSpringResources);
        assertEquals(TestMessages.BUNDLE_ID_DEFAULT_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.DEFAULT_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_DEFAULT_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                Locale.ENGLISH));
        assertEquals(TestMessages.BUNDLE_ID_YY_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.YY_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_ZZ_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.ZZ_LOCALE));
    }

    /**
     * Tests alternative {@code I18nResources} configuration.
     */
    @Test
    void testAlternativeI18nResources() {
        final I18nResources resources = I18N.getResources(ALT_I18N_RESOURCES_KEY);
        assertNotNull(resources);
        assertSame(I18N.getResources(), resources);
    }

    /**
     * Tests {@code I18nContext} default configuration.
     */
    @Test
    void testI18NContext() {
        final I18nContext context = I18nContext.getInstance();
        assertNotNull(context);
        assertTrue(context instanceof I18nSpringContext);
        assertEquals(Locale.getDefault(), context.getLocale());
        assertEquals(LocaleContextHolder.getLocale(), context.getLocale());
        assertEquals(TestMessages.BUNDLE_ID_DEFAULT_VALUE, I18N.getResources().getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID));
    }

    /**
     * Tests locale change.
     */
    @Test
    void testLocaleChange() {
        I18N.setLocale(TestMessages.ZZ_LOCALE);
        assertEquals(TestMessages.ZZ_LOCALE, I18N.getLocale());
        assertEquals(TestMessages.ZZ_LOCALE, LocaleContextHolder.getLocale());
        assertEquals(TestMessages.BUNDLE_ID_ZZ_VALUE, I18N.getResources().getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID));
    }
}
