package dev.orne.i18n.validation.javax;

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

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;

/**
 * Unit tests for {@code AbstractValidatorForI18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see AbstractValidatorForI18nString
 */
@Tag("ut")
class AbstractValidatorForI18nStringTest {

    private static final int RND_STR_LENGTH = 20;

    private @Mock I18nString mockI18nString;
    private @Mock I18nStringMap mockI18nStringMap;
    private @Mock ConstraintValidatorContext mockContext;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test {@link AbstractValidatorForI18nString#AbstractValidatorForI18nString()}.
     */
    @Test
    void testConstructor() {
        final MockImpl result = new MockImpl();
        assertNotNull(result);
    }

    /**
     * Test {@link AbstractValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize() {
        final MockImpl result = spy(new MockImpl());
        final Annotation mockAnnotation = mock(Annotation.class);
        result.initialize(mockAnnotation);
        then(mockAnnotation).shouldHaveNoInteractions();
        then(result).should().initialize(mockAnnotation);
        then(result).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void testIsValid(final boolean valid) {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final MockImpl validator = spy(new MockImpl());
        willReturn(text).given(mockI18nString).get();
        willReturn(valid).given(validator).isTextValid(text);
        final boolean result = validator.isValid(mockI18nString, mockContext);
        assertEquals(valid, result);
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(mockI18nString, mockContext);
        then(validator).should().isTextValid(text);
        then(validator).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Null() {
        final MockImpl validator = spy(new MockImpl());
        final boolean result = validator.isValid(null, mockContext);
        assertTrue(result);
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(null, mockContext);
        then(validator).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, String> originalTranslations = new HashMap<>();
        originalTranslations.put("xx", xxText);
        originalTranslations.put("yy", yyText);
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        final MockImpl validator = spy(new MockImpl());
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        willReturn(true).given(validator).isTextValid(text);
        willReturn(true).given(validator).isTextValid(xxText);
        willReturn(true).given(validator).isTextValid(yyText);
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertTrue(result);
        assertEquals(originalTranslations, translations);
        then(mockI18nStringMap).should().getDefaultText();
        then(mockI18nStringMap).should().getI18n();
        then(mockI18nStringMap).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(mockI18nStringMap, mockContext);
        then(validator).should().isTextValid(text);
        then(validator).should().isTextValid(xxText);
        then(validator).should().isTextValid(yyText);
        then(validator).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_DefaultInvalid() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, String> originalTranslations = new HashMap<>();
        originalTranslations.put("xx", xxText);
        originalTranslations.put("yy", yyText);
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        final MockImpl validator = spy(new MockImpl());
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        willReturn(false).given(validator).isTextValid(text);
        willReturn(true).given(validator).isTextValid(xxText);
        willReturn(true).given(validator).isTextValid(yyText);
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertFalse(result);
        assertEquals(originalTranslations, translations);
        then(mockI18nStringMap).should().getDefaultText();
        then(mockI18nStringMap).should(atLeast(0)).getI18n();
        then(mockI18nStringMap).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(mockI18nStringMap, mockContext);
        then(validator).should().isTextValid(text);
        then(validator).should(atMostOnce()).isTextValid(xxText);
        then(validator).should(atMostOnce()).isTextValid(yyText);
        then(validator).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_TranslationInvalid() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, String> originalTranslations = new HashMap<>();
        originalTranslations.put("xx", xxText);
        originalTranslations.put("yy", yyText);
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        final MockImpl validator = spy(new MockImpl());
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        willReturn(true).given(validator).isTextValid(text);
        willReturn(false).given(validator).isTextValid(xxText);
        willReturn(true).given(validator).isTextValid(yyText);
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertFalse(result);
        assertEquals(originalTranslations, translations);
        then(mockI18nStringMap).should().getDefaultText();
        then(mockI18nStringMap).should(atLeast(0)).getI18n();
        then(mockI18nStringMap).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(mockI18nStringMap, mockContext);
        then(validator).should(atMostOnce()).isTextValid(text);
        then(validator).should().isTextValid(xxText);
        then(validator).should(atMostOnce()).isTextValid(yyText);
        then(validator).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link AbstractValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_TranslationInvalid2() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final Map<String, String> originalTranslations = new HashMap<>();
        originalTranslations.put("xx", xxText);
        originalTranslations.put("yy", yyText);
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        final MockImpl validator = spy(new MockImpl());
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        willReturn(true).given(validator).isTextValid(text);
        willReturn(true).given(validator).isTextValid(xxText);
        willReturn(false).given(validator).isTextValid(yyText);
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertFalse(result);
        assertEquals(originalTranslations, translations);
        then(mockI18nStringMap).should().getDefaultText();
        then(mockI18nStringMap).should(atLeast(0)).getI18n();
        then(mockI18nStringMap).shouldHaveNoMoreInteractions();
        then(mockContext).shouldHaveNoInteractions();
        then(validator).should().isValid(mockI18nStringMap, mockContext);
        then(validator).should(atMostOnce()).isTextValid(text);
        then(validator).should(atMostOnce()).isTextValid(xxText);
        then(validator).should().isTextValid(yyText);
        then(validator).shouldHaveNoMoreInteractions();
    }

    class MockImpl
    extends AbstractValidatorForI18nString<Annotation> {
        @Override
        protected boolean isTextValid(@NotNull String text) {
            // NOP
            return false;
        }
    }
}
