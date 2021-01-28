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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code DefaultI18nContext}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see DefaultI18nContext
 */
@Tag("ut")
class DefaultI18nContextTest {

    private static final Locale MOCK_DEFAULT_LOCALE = new Locale("xx");
    private static final Locale MOCK_LOCALE = new Locale("yy");
    private static Locale preTestsDefaultLocale;

    @BeforeAll
    static void setDefaultLocale() {
        preTestsDefaultLocale = Locale.getDefault();
        Locale.setDefault(MOCK_DEFAULT_LOCALE);
    }

    @AfterAll
    static void restoreDefaultLocale() {
        Locale.setDefault(preTestsDefaultLocale);
    }

    /**
     * Test {@link DefaultI18nContext#DefaultI18nContext(UUID)}.
     */
    @Test
    void testConstructor() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        assertEquals(MOCK_DEFAULT_LOCALE, context.getLocale());
        assertFalse(context.isFullMode());
    }

    /**
     * Test {@link DefaultI18nContext#DefaultI18nContext(UUID)}.
     */
    @Test
    void testConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultI18nContext((UUID) null);
        });
    }

    /**
     * Test {@link DefaultI18nContext#DefaultI18nContext(DefaultI18nContext)}.
     */
    @Test
    void testCopyConstructor() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        context.setLocale(MOCK_LOCALE);
        context.setFullMode(true);
        final DefaultI18nContext result = new DefaultI18nContext(context);
        assertEquals(context.getProviderUUID(), result.getProviderUUID());
        assertEquals(context.isFullMode(), result.isFullMode());
        assertEquals(context, result);
        assertEquals(context.hashCode(), result.hashCode());
    }

    /**
     * Test {@link DefaultI18nContext#DefaultI18nContext(DefaultI18nContext)}.
     */
    @Test
    void testCopyConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultI18nContext((DefaultI18nContext) null);
        });
    }

    /**
     * Test {@link DefaultI18nContext#setLocale(Locale)}.
     */
    @Test
    void testSetLocale() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        context.setLocale(MOCK_LOCALE);
        assertEquals(MOCK_LOCALE, context.getLocale());
    }

    /**
     * Test {@link DefaultI18nContext#setLocale(Locale)}.
     */
    @Test
    void testSetLocale_Null() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        context.setLocale(MOCK_LOCALE);
        context.setLocale(null);
        assertEquals(MOCK_DEFAULT_LOCALE, context.getLocale());
    }

    /**
     * Test {@link DefaultI18nContext#setFullMode(boolean)}.
     */
    @Test
    void testSetFullMode() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        context.setFullMode(true);
        assertTrue(context.isFullMode());
    }

    /**
     * Test {@link DefaultI18nContext#equals(Object)} and
     * {@link DefaultI18nContext#hashCode()}.
     */
    @Test
    void testEqualsHashCodeToString() {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        assertNotEquals(context, (Object) null);
        assertEquals(context, context);
        assertEquals(context.hashCode(), context.hashCode());
        assertNotEquals(context, new Object());
        assertNotEquals(context, new DefaultI18nContext(UUID.randomUUID()));
        final DefaultI18nContext other = new DefaultI18nContext(context);
        assertEquals(context, other);
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
        other.setFullMode(true);
        assertNotEquals(context, other);
        assertNotNull(other.toString());
        other.setLocale(MOCK_LOCALE);
        assertNotEquals(context, other);
        assertNotNull(other.toString());
        other.setFullMode(false);
        assertNotEquals(context, other);
        assertNotNull(other.toString());
        context.setLocale(MOCK_LOCALE);
        assertEquals(context, other);
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
        context.setFullMode(true);
        other.setFullMode(true);
        assertEquals(context, other);
        assertEquals(context.hashCode(), other.hashCode());
        assertNotNull(other.toString());
    }

    /**
     * Test {@link java.io.Serializable} implementation.
     */
    @Test
    void testSerializable() throws IOException, ClassNotFoundException {
        final DefaultI18nContext context = new DefaultI18nContext(UUID.randomUUID());
        context.setLocale(MOCK_LOCALE);
        context.setFullMode(true);
        final byte[] serializationResult;
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream out = new ObjectOutputStream(baos)){
            out.writeObject(context);
            serializationResult = baos.toByteArray();
        }
        assertNotNull(serializationResult);
        final DefaultI18nContext result;
        try (
                final ByteArrayInputStream bais = new ByteArrayInputStream(serializationResult);
                final ObjectInputStream in = new ObjectInputStream(bais)){
            result = (DefaultI18nContext) in.readObject();
        }
        assertNotNull(result);
        assertEquals(context, result);
        assertEquals(context.hashCode(), result.hashCode());
    }
}
