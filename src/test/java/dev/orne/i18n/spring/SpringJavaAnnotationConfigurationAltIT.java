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

import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nContext;
import dev.orne.i18n.I18nContextProvider;
import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringContextProvider
 */
@Tag("it")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJavaAnnotationConfigurationAltIT.SpringConfig.class)
class SpringJavaAnnotationConfigurationAltIT {

    private static final String XX_LANG = "xx";
    private static final String YY_LANG = "yy";
    private static final String ZZ_LANG = "zz";
    private static final Locale XX_LOCALE = new Locale(XX_LANG);
    private static final Locale YY_LOCALE = new Locale(YY_LANG);
    private static final Locale ZZ_LOCALE = new Locale(ZZ_LANG);

    private static final String ALT_I18N_RESOURCES_KEY = "alt";
    private static final String BUNDLE_PROP = "dev.orne.i18n.test.bundle";
    private static final Object BUNDLE_VALUE_DEFAULT = "dev/orne/i18n/test-messages.properties";
    private static final Object BUNDLE_VALUE_YY = "dev/orne/i18n/test-messages_yy.properties";
    private static final Object BUNDLE_VALUE_ZZ = "dev/orne/i18n/test-messages_zz.properties";
    private static final String FAIL_VALUE = "Message not found";

    @Autowired
    private ApplicationContext appContext;

    @AfterEach
    void clearContext() {
        I18N.clearContext();
    }

    /**
     * Tests {@code MessageSource} configuration.
     */
    @Test
    void testMessageSource() {
        final MessageSource messageSource = appContext.getBean(MessageSource.class);
        assertNotNull(messageSource);
        assertEquals(BUNDLE_VALUE_DEFAULT, messageSource.getMessage(
                BUNDLE_PROP,
                new Object[0],
                XX_LOCALE));
        assertEquals(BUNDLE_VALUE_DEFAULT, messageSource.getMessage(
                BUNDLE_PROP,
                new Object[0],
                Locale.ENGLISH));
        assertEquals(BUNDLE_VALUE_YY, messageSource.getMessage(
                BUNDLE_PROP,
                new Object[0],
                YY_LOCALE));
        assertEquals(BUNDLE_VALUE_ZZ, messageSource.getMessage(
                BUNDLE_PROP,
                new Object[0],
                ZZ_LOCALE));
    }

    /**
     * Tests default {@code I18nContextProvider} configuration.
     */
    @Test
    void testDefaultProvider() {
        final I18nContextProvider provider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof I18nSpringContextProvider);
        assertArrayEquals(new Locale[] {
                XX_LOCALE,
                YY_LOCALE,
                ZZ_LOCALE
        }, provider.getAvailableLocales());
        assertSame(provider, I18N.getContextProvider());
    }

    /**
     * Tests available locales configuration.
     */
    @Test
    void testAvailableLocales() {
        assertArrayEquals(new Locale[] {
                XX_LOCALE,
                YY_LOCALE,
                ZZ_LOCALE
        }, I18N.getAvailableLocales());
    }

    /**
     * Tests default {@code I18nResources} configuration.
     */
    @Test
    void testDefaultI18nResources() {
        final I18nResources resources = I18N.getDefaultI18nResources();
        assertNotNull(resources);
        assertTrue(resources instanceof I18nSpringResources);
        assertEquals(BUNDLE_VALUE_DEFAULT, resources.getMessage(
                FAIL_VALUE,
                BUNDLE_PROP,
                XX_LOCALE));
        assertEquals(BUNDLE_VALUE_DEFAULT, resources.getMessage(
                FAIL_VALUE,
                BUNDLE_PROP,
                Locale.ENGLISH));
        assertEquals(BUNDLE_VALUE_YY, resources.getMessage(
                FAIL_VALUE,
                BUNDLE_PROP,
                YY_LOCALE));
        assertEquals(BUNDLE_VALUE_ZZ, resources.getMessage(
                FAIL_VALUE,
                BUNDLE_PROP,
                ZZ_LOCALE));
    }

    /**
     * Tests alternative {@code I18nResources} configuration.
     */
    @Test
    void testAlternativeI18nResources() {
        final I18nResources resources = I18N.getI18nResources(ALT_I18N_RESOURCES_KEY);
        assertNotNull(resources);
        assertSame(I18N.getDefaultI18nResources(), resources);
    }

    /**
     * Tests {@code I18nContext} default configuration.
     */
    @Test
    void testI18NContext() {
        final I18nContext context = I18N.getContext();
        assertNotNull(context);
        assertTrue(context instanceof I18nSpringContext);
        assertFalse(context.isFullMode());
        assertEquals(Locale.getDefault(), context.getLocale());
        assertEquals(LocaleContextHolder.getLocale(), context.getLocale());
        assertEquals(BUNDLE_VALUE_DEFAULT, I18N.getDefaultI18nResources().getMessage(
                FAIL_VALUE,
                BUNDLE_PROP));
    }

    /**
     * Tests locale change.
     */
    @Test
    void testLocaleChange() {
        I18N.setLocale(ZZ_LOCALE);
        assertEquals(ZZ_LOCALE, I18N.getLocale());
        assertEquals(ZZ_LOCALE, LocaleContextHolder.getLocale());
        assertEquals(BUNDLE_VALUE_ZZ, I18N.getDefaultI18nResources().getMessage(
                FAIL_VALUE,
                BUNDLE_PROP));
    }

    @Configuration
    @EnableI18N(availableLanguages= { XX_LANG, YY_LANG, ZZ_LANG })
    static class SpringConfig {
        @Bean
        @Primary
        public MessageSource messageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames("dev/orne/i18n/test-messages");
            source.setDefaultEncoding("UTF-8");
            return source;
        }
        @Bean
        public MessageSource altMessageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames("dev/orne/i18n/test-messages-alt");
            source.setDefaultEncoding("UTF-8");
            return source;
        }
    }
}
