package dev.orne.i18n;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
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

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-03
 * @since 0.1
 * @see I18nConfigurationException
 */
@Tag("ut")
class ExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();

    /**
     * Test for {@link I18nConfigurationException}.
     */
    @Test
    void testI18nConfigurationException() {
        assertEmptyException(new I18nConfigurationException());
        assertMessageException(new I18nConfigurationException(TEST_MESSAGE));
        assertCauseException(new I18nConfigurationException(TEST_CAUSE));
        assertFullException(new I18nConfigurationException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new I18nConfigurationException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Asserts that exception has no message and no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test.
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertEmptyException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has message but no cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertMessageException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has cause but no message.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertCauseException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_CAUSE.toString(), exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }

    /**
     * Asserts that exception has message and cause.
     * 
     * @param <T> The exception type.
     * @param exception The exception to test
     * @return The tested exception, for extra verifications.
     */
    private <T extends Exception> @NotNull T assertFullException(
            final @NotNull T exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
        return exception;
    }
}
