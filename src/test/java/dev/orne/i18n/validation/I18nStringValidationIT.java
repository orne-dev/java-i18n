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

import java.lang.annotation.Annotation;
import java.util.ResourceBundle;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.DefaultI18nContextProvider;
import dev.orne.i18n.DefaultI18nContextProviderStrategy;
import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nBundleResources;
import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nResourcesString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;

/**
 * Integration tests for {@code I18nString} validation with Hibernate
 * implementation of Jackarta Bean Validation.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 */
@Tag("it")
class I18nStringValidationIT {

    private static Validator validator;

    @BeforeAll
    static void createValidator() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeAll
    static void configureI18N() {
        final ResourceBundle bundle = ResourceBundle.getBundle("dev/orne/i18n/test-messages");
        final I18nBundleResources resources = new I18nBundleResources(bundle);
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider();
        provider.setDefaultI18nResources(resources);
        final DefaultI18nContextProviderStrategy strategy = new DefaultI18nContextProviderStrategy(provider);
        I18N.setContextProviderStrategy(strategy);
    }

    @AfterAll
    static void restoreDefaultStrategy() {
        I18N.reconfigure();
    }

    @Test
    void testNull() {
        final Container container = new Container();
        assertInvalid(container, NotNull.class, ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testEmptyFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("");
        assertValid(container, ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testBlankFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("     ");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testShortFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("xx");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testLongFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("A valid long value");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testInvalidFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("Won't pass");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testValidFixed() {
        final Container container = new Container();
        container.bean = I18nFixedString.from("VALID");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testEmptyResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.empty")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testBlankResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.blank")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testShortResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.short")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testLongResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.long")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testInvalidResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.invalid")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testValidResources() {
        final Container container = new Container();
        container.bean = I18nResourcesString.forDefault("")
                .withCode("dev.orne.i18n.test.valid")
                .build();
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testEmptyMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("")
                .set("xx", "this is valid");
        assertValid(container, ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertInvalid(container, NotEmpty.class, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testBlankMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("     ")
                .set("xx", "this is valid");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "     ")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertInvalid(container, NotBlank.class, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testShortMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("no")
                .set("xx", "this is valid");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "NO")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testLongMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("Too long to be valid")
                .set("xx", "this is valid");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "Too long to be valid")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertInvalid(container, Size.class, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    @Test
    void testInvalidMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("Won't pass")
                .set("xx", "this is valid");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
        container.bean = new I18nStringMap("this is valid")
                .set("xx", "Won't pass")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertInvalid(container, Pattern.class, ValidatePattern.class);
    }

    @Test
    void testValidMap() {
        final Container container = new Container();
        container.bean = new I18nStringMap("VALID")
                .set("xx", "this is valid")
                .set("yy", "valid too");
        assertValid(container, ValidateNotNull.class);
        assertValid(container, ValidateNotEmpty.class);
        assertValid(container, ValidateNotBlank.class);
        assertValid(container, ValidateSize.class);
        assertValid(container, ValidatePattern.class);
    }

    private static void assertValid(
            final Container container,
            final Class<?>... groups) {
        final Set<ConstraintViolation<Container>> result = validator.validate(container, groups);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private static Set<ConstraintViolation<Container>> assertInvalid(
            final Container container,
            final Class<?>... groups) {
        final Set<ConstraintViolation<Container>> result = validator.validate(container, groups);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        return result;
    }

    private static ConstraintViolation<Container> assertInvalid(
            final Container container,
            final Class<? extends Annotation> constraint,
            final Class<?>... groups) {
        final Set<ConstraintViolation<Container>> errors = assertInvalid(container, groups);
        assertEquals(1, errors.size());
        ConstraintViolation<Container> result = null;
        for (final ConstraintViolation<Container> error : errors) {
            if (constraint.isInstance(error.getConstraintDescriptor().getAnnotation())) {
                result = error;
            }
        }
        assertNotNull(result);
        return result;
    }

    static class Container {
        @NotNull(groups = ValidateNotNull.class)
        @NotEmpty(groups = ValidateNotEmpty.class)
        @NotBlank(groups = ValidateNotBlank.class)
        @Size(min=5, max=15, groups = ValidateSize.class)
        @Pattern(regexp = ".*valid.*", flags = { Flag.CASE_INSENSITIVE } , groups = ValidatePattern.class)
        private I18nString bean;
    }

    static interface ValidateNotNull {}
    static interface ValidateNotEmpty {}
    static interface ValidateNotBlank {}
    static interface ValidateSize {}
    static interface ValidatePattern {}
}
