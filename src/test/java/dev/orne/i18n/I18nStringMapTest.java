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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code I18nStringMap}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nStringMap
 */
@Tag("ut")
class I18nStringMapTest {

    private static final String MOCK_DEF_MSG = "mock default message";
    private static final String MOCK_CONTEXT_MSG = "mock cx message";
    private static final String MOCK_XX_MSG = "mock xx message";
    private static final String MOCK_YY_MSG = "mock xx message";
    private static final String MOCK_ZZ_MSG = "mock xx message";
    private static final String CONTEXT_LANG = "cx";
    private static final String XX_LANG = "xx";
    private static final String YY_LANG = "yy";
    private static final String ZZ_LANG = "zz";
    private static final Locale CONTEXT_LOCALE = new Locale(CONTEXT_LANG);
    private static final Locale XX_LOCALE = new Locale(XX_LANG);
    private static final Locale YY_LOCALE = new Locale(YY_LANG);
    private static final Locale ZZ_LOCALE = new Locale(ZZ_LANG);

    private static I18nContextProviderStrategy preTestsStrategy;
    private @Mock I18nContextProviderStrategy mockStrategy;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;

    @BeforeAll
    static void saveDefaultStrategy() {
        preTestsStrategy = I18N.getContextProviderStrategy();
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockResources).given(mockProvider).getDefaultI18nResources();
        willReturn(mockResources).given(mockProvider).getI18nResources(any());
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(CONTEXT_LOCALE).given(mockContext).getLocale();
    }

    @AfterEach
    void restoreDefaultStrategy() {
        I18N.setContextProviderStrategy(preTestsStrategy);
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(String)}.
     */
    @Test
    void testConstructor() {
        final I18nStringMap result = new I18nStringMap(MOCK_DEF_MSG);
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertNotNull(result.getAvailableTranslations());
        assertTrue(result.getAvailableTranslations().isEmpty());
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(String)}.
     */
    @Test
    void testConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new I18nStringMap((String) null);
        });
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(I18nString)}.
     */
    @Test
    void testCopyConstructor() {
        final I18nStringMap copy = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        final I18nStringMap result = new I18nStringMap(copy);
        assertEquals(copy, result);
        assertEquals(copy.getDefaultText(), result.getDefaultText());
        assertEquals(copy.getAvailableTranslations(), result.getAvailableTranslations());
        assertEquals(copy.get(XX_LANG), result.get(XX_LANG));
        assertEquals(copy.get(YY_LANG), result.get(YY_LANG));
        assertEquals(copy.get(ZZ_LANG), result.get(ZZ_LANG));
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(I18nString)}.
     */
    @Test
    void testCopyConstructor_Fixed() {
        final I18nFixedString copy = I18nFixedString.from(MOCK_DEF_MSG);
        final I18nStringMap result = new I18nStringMap(copy);
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertNotNull(result.getAvailableTranslations());
        assertTrue(result.getAvailableTranslations().isEmpty());
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(I18nString)}.
     */
    @Test
    void testCopyConstructor_Resources() {
        final I18nResourcesString copy = spy(I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode("some code")
                .build());
        willReturn(MOCK_CONTEXT_MSG).given(copy).get();
        final I18nStringMap result = new I18nStringMap(copy);
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertNotNull(result.getAvailableTranslations());
        assertEquals(1, result.getAvailableTranslations().size());
        assertTrue(result.getAvailableTranslations().contains(CONTEXT_LANG));
        assertEquals(MOCK_CONTEXT_MSG, result.get(CONTEXT_LANG));
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(I18nString)}.
     */
    @Test
    void testCopyConstructor_IFace() {
        final I18nString mockI18nString = mock(I18nString.class);
        willReturn(MOCK_DEF_MSG).given(mockI18nString).get();
        final I18nStringMap result = new I18nStringMap(mockI18nString);
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertNotNull(result.getAvailableTranslations());
        assertTrue(result.getAvailableTranslations().isEmpty());
    }

    /**
     * Test {@link I18nStringMap#I18nStringMap(I18nString)}.
     */
    @Test
    void testCopyConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new I18nStringMap((I18nString) null);
        });
    }

    /**
     * Test {@link I18nStringMap#set(String, String)}.
     */
    @Test
    void testSet_Language() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        final I18nStringMap result = bean.set(XX_LANG, MOCK_XX_MSG);
        assertSame(bean, result);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(1, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
    }

    /**
     * Test {@link I18nStringMap#set(String, String)}.
     */
    @Test
    void testSet_Language_NullLanguage() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.set((String) null, MOCK_XX_MSG);
        });
    }

    /**
     * Test {@link I18nStringMap#set(String, String)}.
     */
    @Test
    void testSet_Language_NullText() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.set(XX_LANG, null);
        });
    }

    /**
     * Test {@link I18nStringMap#set(String, String)}.
     */
    @Test
    void testSet_Language_Multiple() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(3, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertTrue(bean.getAvailableTranslations().contains(YY_LANG));
        assertTrue(bean.getAvailableTranslations().contains(ZZ_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
        assertEquals(MOCK_YY_MSG, bean.get(YY_LANG));
        assertEquals(MOCK_YY_MSG, bean.get(YY_LOCALE));
        assertEquals(MOCK_ZZ_MSG, bean.get(ZZ_LANG));
        assertEquals(MOCK_ZZ_MSG, bean.get(ZZ_LOCALE));
    }

    /**
     * Test {@link I18nStringMap#set(Locale, String)}.
     */
    @Test
    void testSet_Locale() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        final I18nStringMap result = bean.set(XX_LOCALE, MOCK_XX_MSG);
        assertSame(bean, result);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(1, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
    }

    /**
     * Test {@link I18nStringMap#set(Locale, String)}.
     */
    @Test
    void testSet_Locale_NullLocale() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.set((Locale) null, MOCK_XX_MSG);
        });
    }

    /**
     * Test {@link I18nStringMap#set(Locale, String)}.
     */
    @Test
    void testSet_Locale_NullText() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.set(XX_LOCALE, null);
        });
    }

    /**
     * Test {@link I18nStringMap#set(Locale, String)}.
     */
    @Test
    void testSet_Locale_Multiple() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG)
                .set(YY_LOCALE, MOCK_YY_MSG)
                .set(ZZ_LOCALE, MOCK_ZZ_MSG);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(3, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertTrue(bean.getAvailableTranslations().contains(YY_LANG));
        assertTrue(bean.getAvailableTranslations().contains(ZZ_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
        assertEquals(MOCK_YY_MSG, bean.get(YY_LANG));
        assertEquals(MOCK_YY_MSG, bean.get(YY_LOCALE));
        assertEquals(MOCK_ZZ_MSG, bean.get(ZZ_LANG));
        assertEquals(MOCK_ZZ_MSG, bean.get(ZZ_LOCALE));
    }

    /**
     * Test {@link I18nResourcesString#get()}.
     */
    @Test
    void testGet() {
        final I18nStringMap bean = spy(new I18nStringMap(MOCK_DEF_MSG));
        willReturn(MOCK_CONTEXT_MSG).given(bean).get(CONTEXT_LOCALE);
        final String result = bean.get();
        assertEquals(MOCK_CONTEXT_MSG, result);
        then(bean).should().get(CONTEXT_LOCALE);
    }

    /**
     * Test {@link I18nResourcesString#get(String)}.
     */
    @Test
    void testGet_Language() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG);
        final String result = bean.get(XX_LANG);
        assertEquals(MOCK_XX_MSG, result);
    }

    /**
     * Test {@link I18nResourcesString#get(String)}.
     */
    @Test
    void testGet_Language_Null() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.get((String) null);
        });
    }

    /**
     * Test {@link I18nResourcesString#get(String)}.
     */
    @Test
    void testGet_Language_Missing() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG);
        final String result = bean.get(YY_LANG);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link I18nResourcesString#get(Locale)}.
     */
    @Test
    void testGet_Locale() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG);
        final String result = bean.get(XX_LOCALE);
        assertEquals(MOCK_XX_MSG, result);
    }

    /**
     * Test {@link I18nResourcesString#get(Locale)}.
     */
    @Test
    void testGet_Locale_Null() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.get((String) null);
        });
    }

    /**
     * Test {@link I18nResourcesString#get(Locale)}.
     */
    @Test
    void testGet_Locale_Missing() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG);
        final String result = bean.get(YY_LOCALE);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Language() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG)
                .set(YY_LOCALE, MOCK_YY_MSG);
        final I18nStringMap result = bean.remove(YY_LANG);
        assertSame(bean, result);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(1, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Language_Null() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG)
                .set(YY_LOCALE, MOCK_YY_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.remove((String) null);
        });
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Language_Multiple() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .remove(YY_LANG)
                .remove(XX_LANG);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertTrue(bean.getAvailableTranslations().isEmpty());
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Locale() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG)
                .set(YY_LOCALE, MOCK_YY_MSG);
        final I18nStringMap result = bean.remove(YY_LOCALE);
        assertSame(bean, result);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertEquals(1, bean.getAvailableTranslations().size());
        assertTrue(bean.getAvailableTranslations().contains(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LANG));
        assertEquals(MOCK_XX_MSG, bean.get(XX_LOCALE));
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Locale_Null() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LOCALE, MOCK_XX_MSG)
                .set(YY_LOCALE, MOCK_YY_MSG);
        assertThrows(NullPointerException.class, () -> {
            bean.remove((Locale) null);
        });
    }

    /**
     * Test {@link I18nStringMap#remove(Locale)}.
     */
    @Test
    void testRemove_Locale_Multiple() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .remove(YY_LOCALE)
                .remove(XX_LOCALE);
        assertEquals(MOCK_DEF_MSG, bean.getDefaultText());
        assertTrue(bean.getAvailableTranslations().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString#equals(Object)} and {@link I18nResourcesString#hashCode()}.
     */
    @Test
    void testEqualsHash() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        assertNotEquals(bean, (Object) null);
        assertEquals(bean, bean);
        assertEquals(bean.hashCode(), bean.hashCode());
        assertNotEquals(bean, new Object());
        assertNotEquals(bean, I18nFixedString.from(MOCK_DEF_MSG));
        final I18nString mockI18nString = mock(I18nString.class);
        willReturn(MOCK_DEF_MSG).given(mockI18nString).get();
        assertNotEquals(bean, mockI18nString);
        then(mockI18nString).shouldHaveNoInteractions();
        final I18nStringMap other = new I18nStringMap(bean);
        assertEquals(bean, other);
        assertEquals(bean.hashCode(), other.hashCode());
        other.set(XX_LANG, MOCK_XX_MSG);
        other.set(YY_LOCALE, MOCK_YY_MSG);
        assertNotEquals(bean, other);
        bean.set(XX_LANG, "not same");
        assertNotEquals(bean, other);
        bean.set(XX_LANG, MOCK_XX_MSG);
        assertNotEquals(bean, other);
        other.remove(YY_LANG);
        assertEquals(bean, other);
        assertEquals(bean.hashCode(), other.hashCode());
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(CONTEXT_LANG, MOCK_CONTEXT_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        assertFalse(bean.isEquivalent(null));
        assertTrue(bean.isEquivalent(bean));
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent_I18nString_Other() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(CONTEXT_LANG, MOCK_CONTEXT_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        assertFalse(bean.isEquivalent(I18nFixedString.from(MOCK_DEF_MSG)));
        assertTrue(bean.isEquivalent(I18nFixedString.from(MOCK_CONTEXT_MSG)));
        final I18nString mockI18nString = mock(I18nString.class);
        willReturn(MOCK_CONTEXT_MSG).given(mockI18nString).get();
        assertTrue(bean.isEquivalent(mockI18nString));
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
        final I18nString mockI18nString2 = mock(I18nString.class);
        willReturn(MOCK_DEF_MSG).given(mockI18nString2).get();
        assertFalse(bean.isEquivalent(mockI18nString2));
        then(mockI18nString2).should().get();
        then(mockI18nString2).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent_I18nStringMap() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG);
        final I18nStringMap other = new I18nStringMap(MOCK_DEF_MSG);
        assertTrue(bean.isEquivalent(other));
        bean.set(CONTEXT_LANG, MOCK_CONTEXT_MSG);
        other.set(CONTEXT_LANG, "other text");
        assertFalse(bean.isEquivalent(other));
        other.set(CONTEXT_LANG, MOCK_CONTEXT_MSG);
        assertTrue(bean.isEquivalent(other));
        bean.set(XX_LANG, MOCK_XX_MSG);
        assertFalse(bean.isEquivalent(other));
    }

    /**
     * Test {@link I18nResourcesString#toString()}.
     */
    @Test
    void testToString() {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        assertEquals(MOCK_DEF_MSG, bean.toString());
    }

    /**
     * Test {@link java.io.Serializable} implementation.
     */
    @Test
    void testSerializable() throws IOException, ClassNotFoundException {
        final I18nStringMap bean = new I18nStringMap(MOCK_DEF_MSG)
                .set(XX_LANG, MOCK_XX_MSG)
                .set(YY_LANG, MOCK_YY_MSG)
                .set(ZZ_LANG, MOCK_ZZ_MSG);
        final byte[] serializationResult;
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream out = new ObjectOutputStream(baos)){
            out.writeObject(bean);
            serializationResult = baos.toByteArray();
        }
        assertNotNull(serializationResult);
        final I18nStringMap result;
        try (
                final ByteArrayInputStream bais = new ByteArrayInputStream(serializationResult);
                final ObjectInputStream in = new ObjectInputStream(bais)){
            result = (I18nStringMap) in.readObject();
        }
        assertNotNull(result);
        assertEquals(bean, result);
        assertEquals(bean.hashCode(), result.hashCode());
    }
}
