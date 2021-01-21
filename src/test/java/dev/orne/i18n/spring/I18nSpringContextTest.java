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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Unit tests for {@code I18nSpringContext}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringContext
 */
@Tag("ut")
class I18nSpringContextTest {

    private static final Locale MOCK_DEFAULT_LOCALE = new Locale("xx");
    private static final Locale MOCK_DEFAULT_CONTEXT_LOCALE = new Locale("yy");
    private static final Locale MOCK_LOCALE = new Locale("zz");
    private static LocaleContext preTestsLocaleContext;
    private static Locale preTestsDefaultLocale;

    @BeforeAll
    static void saveDefaults() {
        preTestsLocaleContext = LocaleContextHolder.getLocaleContext();
        preTestsDefaultLocale = Locale.getDefault();
        Locale.setDefault(MOCK_DEFAULT_LOCALE);
    }

    @AfterAll
    static void restoreDefaults() {
        Locale.setDefault(preTestsDefaultLocale);
        LocaleContextHolder.setLocaleContext(preTestsLocaleContext);
    }

    @BeforeEach
    void initMocks() {
        LocaleContextHolder.resetLocaleContext();
        LocaleContextHolder.setLocale(MOCK_DEFAULT_CONTEXT_LOCALE);
    }

    /**
     * Test {@link I18nSpringContext#I18nSpringContext()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringContext context = new I18nSpringContext();
        assertEquals(MOCK_DEFAULT_CONTEXT_LOCALE, context.getLocale());
        assertFalse(context.isFullMode());
    }

    /**
     * Test {@link I18nSpringContext#setLocale(Locale)}.
     */
    @Test
    void testSetLocale() {
        final I18nSpringContext context = new I18nSpringContext();
        context.setLocale(MOCK_LOCALE);
        assertEquals(MOCK_LOCALE, context.getLocale());
        assertEquals(MOCK_LOCALE, LocaleContextHolder.getLocale());
    }

    /**
     * Test {@link I18nSpringContext#setLocale(Locale)}.
     */
    @Test
    void testSetLocale_Null() {
        final I18nSpringContext context = new I18nSpringContext();
        context.setLocale(null);
        assertEquals(MOCK_DEFAULT_LOCALE, LocaleContextHolder.getLocale());
        assertEquals(MOCK_DEFAULT_LOCALE, context.getLocale());
    }

    /**
     * Test {@link I18nSpringContext#setFullMode(boolean)}.
     */
    @Test
    void testSetFullMode() {
        final I18nSpringContext context = new I18nSpringContext();
        context.setFullMode(true);
        assertTrue(context.isFullMode());
    }

    /**
     * Test {@link I18nSpringContext#equals(Object)} and
     * {@link I18nSpringContext#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final I18nSpringContext context = new I18nSpringContext();
        final I18nSpringContext other = new I18nSpringContext();
        assertFalse(context.equals(null));
        assertTrue(context.equals(context));
        assertEquals(context.hashCode(), context.hashCode());
        assertFalse(context.equals(new Object()));
        assertTrue(context.equals(other));
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
        other.setFullMode(true);
        assertFalse(context.equals(other));
        assertNotNull(other.toString());
        other.setFullMode(false);
        assertTrue(context.equals(other));
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
        context.setFullMode(true);
        other.setFullMode(true);
        assertTrue(context.equals(other));
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
    }

    /**
     * Test {@link java.io.Serializable} implementation.
     */
    @Test
    void testSerializable() throws IOException, ClassNotFoundException {
        final I18nSpringContext context = new I18nSpringContext();
        context.setFullMode(true);
        final byte[] serializationResult;
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream out = new ObjectOutputStream(baos)){
            out.writeObject(context);
            serializationResult = baos.toByteArray();
        }
        assertNotNull(serializationResult);
        final I18nSpringContext result;
        try (
                final ByteArrayInputStream bais = new ByteArrayInputStream(serializationResult);
                final ObjectInputStream in = new ObjectInputStream(bais)){
            result = (I18nSpringContext) in.readObject();
        }
        assertNotNull(result);
        assertEquals(context, result);
        assertEquals(context.hashCode(), result.hashCode());
    }
}
