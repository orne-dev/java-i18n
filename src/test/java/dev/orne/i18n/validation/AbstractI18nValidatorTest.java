package dev.orne.i18n.validation;

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
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;

/**
 * Abstract unit tests for {@code I18nString} {@code ConstraintValidator}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @param <T> The constraint type validated
 * @see ConstraintValidator
 * @since 0.1
 */
@Tag("ut")
abstract class AbstractI18nValidatorTest<T extends Annotation> {

    private final @NotNull Class<T> annotationType;
    private final @NotNull Class<? extends ConstraintValidator<T, I18nString>> type;
    protected int textSize = 10;
    protected int mapTranslationsSize = 5;

    protected @Mock I18nString mockI18nString;
    protected @Mock I18nStringMap mockI18nStringMap;
    protected @Mock ConstraintValidatorContext mockContext;
    protected T mockAnnotation;
    protected AutoCloseable mocks;

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
        mockAnnotation = mock(annotationType);
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    protected AbstractI18nValidatorTest(
            final @NotNull Class<T> annotationType,
            final @NotNull Class<? extends ConstraintValidator<T, I18nString>> type) {
        super();
        this.annotationType = Validate.notNull(annotationType);
        this.type = Validate.notNull(type);
    }

    /**
     * Test empty constructor.
     */
    @Test
    void testConstructor() {
        final ConstraintValidator<T, I18nString> result = assertDoesNotThrow(() -> {
            return this.type.newInstance();
        });
        assertNotNull(result);
    }

    /**
     * Test {@link ConstraintValidator#initialize(Annotation)}.
     */
    @Test
    void testInitialize()
    throws Exception {
        assertInitialize();
    }

    /**
     * Test {@link ConstraintValidator#initialize(Annotation)}.
     */
    ConstraintValidator<T, I18nString> assertInitialize() {
        final ConstraintValidator<T, I18nString> validator = assertDoesNotThrow(() -> {
            return this.type.newInstance();
        });
        assertDoesNotThrow(() -> {
            validator.initialize(mockAnnotation);
        });
        return validator;
    }

