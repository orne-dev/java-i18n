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

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nBilingualString;
import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nFixedStringAsObjectContainer;
import dev.orne.i18n.I18nFixedStringContainer;
import dev.orne.i18n.I18nResourcesString;
import dev.orne.i18n.I18nResourcesStringAsObjectContainer;
import dev.orne.i18n.I18nResourcesStringContainer;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringAsObjectContainer;
import dev.orne.i18n.I18nStringContainer;
import dev.orne.i18n.I18nStringJacksonSerializer;
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.I18nStringMapAsObjectContainer;
import dev.orne.i18n.I18nStringMapContainer;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Unit tests for {@code I18nString} Jackson serialization support.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 * @see I18nString
 * @see I18nStringJacksonSerializer
 */
@Tag("ut")
class I18nStringJacksonSerializationTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);
    private static final String DEFAULT_TEXT_PROP = "defaultText";
    private static final String I18N_PROP = "i18n";

    private static ObjectMapper mapper;

    @BeforeAll
    static void createMapper() {
        mapper = new ObjectMapper();
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
     * Test Jackson JSON serialization support for {@code I18nFixedString}.
     */
    @Test
    void testI18nFixedString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final String json = toJson(bean);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        assertStringNode(text, tree);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}.
     */
    @Test
    void testI18nResourcesString() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(RandomStringUtils.random(RND_STR_LENGTH))
                .build());
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final String json = toJson(bean);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        assertStringNode(text, tree);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}.
     */
    @Test
    void testI18nStringMap() {
        final I18nStringMap bean = mock(I18nStringMap.class);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final String json = toJson(bean);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        assertStringNode(text, tree);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nBilingualString}.
     */
    @Test
    void testI18nBilingual() {
        final I18nBilingualString bean = mock(I18nBilingualString.class);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final String json = toJson(bean);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        assertStringNode(text, tree);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testContainer_I18nFixedString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testContainer_I18nResourcesString() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(RandomStringUtils.random(RND_STR_LENGTH))
                .build());
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testContainer_I18nStringMap() {
        final I18nStringMap bean = mock(I18nStringMap.class);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testContainer_I18nBilingualString() {
        final I18nBilingualString bean = mock(I18nBilingualString.class);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Null() {
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nFixedString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(text, beanNode);
        assertTranslationNodeEmpty(i18nNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nResourcesString() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(RandomStringUtils.random(RND_STR_LENGTH))
                .build());
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode(MOCK_LANG, text, i18nNode);
        assertEquals(1, i18nNode.size());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nStringMap() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode("xx", xxText, i18nNode);
        assertContainsTranslationNode("yy", yyText, i18nNode);
        assertContainsTranslationNode("zz", zzText, i18nNode);
        assertEquals(3, i18nNode.size());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nBilingualString() {
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nBilingualString bean = new I18nBilingualString(xxText, yyText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(xxText, beanNode);
        assertContainsTranslationNode(I18nBilingualString.TRANSLATION_LANG, yyText, i18nNode);
        assertEquals(1, i18nNode.size());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedContainer_Null() {
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_I18nFixedString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_Null() {
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_I18nFixedString() {
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(text, beanNode);
        assertTranslationNodeEmpty(i18nNode);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesContainer_Null() {
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesContainer_I18nResourcesString() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(RandomStringUtils.random(RND_STR_LENGTH))
                .build());
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_Null() {
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_I18nResourcesString() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(RandomStringUtils.random(RND_STR_LENGTH))
                .build());
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode(MOCK_LANG, text, i18nNode);
        assertEquals(1, i18nNode.size());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapContainer_Null() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_I18nStringMap() {
        final I18nStringMap bean = mock(I18nStringMap.class);
        final String text = RandomStringUtils.random(RND_STR_LENGTH);
        willReturn(text).given(bean).get();
        final I18nStringMapContainer container = new I18nStringMapContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertStringNode(text, beanNode);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_Null() {
        final I18nStringMapAsObjectContainer container = new I18nStringMapAsObjectContainer();
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_I18nStringMap() {
        final String defaultText = RandomStringUtils.random(RND_STR_LENGTH);
        final String xxText = RandomStringUtils.random(RND_STR_LENGTH);
        final String yyText = RandomStringUtils.random(RND_STR_LENGTH);
        final String zzText = RandomStringUtils.random(RND_STR_LENGTH);
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringMapAsObjectContainer container = new I18nStringMapAsObjectContainer();
        container.setBean(bean);
        final String json = toJson(container);
        assertNotNull(json);
        final JsonNode tree = jsonToTree(json);
        final JsonNode beanNode = assertContainerNode(tree);
        final JsonNode i18nNode = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode("xx", xxText, i18nNode);
        assertContainsTranslationNode("yy", yyText, i18nNode);
        assertContainsTranslationNode("zz", zzText, i18nNode);
        assertEquals(3, i18nNode.size());
    }

    protected String toJson(final Object bean) {
        return assertDoesNotThrow(() -> {
            return mapper.writeValueAsString(bean);
        });
    }

    protected JsonNode jsonToTree(final String json) {
        return assertDoesNotThrow(() -> {
            return mapper.readTree(json);
        });
    }

    protected void assertNullNode(
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isNull());
    }

    protected JsonNode assertContainerNode(
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isObject());
        final JsonNode beanNode = node.get(I18nStringContainer.BEAN_PROPERTY);
        return beanNode;
    }

    protected void assertStringNode(
            final @NotNull String expectedText,
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isTextual());
        assertEquals(expectedText, node.asText());
    }

    protected JsonNode assertMapNode(
            final @NotNull String expectedDefaultText,
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isObject());
        final JsonNode defaultTextNode = node.get(DEFAULT_TEXT_PROP);
        assertStringNode(expectedDefaultText, defaultTextNode);
        final JsonNode i18nNode = node.get(I18N_PROP);
        return i18nNode;
    }

    protected void assertTranslationNodeEmpty(
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isObject());
        assertTrue(node.isEmpty());
    }

    protected void assertContainsTranslationNode(
            final @NotNull String lang,
            final @NotNull String expectedText,
            final @NotNull JsonNode node) {
        assertNotNull(node);
        assertTrue(node.isObject());
        final JsonNode translationNode = node.get(lang);
        assertStringNode(expectedText, translationNode);
    }
}
