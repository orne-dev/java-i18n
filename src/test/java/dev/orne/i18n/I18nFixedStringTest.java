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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.validation.constraints.NotNull;

/**
 * Unit tests for {@code I18nFixedString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nFixedString
 */
@Tag("ut")
class I18nFixedStringTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);

    private @Mock I18nString mockI18nString;
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
     * Test {@link I18nFixedString#from(String)}.
     */
    @Test
    void testFrom() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString result = I18nFixedString.from(text);
        assertNotNull(result);
        assertSame(text, result.get());
        assertSame(text, result.get(MOCK_LANG));
        assertSame(text, result.get(MOCK_LOCALE));
        assertSame(result, I18nFixedString.from(text));
    }

    /**
     * Test {@link I18nFixedString#from(String)}.
     */
    @Test
    void testFrom_Null() {
        assertNull(I18nFixedString.from((String) null));
    }

    /**
     * Test {@link I18nFixedString#from(I18nString)}.
     */
    @Test
    void testFrom_I18nString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(mockI18nString).get();
        final I18nFixedString result = I18nFixedString.from(mockI18nString);
        assertNotNull(result);
        assertSame(text, result.get());
        assertSame(text, result.get(MOCK_LANG));
        assertSame(text, result.get(MOCK_LOCALE));
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
        assertSame(result, I18nFixedString.from(text));
    }

    /**
     * Test {@link I18nFixedString#from(I18nString)}.
     */
    @Test
    void testFrom_I18nString_Null() {
        assertNull(I18nFixedString.from((I18nString) null));
    }

    /**
     * Test {@link I18nFixedString#equals(Object)} and {@link I18nFixedString#hashCode()}.
     */
    @Test
    void testEqualsHash() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString result = I18nFixedString.from(text);
        assertNotEquals(result, (Object) null);
        assertEquals(result, result);
        assertEquals(result.hashCode(), result.hashCode());
        assertNotEquals(result, new Object());
        willReturn(text).given(mockI18nString).get();
        assertNotEquals(result, mockI18nString);
        then(mockI18nString).shouldHaveNoInteractions();
        I18nFixedString other = I18nFixedString.from(text);
        assertEquals(result, other);
        assertEquals(result.hashCode(), other.hashCode());
        other = I18nFixedString.from(randomString(text));
        assertNotEquals(result, other);
    }

    /**
     * Test {@link I18nFixedString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString result = I18nFixedString.from(text);
        assertFalse(result.isEquivalent(null));
        assertTrue(result.isEquivalent(result));
        willReturn(text).given(mockI18nString).get();
        assertTrue(result.isEquivalent(mockI18nString));
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nFixedString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent_I18nString_Other() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString result = I18nFixedString.from(text);
        final String otherText = randomString(text);
        willReturn(otherText).given(mockI18nString).get();
        assertFalse(result.isEquivalent(mockI18nString));
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nFixedString#asMap()}.
     */
    @Test
    void testAsMap() {
        String text = RandomStringUtils.random(RND_STR_LENGTH);
        I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringMap result = bean.asMap();
        assertNotNull(result);
        assertEquals(text, result.getDefaultText());
        assertTrue(result.getI18n().isEmpty());
    }

    /**
     * Test {@link I18nFixedString#toString()}.
     */
    @Test
    void testToString() {
        String text = RandomStringUtils.random(RND_STR_LENGTH);
        I18nFixedString bean = I18nFixedString.from(text);
        assertEquals(text, bean.toString());
    }

    /**
     * Test {@link java.io.Serializable} implementation.
     */
    @Test
    void testSerializable() throws IOException, ClassNotFoundException {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final byte[] serializationResult;
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream out = new ObjectOutputStream(baos)){
            out.writeObject(bean);
            serializationResult = baos.toByteArray();
        }
        assertNotNull(serializationResult);
        final I18nFixedString result;
        try (
                final ByteArrayInputStream bais = new ByteArrayInputStream(serializationResult);
                final ObjectInputStream in = new ObjectInputStream(bais)){
            result = (I18nFixedString) in.readObject();
        }
        assertNotNull(result);
        assertEquals(bean, result);
        assertEquals(bean.hashCode(), result.hashCode());
    }

    /**
     * Returns a random {@code String} that is not equal to {@code avoid}.
     * 
     * @param avoid The {@code String} to avoid
     * @return The random {@code String} not equal to {@code avoid}
     */
    private @NotNull String randomString(
            final @NotNull String avoid) {
        String result;
        do {
            result = RandomStringUtils.random(RND_STR_LENGTH);
        } while (avoid.equals(result));
        return result;
    }
}