    protected ConstraintValidator<T, I18nString> createValidator() {
        final ConstraintValidator<T, I18nString> validator = assertDoesNotThrow(() -> {
            return this.type.newInstance();
        });
        validator.initialize(mockAnnotation);
        return validator;
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} with null value.
     * 
     * @param valid If the value should be valid
     */
    protected void assertNullValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final boolean result = validator.isValid(null, mockContext);
        assertEquals(valid, result);
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} with empty value.
     * 
     * @param valid If the value should be valid
     */
    protected void assertEmptyValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        willReturn("").given(mockI18nString).get();
        final boolean result = validator.isValid(mockI18nString, mockContext);
        assertEquals(valid, result);
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} with blank value.
     * 
     * @param valid If the value should be valid
     */
    protected void assertBlankValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = StringUtils.leftPad("", textSize);
        willReturn(text).given(mockI18nString).get();
        final boolean result = validator.isValid(mockI18nString, mockContext);
        assertEquals(valid, result);
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} with random value.
     * 
     * @param valid If the value should be valid
     */
    protected void assertRandomValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = RandomStringUtils.random(textSize);
        willReturn(text).given(mockI18nString).get();
        final boolean result = validator.isValid(mockI18nString, mockContext);
        assertEquals(valid, result);
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    protected @NotNull Map<@NotNull String, @NotNull String> generateRandomTranslations() {
        final Map<String, String> result = new HashMap<>();
        for (int i = 0; i < this.mapTranslationsSize; i++) {
            result.put(RandomStringUtils.randomAlphabetic(2), RandomStringUtils.random(textSize));
        }
        return result;
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
     * with {@code I18nStringMap} value with empty default text.
     * 
     * @param valid If the value should be valid
     */
    protected void assertMapEmptyDefaultValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final Map<String, String> originalTranslations = generateRandomTranslations();
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        willReturn("").given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertEquals(valid, result);
        then(mockI18nStringMap).should(never()).setDefaultText(any());
        then(mockI18nStringMap).should(never()).setI18n(any());
        then(mockI18nStringMap).should(never()).set(any(Locale.class), any());
        then(mockI18nStringMap).should(never()).set(any(String.class), any());
        then(mockI18nStringMap).should(never()).remove(any(Locale.class));
        then(mockI18nStringMap).should(never()).remove(any(String.class));
        assertEquals(originalTranslations, translations);
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
     * with {@code I18nStringMap} value with blank default text.
     * 
     * @param valid If the value should be valid
     */
    protected void assertMapBlankDefaultValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = StringUtils.leftPad("", textSize);
        final Map<String, String> originalTranslations = generateRandomTranslations();
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertEquals(valid, result);
        then(mockI18nStringMap).should(never()).setDefaultText(any());
        then(mockI18nStringMap).should(never()).setI18n(any());
        then(mockI18nStringMap).should(never()).set(any(Locale.class), any());
        then(mockI18nStringMap).should(never()).set(any(String.class), any());
        then(mockI18nStringMap).should(never()).remove(any(Locale.class));
        then(mockI18nStringMap).should(never()).remove(any(String.class));
        assertEquals(originalTranslations, translations);
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
     * with {@code I18nStringMap} value with empty translation.
     * 
     * @param valid If the value should be valid
     */
    protected void assertMapEmptyTranslationValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = RandomStringUtils.random(textSize);
        final Map<String, String> originalTranslations = generateRandomTranslations();
        originalTranslations.put(RandomStringUtils.randomAlphabetic(2), "");
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertEquals(valid, result);
        then(mockI18nStringMap).should(never()).setDefaultText(any());
        then(mockI18nStringMap).should(never()).setI18n(any());
        then(mockI18nStringMap).should(never()).set(any(Locale.class), any());
        then(mockI18nStringMap).should(never()).set(any(String.class), any());
        then(mockI18nStringMap).should(never()).remove(any(Locale.class));
        then(mockI18nStringMap).should(never()).remove(any(String.class));
        assertEquals(originalTranslations, translations);
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
     * with {@code I18nStringMap} value with blank translation.
     * 
     * @param valid If the value should be valid
     */
    protected void assertMapBlankTranslationValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = RandomStringUtils.random(textSize);
        final Map<String, String> originalTranslations = generateRandomTranslations();
        originalTranslations.put(RandomStringUtils.randomAlphabetic(2), StringUtils.leftPad("", textSize));
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertEquals(valid, result);
        then(mockI18nStringMap).should(never()).setDefaultText(any());
        then(mockI18nStringMap).should(never()).setI18n(any());
        then(mockI18nStringMap).should(never()).set(any(Locale.class), any());
        then(mockI18nStringMap).should(never()).set(any(String.class), any());
        then(mockI18nStringMap).should(never()).remove(any(Locale.class));
        then(mockI18nStringMap).should(never()).remove(any(String.class));
        assertEquals(originalTranslations, translations);
    }

    /**
     * Test {@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)}
     * with {@code I18nStringMap} value with random values.
     * 
     * @param valid If the value should be valid
     */
    protected void assertMapRandomValidation(
            final boolean valid) {
        final ConstraintValidator<T, I18nString> validator = createValidator();
        final String text = RandomStringUtils.random(textSize);
        final Map<String, String> originalTranslations = generateRandomTranslations();
        final Map<String, String> translations = new HashMap<>(originalTranslations);
        willReturn(text).given(mockI18nStringMap).getDefaultText();
        willReturn(translations).given(mockI18nStringMap).getI18n();
        final boolean result = validator.isValid(mockI18nStringMap, mockContext);
        assertEquals(valid, result);
        then(mockI18nStringMap).should(never()).setDefaultText(any());
        then(mockI18nStringMap).should(never()).setI18n(any());
        then(mockI18nStringMap).should(never()).set(any(Locale.class), any());
        then(mockI18nStringMap).should(never()).set(any(String.class), any());
        then(mockI18nStringMap).should(never()).remove(any(Locale.class));
        then(mockI18nStringMap).should(never()).remove(any(String.class));
        assertEquals(originalTranslations, translations);
    }
}
