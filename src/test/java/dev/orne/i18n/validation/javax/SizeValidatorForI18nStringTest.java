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

import javax.validation.constraints.Size;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nString;

/**
 * Unit tests for {@code SizeValidatorForI18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see SizeValidatorForI18nString
 */
@Tag("ut")
class SizeValidatorForI18nStringTest
extends AbstractI18nValidatorTest<Size> {

    public SizeValidatorForI18nStringTest() {
        super(Size.class, SizeValidatorForI18nString.class);
    }

    /**
     * Test {@link SizeValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize()
    throws Exception {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        super.testInitialize();
    }

    /**
     * Test {@link SizeValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize_NegativeMin()
    throws Exception {
        willReturn(-1).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        assertThrows(IllegalArgumentException.class, () -> {
            validator.initialize(mockAnnotation);
        });
    }

    /**
     * Test {@link SizeValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize_NegativeMax()
    throws Exception {
        willReturn(5).given(mockAnnotation).min();
        willReturn(-1).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        assertThrows(IllegalArgumentException.class, () -> {
            validator.initialize(mockAnnotation);
        });
    }

    /**
     * Test {@link SizeValidatorForI18nString#initialize(Annotation)}.
     */
    @Test
    void testInitialize_MinGreaterThanMax()
    throws Exception {
        willReturn(10).given(mockAnnotation).min();
        willReturn(5).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        assertThrows(IllegalArgumentException.class, () -> {
            validator.initialize(mockAnnotation);
        });
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Null() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertNullValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Empty() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertEmptyValidation(false);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Blank() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertBlankValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertRandomValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Empty() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertMapEmptyDefaultValidation(false);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Blank() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertMapBlankDefaultValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Empty() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertMapEmptyTranslationValidation(false);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Blank() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertMapBlankTranslationValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        assertMapRandomValidation(true);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Null() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        assertThrows(NullPointerException.class, () -> {
            validator.isTextValid(null);
        });
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Empty() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid("");
        assertFalse(result);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Blank() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                StringUtils.leftPad("", textSize));
        assertTrue(result);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Min() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                RandomStringUtils.random(2));
        assertFalse(result);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Max() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                RandomStringUtils.random(20));
        assertFalse(result);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Random() {
        willReturn(5).given(mockAnnotation).min();
        willReturn(15).given(mockAnnotation).max();
        final SizeValidatorForI18nString validator = new SizeValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                RandomStringUtils.random(textSize));
        assertTrue(result);
    }
}
