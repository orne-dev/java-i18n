package dev.orne.i18n.spring;

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

import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nContext;
import dev.orne.i18n.I18nContextProvider;
import dev.orne.i18n.I18nContextProviderStrategy;

/**
 * Unit tests for {@code I18nSpringResources}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringResources
 */
@Tag("ut")
class I18nSpringResourcesTest {

    private static final String MOCK_DEF_MSG = "mock default message";
    private static final String MOCK_DEF_MSG_TMPL = "mock default message: {0} {2}";
    private static final String MOCK_DEF_MSG_INV_TMPL = "mock default message: {";
    private static final String MOCK_DEF_MSG_TMPL_RESULT = "mock default message: arg1 arg3";
    private static final String MOCK_MSG_CODE = "mock message code";
    private static final String MOCK_MSG_CODE_2 = "mock message code 2";
    private static final String MOCK_MSG_CODE_3 = "mock message code 3";
    private static final String MOCK_MSG = "mock message ";
    private static final Object MOCK_PARAM = "arg1";
    private static final Object MOCK_PARAM_2 = new Object();
    private static final Object MOCK_PARAM_3 = "arg3";
    private static final String[] CODES = {
            MOCK_MSG_CODE,
            MOCK_MSG_CODE_2,
            MOCK_MSG_CODE_3
    };
    private static final Object[] ARGS = {
            MOCK_PARAM,
            MOCK_PARAM_2,
            MOCK_PARAM_3
    };
    private static final String MOCK_DEFAULT_LANG = "zz";
    private static final Locale MOCK_DEFAULT_LOCALE = new Locale(MOCK_DEFAULT_LANG);
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);
    private static I18nContextProviderStrategy preTestsStrategy;

    private @Mock I18nContextProviderStrategy mockStrategy;
    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nContext mockContext;
    private @Mock MessageSource source;
    protected AutoCloseable mocks;

    @BeforeAll
    static void saveDefaultStrategy() {
        preTestsStrategy = I18N.getContextProviderStrategy();
    }

    @AfterAll
    static void restoreDefaultStrategy() {
        I18N.setContextProviderStrategy(preTestsStrategy);
    }

    @BeforeEach
    void initMocks() {
        mocks = MockitoAnnotations.openMocks(this);
        I18N.setContextProviderStrategy(mockStrategy);
        willReturn(mockProvider).given(mockStrategy).getContextProvider();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(MOCK_DEFAULT_LOCALE).given(mockContext).getLocale();
    }

    @AfterEach
    void closeMocks() throws Exception {
        mocks.close();
    }

    /**
     * Test {@link I18nSpringResources#I18nSpringResources(MessageSource)}.
     */
    @Test
    void testConstructor() {
        final I18nSpringResources result = new I18nSpringResources(source);
        assertSame(source, result.getSource());
        then(source).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nSpringResources#I18nSpringResources(MessageSource)}.
     */
    @Test
    void testConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new I18nSpringResources(null);
        });
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code() {
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_MSG, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code_NotFound() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code_InvalidTemplate() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale() {
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale_NotFound() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale_InvalidTemplate() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        then(source).should().getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes() {
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_FoundFallback() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_NotFound() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, CODES, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_InvalidTemplate() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, CODES, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_DEFAULT_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale() {
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_FoundFallback() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        willReturn(MOCK_MSG).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        then(source).should(order, never()).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_NotFound() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nSpringResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_InvalidTemplate() {
        final NoSuchMessageException mockEx = new NoSuchMessageException(MOCK_MSG_CODE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        willThrow(mockEx).given(source).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        final I18nSpringResources resources = new I18nSpringResources(source);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        final InOrder order = inOrder(source);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE, ARGS, MOCK_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_2, ARGS, MOCK_LOCALE);
        then(source).should(order, times(1)).getMessage(MOCK_MSG_CODE_3, ARGS, MOCK_LOCALE);
        then(source).shouldHaveNoMoreInteractions();
    }
}
