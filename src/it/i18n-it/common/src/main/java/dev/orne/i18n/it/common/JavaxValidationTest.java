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

import java.lang.annotation.Annotation;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nResourcesString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.context.ContextTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern.Flag;

/**
 * Integration tests for {@code I18nString} validation with Hibernate
 * implementation of JavaX Bean Validation.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @since 0.1
 */
@Tag("javax-validation")
public class JavaxValidationTest {

    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeAll
    static void configureI18N() {
        TestMessages.configureI18N();
    }

    @AfterAll
    static void restoreDefaultStrategy() {
        ContextTestUtils.reset();
    }

    @Test
    void testNull() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        assertInvalid(container, NotNull.class, I18nStringValidationContainer.ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testEmptyFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testBlankFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("     ");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testShortFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("xx");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testLongFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("A valid long value");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testInvalidFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("Won't pass");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testValidFixed() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nFixedString.from("VALID");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testEmptyResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.EMPTY)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testBlankResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.BLANK)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testShortResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.SHORT)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testLongResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.LONG)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testInvalidResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.INVALID)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testValidResources() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = I18nResourcesString.forDefault("")
                .withCode(TestMessages.Entries.VALID)
                .build();
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testEmptyMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("")
                .set("xx", "this is valid");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testBlankMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("     ")
                .set("xx", "this is valid");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "     ")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testShortMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("no")
                .set("xx", "this is valid");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "NO")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testLongMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("Too long to be valid")
                .set("xx", "this is valid");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "Too long to be valid")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertInvalid(container, Size.class, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testInvalidMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("Won't pass")
                .set("xx", "this is valid");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "Won't pass")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertInvalid(container, Pattern.class, I18nStringValidationContainer.ValidatePattern.class);
    }

    @Test
    void testValidMap() {
        final I18nStringValidationContainer container = new I18nStringValidationContainer();
        container.bean = new I18nStringMap("VALID")
                .set("xx", "this is valid")
                .set("yy", "valid too");
        assertValid(container, I18nStringValidationContainer.ValidateNotNull.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotEmpty.class);
        assertValid(container, I18nStringValidationContainer.ValidateNotBlank.class);
        assertValid(container, I18nStringValidationContainer.ValidateSize.class);
        assertValid(container, I18nStringValidationContainer.ValidatePattern.class);
    }

    private static void assertValid(
            final I18nStringValidationContainer container,
            final Class<?>... groups) {
        final Set<ConstraintViolation<I18nStringValidationContainer>> result = validator.validate(container, groups);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private static Set<ConstraintViolation<I18nStringValidationContainer>> assertInvalid(
            final I18nStringValidationContainer container,
            final Class<?>... groups) {
        final Set<ConstraintViolation<I18nStringValidationContainer>> result = validator.validate(container, groups);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        return result;
    }

    private static ConstraintViolation<I18nStringValidationContainer> assertInvalid(
            final I18nStringValidationContainer container,
            final Class<? extends Annotation> constraint,
            final Class<?>... groups) {
        final Set<ConstraintViolation<I18nStringValidationContainer>> errors = assertInvalid(container, groups);
        assertEquals(1, errors.size());
        ConstraintViolation<I18nStringValidationContainer> result = null;
        for (final ConstraintViolation<I18nStringValidationContainer> error : errors) {
            if (constraint.isInstance(error.getConstraintDescriptor().getAnnotation())) {
                result = error;
            }
        }
        assertNotNull(result);
        return result;
    }

    /**
     * {@code I18nString} validation test container.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @since 0.1
     */
    public static class I18nStringValidationContainer {

        @NotNull(groups = ValidateNotNull.class)
        @NotEmpty(groups = ValidateNotEmpty.class)
        @NotBlank(groups = ValidateNotBlank.class)
        @Size(min=5, max=15, groups = ValidateSize.class)
        @Pattern(regexp = ".*valid.*", flags = { Flag.CASE_INSENSITIVE }, groups = ValidatePattern.class)
        public I18nString bean;

        public static interface ValidateNotNull {}
        public static interface ValidateNotEmpty {}
        public static interface ValidateNotBlank {}
        public static interface ValidateSize {}
        public static interface ValidatePattern {}
    }
}
