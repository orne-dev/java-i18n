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

import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nResources;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.i18n.context.I18nContextProviderStrategy;
import dev.orne.i18n.spring.I18nSpringContextProvider;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 0.1
 * @see I18nSpringContextProvider
 */
class AbstractSpringConfigurationAltIT
extends AbstractSpringConfigurationIT {

    protected static final String ALT_RESOURCES_BEAN = "altResources";

    /**
     * Tests default {@code I18nContextProvider} configuration.
     */
    @Test
    void testDefaultProvider() {
        final I18nContextProvider provider = I18nContextProviderStrategy.getInstance().getDefaultContextProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof I18nSpringContextProvider);
        assertArrayEquals(new Locale[] {
                TestMessages.DEFAULT_LOCALE,
                TestMessages.YY_LOCALE,
                TestMessages.ZZ_LOCALE
        }, provider.getAvailableLocales());
        assertSame(provider, I18nContextProvider.getInstance());
    }

    /**
     * Tests available locales configuration.
     */
    @Test
    void testAvailableLocales() {
        assertArrayEquals(new Locale[] {
                TestMessages.DEFAULT_LOCALE,
                TestMessages.YY_LOCALE,
                TestMessages.ZZ_LOCALE
        }, I18N.getAvailableLocales());
    }

    /**
     * Tests alternative {@code I18nResources} configuration.
     */
    @Test
    void testAlternativeI18nResources() {
        final I18nResources resources = I18N.getI18nResources(ALT_I18N_RESOURCES_KEY);
        assertNotNull(resources);
        assertNotSame(I18N.getI18nResources(), resources);
        assertEquals(TestMessages.BUNDLE_ID_ALT_DEFAULT_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.DEFAULT_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_ALT_DEFAULT_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                Locale.ENGLISH));
        assertEquals(TestMessages.BUNDLE_ID_ALT_YY_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.YY_LOCALE));
        assertEquals(TestMessages.BUNDLE_ID_ALT_ZZ_VALUE, resources.getMessage(
                FAIL_VALUE,
                TestMessages.Entries.BUNDLE_ID,
                TestMessages.ZZ_LOCALE));
    }
}
