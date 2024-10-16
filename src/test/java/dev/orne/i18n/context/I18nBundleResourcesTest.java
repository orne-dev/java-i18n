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
import static org.mockito.BDDMockito.*;

import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@code I18nBundleResources}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nBundleResources
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nBundleResourcesTest {

    private static final String BASE_NAME = "dev.orne.i18n.test-messages";
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
    private static final String MOCK_CTX_LANG = "zz";
    private static final Locale MOCK_CTX_LOCALE = new Locale(MOCK_CTX_LANG);
    private static final String MOCK_LANG = "yy";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);

    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nContext mockContext;
    private @Mock MockResourceBundle mockBundle;

    @AfterEach
    void resetConfiguration() {
        ContextTestUtils.reset();
    }

    void mockStrategy() {
        ContextTestUtils.setProvider(mockProvider);
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(MOCK_CTX_LOCALE).given(mockContext).getLocale();
    }

    /**
     * Test {@link I18nBundleResources#I18nBundleResources(String)}.
     */
    @Test
    void testConstructor() {
        mockStrategy();
        assertThrows(NullPointerException.class, () -> {
            new I18nBundleResources(null);
        });
        final I18nBundleResources result = new I18nBundleResources(BASE_NAME);
        ResourceBundle bundle = result.getBundle(MOCK_LOCALE);
        assertEquals(BASE_NAME, bundle.getBaseBundleName());
        assertEquals(MOCK_LOCALE, bundle.getLocale());
        bundle = result.getBundle(new Locale("xx"));
        assertEquals(BASE_NAME, bundle.getBaseBundleName());
        assertEquals(Locale.ROOT, bundle.getLocale());
        bundle = result.getBundle(MOCK_CTX_LOCALE);
        assertEquals(BASE_NAME, bundle.getBaseBundleName());
        assertEquals(MOCK_CTX_LOCALE, bundle.getLocale());
        bundle = result.getBundle();
        assertEquals(BASE_NAME, bundle.getBaseBundleName());
        assertEquals(MOCK_CTX_LOCALE, bundle.getLocale());
    }

    /**
     * Test {@link I18nBundleResources#forBasename(String)}.
     */
    @Test
    void testForBasename() {
        I18nBundleResources result = assertInstanceOf(I18nBundleResources.class, 
                I18nBundleResources.forBasename(BASE_NAME));
        assertEquals(BASE_NAME, result.getBundle().getBaseBundleName());
        assertInstanceOf(DummyI18nResources.class,
                I18nBundleResources.forBasename("dev.orne.i18n.missing-messages"));
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_MSG, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code_NotFound() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Object...)}
     */
    @Test
    void testGetMessage_Code_InvalidTemplate() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, MOCK_MSG_CODE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale_NotFound() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String, Locale, Object...)}
     */
    @Test
    void testGetMessage_Code_Locale_InvalidTemplate() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, MOCK_MSG_CODE, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        then(mockBundle).should().handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_FoundFallback() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_NotFound() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_3);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, CODES, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Object...)}
     */
    @Test
    void testGetMessage_Codes_InvalidTemplate() {
        mockStrategy();
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_CTX_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_3);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, CODES, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_FoundFallback() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(MOCK_MSG).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        final String result = resources.getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_MSG, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, never()).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_NotFound() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_3);
        final String result = resources.getMessage(MOCK_DEF_MSG_TMPL, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link I18nBundleResources#getMessage(String, String[], Locale, Object...)}
     */
    @Test
    void testGetMessage_Codes_Locale_InvalidTemplate() {
        final I18nBundleResources resources = spy(new I18nBundleResources(BASE_NAME));
        willReturn(mockBundle).given(resources).getBundle(MOCK_LOCALE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_2);
        willReturn(null).given(mockBundle).handleGetObject(MOCK_MSG_CODE_3);
        final String result = resources.getMessage(MOCK_DEF_MSG_INV_TMPL, CODES, MOCK_LOCALE, ARGS);
        assertEquals(MOCK_DEF_MSG_INV_TMPL, result);
        final InOrder order = inOrder(mockBundle);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_2);
        then(mockBundle).should(order, times(1)).handleGetObject(MOCK_MSG_CODE_3);
        then(mockBundle).shouldHaveNoMoreInteractions();
    }

    abstract static class MockResourceBundle
    extends ResourceBundle {
        @Override
        public abstract Object handleGetObject(String key);
    }
}
