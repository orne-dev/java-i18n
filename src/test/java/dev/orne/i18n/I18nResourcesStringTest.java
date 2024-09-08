package dev.orne.i18n;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.I18nContext;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.params.GenerationParameters;

/**
 * Unit tests for {@code I18nResourcesString}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nResourcesString
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nResourcesStringTest {

    private static final String MOCK_RESOURCES_KEY = "mock i18n resources key";
    private static final String MOCK_DEF_MSG = "mock default message";
    private static final String MOCK_DEF_MSG_TMPL = "mock default message: {0} {2}";
    private static final String MOCK_DEF_MSG_INV_TMPL = "mock default message: {";
    private static final String MOCK_DEF_MSG_TMPL_RESULT = "mock default message: arg1 arg3";
    private static final String MOCK_MSG_CODE = "mock message code";
    private static final String MOCK_MSG_CODE_2 = "mock message code 2";
    private static final String MOCK_MSG_CODE_3 = "mock message code 3";
    private static final String MOCK_MSG = "mock message ";
    private static final Serializable MOCK_PARAM = "arg1";
    private static final Serializable MOCK_PARAM_2 = null;
    private static final Serializable MOCK_PARAM_3 = "arg3";
    private static final String[] CODES = {
            MOCK_MSG_CODE,
            MOCK_MSG_CODE_2,
            MOCK_MSG_CODE_3
    };
    private static final Serializable[] ARGS = {
            MOCK_PARAM,
            MOCK_PARAM_2,
            MOCK_PARAM_3
    };
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);

    private @Mock I18nContextProvider mockProvider;
    private @Mock I18nResources mockResources;
    private @Mock I18nContext mockContext;
    private @Mock I18nString mockI18nString;

    @BeforeEach
    void mockStrategy() {
        ContextTestUtils.setProvider(mockProvider);
    }

    @AfterEach
    void restStrategy() {
        ContextTestUtils.reset();
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor() {
        final I18nResourcesString result = new I18nResourcesString(
                MOCK_RESOURCES_KEY,
                MOCK_DEF_MSG,
                CODES,
                ARGS);
        assertEquals(MOCK_RESOURCES_KEY, result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(CODES, result.getCodes());
        assertArrayEquals(ARGS, result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullResources() {
        final I18nResourcesString result = new I18nResourcesString(
                null,
                MOCK_DEF_MSG,
                CODES,
                ARGS);
        assertNull(result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(CODES, result.getCodes());
        assertArrayEquals(ARGS, result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullDefault() {
        assertThrows(NullPointerException.class, () -> {
            new I18nResourcesString(
                    MOCK_RESOURCES_KEY,
                    null,
                    CODES,
                    ARGS);
        });
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullCodes() {
        assertThrows(NullPointerException.class, () -> {
            new I18nResourcesString(
                    MOCK_RESOURCES_KEY,
                    MOCK_DEF_MSG,
                    null,
                    ARGS);
        });
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            new I18nResourcesString(
                    MOCK_RESOURCES_KEY,
                    MOCK_DEF_MSG,
                    new String[] {
                        MOCK_MSG_CODE,
                        null,
                        MOCK_MSG_CODE_3
                    },
                    ARGS);
        });
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NoArgs() {
        final I18nResourcesString result = new I18nResourcesString(
                MOCK_RESOURCES_KEY,
                MOCK_DEF_MSG,
                CODES);
        assertEquals(MOCK_RESOURCES_KEY, result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(CODES, result.getCodes());
        assertArrayEquals(new Serializable[0], result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullArgs() {
        assertThrows(NullPointerException.class, () -> {
            new I18nResourcesString(
                    MOCK_RESOURCES_KEY,
                    MOCK_DEF_MSG,
                    CODES,
                    (Serializable[]) null);
        });
    }

    /**
     * Test {@link I18nResourcesString#I18nResourcesString(String, String, String[], Serializable...)}.
     */
    @Test
    void testConstructor_NullArg() {
        final Serializable[] args = new Serializable[] {
                MOCK_PARAM,
                null,
                MOCK_PARAM_3
        };
        final I18nResourcesString result = new I18nResourcesString(
                MOCK_RESOURCES_KEY,
                MOCK_DEF_MSG,
                CODES,
                args);
        assertEquals(MOCK_RESOURCES_KEY, result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(CODES, result.getCodes());
        assertArrayEquals(args, result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString#forDefault(String)}.
     */
    @Test
    void testForDefault() {
        assertThrows(NullPointerException.class, () -> {
            I18nResourcesString.forDefault(null);
        });
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG);
        assertNotNull(builder);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#ofResources(String)}.
     */
    @Test
    void testBuilderOfResources() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY);
        assertEquals(MOCK_RESOURCES_KEY, builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#ofResources(String)}.
     */
    @Test
    void testBuilderOfResources_Null() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(null);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#ofResources(String)}.
     */
    @Test
    void testBuilderOfResources_Reset() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .ofResources(null);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCode(String)}.
     */
    @Test
    void testBuilderWithCode() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode(MOCK_MSG_CODE);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertEquals(1, builder.getCodes().size());
        assertTrue(builder.getCodes().contains(MOCK_MSG_CODE));
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCode(String)}.
     */
    @Test
    void testBuilderWithCode_Null() {
        final I18nResourcesString.Builder builder =
                I18nResourcesString.forDefault(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            builder.withCode(null);
        });
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCode(String)}.
     */
    @Test
    void testBuilderWithCode_Multiple() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode(MOCK_MSG_CODE)
                .withCode(MOCK_MSG_CODE_2)
                .withCode(MOCK_MSG_CODE_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertEquals(3, builder.getCodes().size());
        assertEquals(MOCK_MSG_CODE, builder.getCodes().get(0));
        assertEquals(MOCK_MSG_CODE_2, builder.getCodes().get(1));
        assertEquals(MOCK_MSG_CODE_3, builder.getCodes().get(2));
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCodes(String...)}.
     */
    @Test
    void testBuilderWithCodes() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCodes(MOCK_MSG_CODE);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertEquals(1, builder.getCodes().size());
        assertTrue(builder.getCodes().contains(MOCK_MSG_CODE));
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCodes(String...)}.
     */
    @Test
    void testBuilderWithCodes_NullCodes() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            builder.withCodes((String[]) null);
        });
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCodes(String...)}.
     */
    @Test
    void testBuilderWithCodes_NullCode() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG);
        assertThrows(IllegalArgumentException.class, () -> {
            builder.withCodes(MOCK_MSG_CODE, null, MOCK_MSG_CODE_3);
        });
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCodes(String...)}.
     */
    @Test
    void testBuilderWithCodes_Varargs() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCodes(MOCK_MSG_CODE, MOCK_MSG_CODE_2, MOCK_MSG_CODE_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertEquals(3, builder.getCodes().size());
        assertEquals(MOCK_MSG_CODE, builder.getCodes().get(0));
        assertEquals(MOCK_MSG_CODE_2, builder.getCodes().get(1));
        assertEquals(MOCK_MSG_CODE_3, builder.getCodes().get(2));
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
        final I18nResourcesString.Builder other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode(MOCK_MSG_CODE)
                .withCode(MOCK_MSG_CODE_2)
                .withCode(MOCK_MSG_CODE_3);
        assertEquals(other, builder);
        assertEquals(other.hashCode(), builder.hashCode());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withCodes(String...)}.
     */
    @Test
    void testBuilderWithCodes_Append() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode(MOCK_MSG_CODE)
                .withCodes(MOCK_MSG_CODE_2, MOCK_MSG_CODE_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertEquals(3, builder.getCodes().size());
        assertEquals(MOCK_MSG_CODE, builder.getCodes().get(0));
        assertEquals(MOCK_MSG_CODE_2, builder.getCodes().get(1));
        assertEquals(MOCK_MSG_CODE_3, builder.getCodes().get(2));
        assertNotNull(builder.getArguments());
        assertTrue(builder.getArguments().isEmpty());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArg(Serializable)}.
     */
    @Test
    void testBuilderWithArg() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArg(MOCK_PARAM);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(1, builder.getArguments().size());
        assertTrue(builder.getArguments().contains(MOCK_PARAM));
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArg(Serializable)}.
     */
    @Test
    void testBuilderWithArg_Null() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArg(null);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(1, builder.getArguments().size());
        assertNull(builder.getArguments().get(0));
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArg(Serializable)}.
     */
    @Test
    void testBuilderWithArg_Multiple() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArg(MOCK_PARAM)
                .withArg(MOCK_PARAM_2)
                .withArg(MOCK_PARAM_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(3, builder.getArguments().size());
        assertEquals(MOCK_PARAM, builder.getArguments().get(0));
        assertEquals(MOCK_PARAM_2, builder.getArguments().get(1));
        assertEquals(MOCK_PARAM_3, builder.getArguments().get(2));
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArgs(Serializable...)}.
     */
    @Test
    void testBuilderWithArgs() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArgs(MOCK_PARAM);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(1, builder.getArguments().size());
        assertTrue(builder.getArguments().contains(MOCK_PARAM));
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArgs(Serializable...)}.
     */
    @Test
    void testBuilderWithArgs_NullArgs() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG);
        assertThrows(NullPointerException.class, () -> {
            builder.withArgs((Serializable[]) null);
        });
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArgs(Serializable...)}.
     */
    @Test
    void testBuilderWithArgs_NullArg() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArgs(MOCK_PARAM, null, MOCK_PARAM_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(3, builder.getArguments().size());
        assertEquals(MOCK_PARAM, builder.getArguments().get(0));
        assertNull(builder.getArguments().get(1));
        assertEquals(MOCK_PARAM_3, builder.getArguments().get(2));
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArgs(Serializable...)}.
     */
    @Test
    void testBuilderWithArgs_Varargs() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArgs(MOCK_PARAM, MOCK_PARAM_2, MOCK_PARAM_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(3, builder.getArguments().size());
        assertEquals(MOCK_PARAM, builder.getArguments().get(0));
        assertEquals(MOCK_PARAM_2, builder.getArguments().get(1));
        assertEquals(MOCK_PARAM_3, builder.getArguments().get(2));
        final I18nResourcesString.Builder other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArg(MOCK_PARAM)
                .withArg(MOCK_PARAM_2)
                .withArg(MOCK_PARAM_3);
        assertEquals(other, builder);
        assertEquals(other.hashCode(), builder.hashCode());
    }

    /**
     * Test {@link I18nResourcesString.Builder#withArgs(Serializable...)}.
     */
    @Test
    void testBuilderWithArgs_Append() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withArg(MOCK_PARAM)
                .withArgs(MOCK_PARAM_2, MOCK_PARAM_3);
        assertNull(builder.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, builder.getDefaultText());
        assertNotNull(builder.getCodes());
        assertTrue(builder.getCodes().isEmpty());
        assertNotNull(builder.getArguments());
        assertEquals(3, builder.getArguments().size());
        assertEquals(MOCK_PARAM, builder.getArguments().get(0));
        assertEquals(MOCK_PARAM_2, builder.getArguments().get(1));
        assertEquals(MOCK_PARAM_3, builder.getArguments().get(2));
    }

    /**
     * Test {@link I18nResourcesString.Builder#build()}.
     */
    @Test
    void testBuilderBuild() {
        final I18nResourcesString result = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(MOCK_RESOURCES_KEY, result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(CODES, result.getCodes());
        assertArrayEquals(ARGS, result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString.Builder#build()}.
     */
    @Test
    void testBuilderBuild_Min() {
        final I18nResourcesString result = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCode(MOCK_MSG_CODE)
                .build();
        assertNull(result.getI18nResourcesKey());
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertArrayEquals(new String[] { MOCK_MSG_CODE }, result.getCodes());
        assertArrayEquals(new Serializable[0], result.getArguments());
    }

    /**
     * Test {@link I18nResourcesString.Builder#build()}.
     */
    @Test
    void testBuilderBuild_EmptyCodes() {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(MOCK_DEF_MSG);
        assertThrows(IllegalArgumentException.class, () -> {
                builder.build();
        });
    }

    /**
     * Test {@link I18nResourcesString#FormattedDefaultText()}.
     */
    @Test
    void testGetFormattedDefaultText() {
        I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG_TMPL)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, bean.getFormattedDefaultText());
        bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG_INV_TMPL)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(MOCK_DEF_MSG_INV_TMPL, bean.getFormattedDefaultText());
    }

    /**
     * Test {@link I18nResourcesString#get()}.
     */
    @Test
    void testGet() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        final String result = bean.get();
        assertEquals(MOCK_MSG, result);
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should().getI18nResources(MOCK_RESOURCES_KEY);
        then(mockContext).shouldHaveNoMoreInteractions();
        then(mockResources).should().getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        then(mockResources).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#get(String)}.
     */
    @Test
    void testGet_Language() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, (Object[]) ARGS);
        final String result = bean.get(MOCK_LANG);
        assertEquals(MOCK_MSG, result);
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should().getI18nResources(MOCK_RESOURCES_KEY);
        then(mockContext).shouldHaveNoMoreInteractions();
        then(mockResources).should().getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, (Object[]) ARGS);
        then(mockResources).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#get(String)}.
     */
    @Test
    void testGet_Language_Null() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertThrows(NullPointerException.class, () -> {
            bean.get((String) null);
        });
    }

    /**
     * Test {@link I18nResourcesString#get(Locale)}.
     */
    @Test
    void testGet_Locale() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, (Object[]) ARGS);
        final String result = bean.get(MOCK_LOCALE);
        assertEquals(MOCK_MSG, result);
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should().getI18nResources(MOCK_RESOURCES_KEY);
        then(mockContext).shouldHaveNoMoreInteractions();
        then(mockResources).should().getMessage(MOCK_DEF_MSG, CODES, MOCK_LOCALE, (Object[]) ARGS);
        then(mockResources).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#get(Locale)}.
     */
    @Test
    void testGet_Locale_Null() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertThrows(NullPointerException.class, () -> {
            bean.get((Locale) null);
        });
    }

    /**
     * Test {@link I18nResourcesString#equals(Object)} and {@link I18nResourcesString#hashCode()}.
     */
    @Test
    void testEqualsHash() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertNotEquals(bean, (Object) null);
        assertEquals(bean, bean);
        assertEquals(bean.hashCode(), bean.hashCode());
        assertNotEquals(bean, new Object());
        assertNotEquals(bean, mockI18nString);
        then(mockI18nString).shouldHaveNoInteractions();
        I18nResourcesString other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(bean, other);
        assertEquals(bean.hashCode(), other.hashCode());
        other = I18nResourcesString
                .forDefault("another default message")
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertNotEquals(bean, other);
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertNotEquals(bean, other);
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources("another resources")
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertNotEquals(bean, other);
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCode(MOCK_MSG_CODE)
                .withArgs(ARGS)
                .build();
        assertNotEquals(bean, other);
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .build();
        assertNotEquals(bean, other);
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArg("another arg")
                .build();
        assertNotEquals(bean, other);
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertFalse(bean.isEquivalent(null));
        assertTrue(bean.isEquivalent(bean));
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        willReturn(MOCK_MSG).given(mockI18nString).get();
        assertTrue(bean.isEquivalent(mockI18nString));
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should().getI18nResources(MOCK_RESOURCES_KEY);
        then(mockContext).shouldHaveNoMoreInteractions();
        then(mockResources).should().getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        then(mockResources).shouldHaveNoMoreInteractions();
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent_I18nString_Other() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        willReturn("another text").given(mockI18nString).get();
        assertFalse(bean.isEquivalent(mockI18nString));
        then(mockProvider).should().getContext();
        then(mockProvider).shouldHaveNoMoreInteractions();
        then(mockContext).should().getI18nResources(MOCK_RESOURCES_KEY);
        then(mockContext).shouldHaveNoMoreInteractions();
        then(mockResources).should().getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        then(mockResources).shouldHaveNoMoreInteractions();
        then(mockI18nString).should().get();
        then(mockI18nString).shouldHaveNoMoreInteractions();
    }

    /**
     * Test {@link I18nResourcesString#isEquivalent(I18nString)}.
     */
    @Test
    void testIsEquivalent_I18nResourcesString() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        I18nResourcesString other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertTrue(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault("another default message")
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertFalse(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertFalse(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources("another resources")
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertFalse(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCode(MOCK_MSG_CODE)
                .withArgs(ARGS)
                .build();
        assertFalse(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .build();
        assertFalse(bean.isEquivalent(other));
        other = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArg("another arg")
                .build();
        assertFalse(bean.isEquivalent(other));
    }

    /**
     * Test {@link I18nFixedString#asMap()}.
     */
    @Test
    void testAsMap() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        willReturn(mockContext).given(mockProvider).getContext();
        willReturn(MOCK_LOCALE).given(mockContext).getLocale();
        willReturn(mockResources).given(mockContext).getI18nResources(MOCK_RESOURCES_KEY);
        willReturn(MOCK_MSG).given(mockResources).getMessage(MOCK_DEF_MSG, CODES, (Object[]) ARGS);
        final I18nStringMap result = bean.asMap();
        assertNotNull(result);
        assertEquals(MOCK_DEF_MSG, result.getDefaultText());
        assertEquals(1, result.getI18n().size());
        assertEquals(MOCK_MSG, result.getI18n().get(MOCK_LANG));
    }

    /**
     * Test {@link I18nResourcesString#toString()}.
     */
    @Test
    void testToString() {
        I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG_TMPL)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(MOCK_DEF_MSG_TMPL_RESULT, bean.toString());
        bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG_INV_TMPL)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        assertEquals(MOCK_DEF_MSG_INV_TMPL, bean.toString());
    }

    /**
     * Test {@link java.io.Serializable} implementation.
     */
    @Test
    void testSerializable() throws IOException, ClassNotFoundException {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(MOCK_DEF_MSG)
                .ofResources(MOCK_RESOURCES_KEY)
                .withCodes(CODES)
                .withArgs(ARGS)
                .build();
        final byte[] serializationResult;
        try (
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream out = new ObjectOutputStream(baos)){
            out.writeObject(bean);
            serializationResult = baos.toByteArray();
        }
        assertNotNull(serializationResult);
        final I18nResourcesString result;
        try (
                final ByteArrayInputStream bais = new ByteArrayInputStream(serializationResult);
                final ObjectInputStream in = new ObjectInputStream(bais)){
            result = (I18nResourcesString) in.readObject();
        }
        assertNotNull(result);
        assertEquals(bean, result);
        assertEquals(bean.hashCode(), result.hashCode());
    }

    /**
     * Test for {@link I18nResourcesStringGenerator#defaultValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testDefaultGeneration()
    throws Throwable {
        final I18nResourcesString result = Generators.defaultValue(I18nResourcesString.class);
        assertNotNull(result);
        final String defaultText = result.getDefaultText();
        assertNotNull(defaultText);
        assertTrue(defaultText.isEmpty());
        assertNull(result.getI18nResourcesKey());
        assertNotNull(result.getCodes());
        assertEquals(1, result.getCodes().length);
        assertTrue(result.getCodes()[0].isEmpty());
        assertNotNull(result.getArguments());
        assertEquals(0, result.getArguments().length);
    }

    /**
     * Test for {@link I18nResourcesStringGenerator#randomValue()}.
     * @throws Throwable Should not happen
     */
    @Test
    void testRandomGeneration()
    throws Throwable {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            final HashSet<String> texts = new HashSet<>();
            final HashSet<Integer> codes = new HashSet<>();
            boolean deaultResources = false;
            final HashSet<String> resources = new HashSet<>();
            final HashSet<Integer> args = new HashSet<>();
            while (texts.size() < 100 ||
                    codes.size() < I18nResourcesStringGenerator.MAX_CODES ||
                    !deaultResources ||
                    resources.size() < 3 ||
                    args.size() < I18nResourcesStringGenerator.MAX_ARGS) {
                final I18nResourcesString result = Generators.randomValue(I18nResourcesString.class);
                assertNotNull(result);
                final String text = result.getDefaultText();
                assertNotNull(text);
                texts.add(text);
                assertNotNull(result.getCodes());
                assertTrue(result.getCodes().length > 0);
                codes.add(result.getCodes().length);
                if (result.getI18nResourcesKey() == null) {
                    deaultResources = true;
                } else {
                    resources.add(result.getI18nResourcesKey());
                }
                assertNotNull(result.getArguments());
                args.add(result.getArguments().length);
            }
        });
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            final HashSet<String> texts = new HashSet<>();
            final HashSet<Integer> codes = new HashSet<>();
            boolean deaultResources = false;
            final HashSet<String> resources = new HashSet<>();
            final HashSet<Integer> args = new HashSet<>();
            while (texts.size() < 100 ||
                    codes.size() < I18nResourcesStringGenerator.MAX_CODES ||
                    !deaultResources ||
                    resources.size() < 3 ||
                    args.size() < I18nResourcesStringGenerator.MAX_ARGS) {
                final I18nResourcesString result = Generators.randomValue(
                        I18nResourcesString.class,
                        GenerationParameters.forSizes().withMinSize(5).withMaxSize(10));
                assertNotNull(result);
                final String text = result.getDefaultText();
                assertNotNull(text);
                texts.add(text);
                assertTrue(text.length() >= 5);
                assertTrue(text.length() <= 10);
                assertNotNull(result.getCodes());
                assertTrue(result.getCodes().length > 0);
                codes.add(result.getCodes().length);
                if (result.getI18nResourcesKey() == null) {
                    deaultResources = true;
                } else {
                    resources.add(result.getI18nResourcesKey());
                }
                assertNotNull(result.getArguments());
                args.add(result.getArguments().length);
            }
        });
    }
}
