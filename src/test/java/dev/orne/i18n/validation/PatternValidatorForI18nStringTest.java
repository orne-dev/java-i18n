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

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nString;

/**
 * Unit tests for {@code PatternValidatorForI18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see PatternValidatorForI18nString
 */
@Tag("ut")
class PatternValidatorForI18nStringTest
extends AbstractI18nValidatorTest<Pattern> {

    private static final String TEST_REGEXP = "\\S+";

    public PatternValidatorForI18nStringTest() {
        super(Pattern.class, PatternValidatorForI18nString.class);
    }

    /**
     * Test {@link PatternValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize()
    throws Exception {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        super.testInitialize();
    }

    /**
     * Test {@link PatternValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize_InvalidRegExp()
    throws Exception {
        willReturn("[").given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        final PatternValidatorForI18nString validator = new PatternValidatorForI18nString();
        assertThrows(IllegalArgumentException.class, () -> {
            validator.initialize(mockAnnotation);
        });
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Null() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertNullValidation(true);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Empty() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertEmptyValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Blank() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertBlankValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertRandomValidation(true);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Empty() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertMapEmptyDefaultValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Blank() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertMapBlankDefaultValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Empty() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertMapEmptyTranslationValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Blank() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertMapBlankTranslationValidation(false);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        assertMapRandomValidation(true);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Null() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        final PatternValidatorForI18nString validator = new PatternValidatorForI18nString();
        validator.initialize(mockAnnotation);
        assertThrows(NullPointerException.class, () -> {
            validator.isTextValid(null);
        });
    }

    /**
     * Test {@link PatternValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Digits() {
        willReturn("\\d+").given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        final PatternValidatorForI18nString validator = new PatternValidatorForI18nString();
        validator.initialize(mockAnnotation);
        boolean result = validator.isTextValid("");
        assertFalse(result);
        result = validator.isTextValid("   ");
        assertFalse(result);
        result = validator.isTextValid("some text");
        assertFalse(result);
        result = validator.isTextValid("1597523");
        assertTrue(result);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_CaseInsensitive() {
        willReturn("some.*").given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[] {
                Pattern.Flag.CASE_INSENSITIVE
        }).given(mockAnnotation).flags();
        final PatternValidatorForI18nString validator = new PatternValidatorForI18nString();
        validator.initialize(mockAnnotation);
        boolean result = validator.isTextValid("");
        assertFalse(result);
        result = validator.isTextValid("   ");
        assertFalse(result);
        result = validator.isTextValid("some");
        assertTrue(result);
        result = validator.isTextValid("some text");
        assertTrue(result);
        result = validator.isTextValid("1597523");
        assertFalse(result);
        result = validator.isTextValid("SOME");
        assertTrue(result);
        result = validator.isTextValid("SOME TEXT");
        assertTrue(result);
        result = validator.isTextValid("SoMe");
        assertTrue(result);
        result = validator.isTextValid("SoMe TEXT");
        assertTrue(result);
    }

    /**
     * Test {@link PatternValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Random() {
        willReturn(TEST_REGEXP).given(mockAnnotation).regexp();
        willReturn(new Pattern.Flag[0]).given(mockAnnotation).flags();
        final PatternValidatorForI18nString validator = new PatternValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                RandomStringUtils.random(textSize));
        assertTrue(result);
    }
}
