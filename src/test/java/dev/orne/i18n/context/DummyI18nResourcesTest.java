package dev.orne.i18n.context;

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

import java.util.Locale;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code DummyI18nResources}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see DummyI18nResources
 */
@Tag("ut")
class DummyI18nResourcesTest {

    private static final String MOCK_DEF_MSG = "mock default message";
    private static final String MOCK_DEF_MSG_TMPL = "mock default message: {0} {2}";
    private static final String MOCK_DEF_MSG_INV_TMPL = "mock default message: {";
    private static final String MOCK_DEF_MSG_TMPL_RESULT = "mock default message: arg1 arg3";
    private static final String MOCK_MSG_CODE = "mock message code";
    private static final String MOCK_MSG_CODE_2 = "mock message code 2";
    private static final String MOCK_MSG_CODE_3 = "mock message code 3";
    private static final String[] CODES = {
            MOCK_MSG_CODE,
            MOCK_MSG_CODE_2,
            MOCK_MSG_CODE_3
    };
    private static final Object[] ARGS = {
            "arg1",
            "arg2",
            "arg3"
    };
    private static final Locale MOCK_DEFAULT_LOCALE = new Locale("xx");

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Object...)}.
     */
    @Test
    void testGetMessage() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG,
                MOCK_MSG_CODE,
                ARGS);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Object...)}.
     */
    @Test
    void testGetMessage_Formatted() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_TMPL,
                MOCK_MSG_CODE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Object...)}.
     */
    @Test
    void testGetMessage_InvalidTemplate() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_INV_TMPL,
                MOCK_MSG_CODE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Object...)}.
     */
    @Test
    void testGetMessage_Codes() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG,
                CODES,
                ARGS);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Object...)}.
     */
    @Test
    void testGetMessage_Codes_Formatted() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_TMPL,
                CODES,
                ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Object...)}.
     */
    @Test
    void testGetMessage_Codes_InvalidTemplate() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_INV_TMPL,
                CODES,
                ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG,
                MOCK_MSG_CODE,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale_Formatted() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_TMPL,
                MOCK_MSG_CODE,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String, Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale_InvalidTemplate() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_INV_TMPL,
                MOCK_MSG_CODE,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale_Codes() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG,
                CODES,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale_Codes_Formatted() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_TMPL,
                CODES,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
    }

    /**
     * Test {@link DummyI18nResources#getMessage(String, String[], Locale, Object...)}.
     */
    @Test
    void testGetMessage_Locale_Codes_InvalidTemplate() {
        final String result = DummyI18nResources.getInstance().getMessage(
                MOCK_DEF_MSG_INV_TMPL,
                CODES,
                MOCK_DEFAULT_LOCALE,
                ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
    }
}
