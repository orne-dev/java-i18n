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

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nString;

/**
 * Unit tests for {@code NotBlankValidatorForI18nString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see NotBlankValidatorForI18nString
 */
@Tag("ut")
@SuppressWarnings("deprecation")
class NotBlankValidatorForI18nStringTest
extends AbstractI18nValidatorTest<NotBlank> {

    public NotBlankValidatorForI18nStringTest() {
        super(NotBlank.class, NotBlankValidatorForI18nString.class);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Null() {
        assertNullValidation(false);
    }

    /**
     * Test {@link SizeValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Empty() {
        assertEmptyValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Blank() {
        assertBlankValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid() {
        assertRandomValidation(true);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Empty() {
        assertMapEmptyDefaultValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Default_Blank() {
        assertMapBlankDefaultValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Empty() {
        assertMapEmptyTranslationValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map_Translation_Blank() {
        assertMapBlankTranslationValidation(false);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isValid(I18nString, ConstraintValidatorContext)}.
     */
    @Test
    void testIsValid_Map() {
        assertMapRandomValidation(true);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Null() {
        final NotBlankValidatorForI18nString validator = new NotBlankValidatorForI18nString();
        validator.initialize(mockAnnotation);
        assertThrows(NullPointerException.class, () -> {
            validator.isTextValid(null);
        });
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Empty() {
        final NotBlankValidatorForI18nString validator = new NotBlankValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid("");
        assertFalse(result);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Blank() {
        final NotBlankValidatorForI18nString validator = new NotBlankValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                StringUtils.leftPad("", textSize));
        assertFalse(result);
    }

    /**
     * Test {@link NotBlankValidatorForI18nString#isTextValid(String)}.
     */
    @Test
    void testIsTextValid_Random() {
        final NotBlankValidatorForI18nString validator = new NotBlankValidatorForI18nString();
        validator.initialize(mockAnnotation);
        final boolean result = validator.isTextValid(
                RandomStringUtils.random(textSize));
        assertTrue(result);
    }
}
