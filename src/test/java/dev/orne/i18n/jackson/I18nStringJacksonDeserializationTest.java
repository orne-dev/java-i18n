package dev.orne.i18n.jackson;

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

import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nBilingualString;
import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nResourcesString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringAsObjectContainer;
import dev.orne.i18n.I18nStringContainer;
import dev.orne.i18n.I18nStringJacksonDeserializer;
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.I18nStringMapAsObjectContainer;
import dev.orne.i18n.I18nStringMapContainer;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Unit tests for {@code I18nString} Jackson deserialization support.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see I18nString
 * @see I18nStringJacksonDeserializer
 */
@Tag("ut")
class I18nStringJacksonDeserializationTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);
    private static final String DEFAULT_TEXT_PROP = "defaultText";
    private static final String I18N_PROP = "i18n";

    private static ObjectMapper mapper;
    private static JsonNodeFactory nodeFactory;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper();
        nodeFactory = mapper.getNodeFactory();
    }

    @AfterEach
    void clearI18nContext() {
        I18nContextProvider.getInstance().clearContext();
    }

    @AfterAll
    static void resetI18N() {
        ContextTestUtils.reset();
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nString}.
     */
    @Test
    void testI18nString_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nString.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nString}.
     */
    @Test
    void testI18nString_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        final I18nString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nString.class);
        });
        assertEquals(I18nFixedString.from(text), result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nString}.
     */
    @Test
    void testI18nString_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        final I18nString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nString.class);
        });
        assertEquals(bean, result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nFixedString}.
     */
    @Test
    void testI18nFixedString_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nFixedString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nFixedString.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nFixedString}.
     */
    @Test
    void testI18nFixedString_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        final I18nFixedString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nFixedString.class);
        });
        assertEquals(I18nFixedString.from(text), result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nFixedString}.
     */
    @Test
    void testI18nFixedString_Object() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("yy", yyText)
                .set(MOCK_LANG, text)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        final I18nFixedString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nFixedString.class);
        });
        assertEquals(I18nFixedString.from(text), result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nResourcesString}.
     */
    @Test
    void testI18nResourcesString_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nResourcesString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nResourcesString.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nResourcesString}.
     */
    @Test
    void testI18nResourcesString_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        assertThrows(JsonProcessingException.class, () -> {
            mapper.readValue(json, I18nResourcesString.class);
        });
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nResourcesString}.
     */
    @Test
    void testI18nResourcesString_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        assertThrows(JsonProcessingException.class, () -> {
            mapper.readValue(json, I18nResourcesString.class);
        });
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMap}.
     */
    @Test
    void testI18nStringMap_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nStringMap result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMap.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMap}.
     */
    @Test
    void testI18nStringMap_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        final I18nStringMap result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMap.class);
        });
        assertEquals(new I18nStringMap(text), result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMap}.
     */
    @Test
    void testI18nStringMap_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        final I18nStringMap result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMap.class);
        });
        assertEquals(bean, result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nUnknownString}.
     */
    @Test
    void testI18nUnknownString_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nUnknownString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nUnknownString.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nUnknownString}.
     */
    @Test
    void testI18nUnknownString_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        final Object result = assertDoesNotThrow(() -> {
            return (Object) mapper.readValue(json, I18nUnknownString.class);
        });
        assertNotNull(result);
        assertFalse(result instanceof I18nUnknownString);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nUnknownString}.
     */
    @Test
    void testI18nUnknownString_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        final Object result = assertDoesNotThrow(() -> {
            return (Object) mapper.readValue(json, I18nUnknownString.class);
        });
        assertNotNull(result);
        assertFalse(result instanceof I18nUnknownString);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nBilingualString}.
     */
    @Test
    void testI18nBilingualString_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nBilingualString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nBilingualString.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nBilingualString}.
     */
    @Test
    void testI18nBilingualString_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(nodeFactory.textNode(text));
        final I18nBilingualString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nBilingualString.class);
        });
        assertNotNull(result);
        assertEquals(text, result.getDefaultText());
        assertEquals(text, result.getTranslation());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nBilingualString}.
     */
    @Test
    void testI18nBilingualString_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createMapNode(bean));
        final I18nBilingualString result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nBilingualString.class);
        });
        assertNotNull(result);
        assertEquals(bean.get(I18nBilingualString.DEFAULT_LANG), result.getDefaultText());
        assertEquals(bean.get(I18nBilingualString.TRANSLATION_LANG), result.getTranslation());
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringContainer}
     * in containers.
     */
    @Test
    void testContainer_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nStringContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringContainer.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringContainer}
     * in containers.
     */
    @Test
    void testContainer_NullBean() {
        final String json = nodeToJson(createContainerNode(nodeFactory.nullNode()));
        final I18nStringContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringContainer.class);
        });
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringContainer}.
     */
    @Test
    void testContainer_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(createContainerNode(nodeFactory.textNode(text)));
        final I18nStringContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringContainer.class);
        });
        assertNotNull(result);
        assertEquals(I18nFixedString.from(text), result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringContainer}.
     */
    @Test
    void testContainer_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createContainerNode(createMapNode(bean)));
        final I18nStringContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringContainer.class);
        });
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringAsObjectContainer}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nStringAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringAsObjectContainer.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringAsObjectContainer}
     * in containers.
     */
    @Test
    void testAsObjectContainer_NullBean() {
        final String json = nodeToJson(createContainerNode(nodeFactory.nullNode()));
        final I18nStringAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringAsObjectContainer.class);
        });
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringAsObjectContainer}.
     */
    @Test
    void testAsObjectContainer_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(createContainerNode(nodeFactory.textNode(text)));
        final I18nStringAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringAsObjectContainer.class);
        });
        assertNotNull(result);
        assertEquals(I18nFixedString.from(text), result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringAsObjectContainer}.
     */
    @Test
    void testAsObjectContainer_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createContainerNode(createMapNode(bean)));
        final I18nStringAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringAsObjectContainer.class);
        });
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringMapContainer}
     * in containers.
     */
    @Test
    void testMapContainer_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nStringMapContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapContainer.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringMapContainer}
     * in containers.
     */
    @Test
    void testMapContainer_NullBean() {
        final String json = nodeToJson(createContainerNode(nodeFactory.nullNode()));
        final I18nStringMapContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapContainer.class);
        });
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMapContainer}.
     */
    @Test
    void testMapContainer_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(createContainerNode(nodeFactory.textNode(text)));
        final I18nStringMapContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapContainer.class);
        });
        assertNotNull(result);
        assertEquals(new I18nStringMap(text), result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMapContainer}.
     */
    @Test
    void testMapContainer_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createContainerNode(createMapNode(bean)));
        final I18nStringMapContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapContainer.class);
        });
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringMapAsObjectContainer}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_Null() {
        final String json = nodeToJson(nodeFactory.nullNode());
        final I18nStringMapAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapAsObjectContainer.class);
        });
        assertNull(result);
    }

    /**
     * Test Jackson JSON deserialization support for null {@code I18nStringMapAsObjectContainer}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_NullBean() {
        final String json = nodeToJson(createContainerNode(nodeFactory.nullNode()));
        final I18nStringMapAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapAsObjectContainer.class);
        });
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMapAsObjectContainer}.
     */
    @Test
    void testMapAsObjectContainer_String() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final String json = nodeToJson(createContainerNode(nodeFactory.textNode(text)));
        final I18nStringMapAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapAsObjectContainer.class);
        });
        assertNotNull(result);
        assertEquals(new I18nStringMap(text), result.getBean());
    }

    /**
     * Test Jackson JSON deserialization support for {@code I18nStringMapAsObjectContainer}.
     */
    @Test
    void testMapAsObjectContainer_Object() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final String json = nodeToJson(createContainerNode(createMapNode(bean)));
        final I18nStringMapAsObjectContainer result = assertDoesNotThrow(() -> {
            return mapper.readValue(json, I18nStringMapAsObjectContainer.class);
        });
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    protected @NotNull String nodeToJson(
            final @NotNull JsonNode node) {
        final StringWriter writer = new StringWriter();
        final JsonGenerator generator = assertDoesNotThrow(() -> {
            return mapper.createGenerator(writer);
        });
        assertDoesNotThrow(() -> {
            mapper.writeTree(generator, node);
        });
        return writer.toString();
    }

    /**
     * Test {@link I18nStringJacksonDeserializer#deserialize(JsonParser, DeserializationContext)}.
     */
    @Test
    void testDeserialize_NoToken() {
        final JsonParser parser = mock(JsonParser.class);
        final DeserializationContext context = mock(DeserializationContext.class);
        final I18nStringJacksonDeserializer deserializer = new I18nStringJacksonDeserializer();
        willReturn(false).given(parser).hasCurrentToken();
        final I18nString result = assertDoesNotThrow(() -> {
            return deserializer.deserialize(parser, context);
        });
        assertNull(result);
        then(parser).should().hasCurrentToken();
        then(parser).shouldHaveNoMoreInteractions();
        then(context).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nStringJacksonDeserializer#deserialize(JsonParser, DeserializationContext)}.
     */
    @Test
    void testDeserialize_NullToken() {
        final JsonParser parser = mock(JsonParser.class);
        final DeserializationContext context = mock(DeserializationContext.class);
        final I18nStringJacksonDeserializer deserializer = new I18nStringJacksonDeserializer();
        final JsonToken token = JsonToken.VALUE_NULL;
        willReturn(true).given(parser).hasCurrentToken();
        willReturn(token).given(parser).getCurrentToken();
        final I18nString result = assertDoesNotThrow(() -> {
            return deserializer.deserialize(parser, context);
        });
        assertNull(result);
        then(parser).should().hasCurrentToken();
        then(parser).should().getCurrentToken();
        then(parser).shouldHaveNoMoreInteractions();
        then(context).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nStringJacksonDeserializer#deserialize(JsonParser, DeserializationContext)}.
     */
    @Test
    void testDeserialize_InvalidToken() {
        final JsonParser parser = mock(JsonParser.class);
        final DeserializationContext context = mock(DeserializationContext.class);
        final I18nStringJacksonDeserializer deserializer = new I18nStringJacksonDeserializer();
        final JsonToken token = JsonToken.VALUE_NUMBER_INT;
        final JsonLocation location = mock(JsonLocation.class);
        willReturn(true).given(parser).hasCurrentToken();
        willReturn(token).given(parser).getCurrentToken();
        willReturn(location).given(parser).getTokenLocation();
        final JsonParseException result = assertThrows(JsonParseException.class, () -> {
            deserializer.deserialize(parser, context);
        });
        assertNotNull(result);
        assertSame(parser, result.getProcessor());
        assertSame(location, result.getLocation());
        assertNotNull(result.getMessage());
        then(parser).should().hasCurrentToken();
        then(parser).should().getCurrentToken();
        then(parser).should().getTokenLocation();
        then(parser).shouldHaveNoMoreInteractions();
        then(context).shouldHaveNoInteractions();
    }

    protected @NotNull JsonNode createContainerNode(
            final JsonNode beanNode) {
        final ObjectNode root = nodeFactory.objectNode();
        root.set(
                I18nStringContainer.BEAN_PROPERTY,
                beanNode);
        return root;
    }

    protected @NotNull JsonNode createMapNode(
            final I18nStringMap model) {
        final ObjectNode root = nodeFactory.objectNode();
        root.set(
                DEFAULT_TEXT_PROP,
                nodeFactory.textNode(model.getDefaultText()));
        final ObjectNode i18n = nodeFactory.objectNode();
        for (final Map.Entry<String, String> entry : model.getI18n().entrySet()) {
            i18n.set(
                    entry.getKey(),
                    nodeFactory.textNode(entry.getValue()));
        }
        root.set(I18N_PROP, i18n);
        return root;
    }

    public class I18nUnknownString
    implements I18nString {
        /** The serial version UID. */
        private static final long serialVersionUID = 1L;
        /** The default text, with other name. */
        private final @NotNull String someText;
        public I18nUnknownString(
                final @NotNull String someText) {
            super();
            this.someText = Validate.notNull(someText);
        }
        public String getSomeText() {
            return this.someText;
        }
        @Override
        public @NotNull String get() {
            return someText;
        }
        @Override
        public @NotNull String get(@NotNull String language) {
            return someText;
        }
        @Override
        public @NotNull String get(@NotNull Locale locale) {
            return someText;
        }
        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(this.someText)
                    .toHashCode();
        }
        @Override
        public boolean equals(final Object obj) {
            if (obj == null) { return false; }
            if (this == obj) { return true; }
            if (!this.getClass().equals(obj.getClass())) { return false; }
            final I18nUnknownString other = (I18nUnknownString) obj;
            return new EqualsBuilder()
                    .append(this.someText, other.someText)
                    .isEquals();
        }
        @Override
        public boolean isEquivalent(final I18nString obj) {
            if (obj == null) { return false; }
            if (obj == this) { return true; }
            if (getClass().equals(obj.getClass())) { return equals(obj); }
            return get(this.someText).equals(obj.get());
        }
        @Override
        public I18nStringMap asMap() {
            return new I18nStringMap(this.someText);
        }
        @Override
        public String toString() {
            return this.someText;
        }
    }
}
