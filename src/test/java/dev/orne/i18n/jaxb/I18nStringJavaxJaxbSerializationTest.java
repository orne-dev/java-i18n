package dev.orne.i18n.jaxb;

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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.I18nStringMapAsObjectContainer;
import dev.orne.i18n.I18nStringMapContainer;
import dev.orne.i18n.I18nXmlSchema;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.i18n.context.I18nContextProviderStrategy;
import jakarta.validation.constraints.NotNull;

/**
 * Unit test for {@code I18nString} JAXB marshalling.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nString.JaxbAdapter
 * @see I18nString.FullJaxbAdapter
 */
@Tag("ut")
class I18nStringJavaxJaxbSerializationTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);

    @AfterEach
    void clearI18nContext() {
        I18nContextProvider.getInstance().clearContext();
    }

    @AfterAll
    static void resetI18N() {
        I18nContextProviderStrategy.setInstance(null);
    }

    private String randomXmlText() {
        final String raw = RandomStringUtils.random(RND_STR_LENGTH).trim();
        final String escaped = StringEscapeUtils.escapeXml10(raw);
        return StringEscapeUtils.unescapeXml(escaped);
    }

    /**
     * Test JAXB {@link ObjectFactory}.
     */
    @Test
    void testObjectFactory() {
        final ObjectFactory factory = new ObjectFactory();
        assertTrue(factory.createXmlI18NString() instanceof XmlI18nString);
        assertTrue(factory.createTranslation() instanceof XmlI18nStringTranslation);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testContainer_I18nFixedString() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testContainer_I18nResourcesString() {
        final String defaultText = randomXmlText();
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(randomXmlText())
                .build());
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testContainer_I18nStringMap() {
        final I18nStringMap bean = mock(I18nStringMap.class);
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testContainer_I18nBilingualString() {
        final I18nBilingualString bean = mock(I18nBilingualString.class);
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nString}
     * in containers.
     */
    @Test
    void testContainer_I18nString() {
        final I18nString bean = mock(I18nString.class);
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Null() {
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nFixedString() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nResourcesString() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(randomXmlText())
                .build());
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode(MOCK_LANG, text, translations);
        assertEquals(1, translations.size());
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nStringMap() {
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode("xx", xxText, translations);
        assertContainsTranslationNode("yy", yyText, translations);
        assertContainsTranslationNode("zz", zzText, translations);
        assertEquals(3, translations.size());
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nBilingualString() {
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final I18nBilingualString bean = new I18nBilingualString(xxText, yyText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(xxText, beanNode);
        assertContainsTranslationNode(I18nBilingualString.TRANSLATION_LANG, yyText, translations);
        assertEquals(1, translations.size());
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nString() {
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final I18nString bean = mock(I18nString.class);
        final I18nStringMap asMap = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText);
        willReturn(asMap).given(bean).asMap();
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode("xx", xxText, translations);
        assertContainsTranslationNode("yy", yyText, translations);
        assertEquals(2, translations.size());
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedContainer_Null() {
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_I18nFixedString() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_Null() {
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_I18nFixedString() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesContainer_Null() {
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesContainer_I18nResourcesString() {
        final String defaultText = randomXmlText();
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(randomXmlText())
                .build());
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_Null() {
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_I18nResourcesString() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final I18nResourcesString bean = spy(I18nResourcesString
                .forDefault(defaultText)
                .withCode(randomXmlText())
                .build());
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(bean.getFormattedDefaultText(), beanNode);
        assertContainsTranslationNode(MOCK_LANG, text, translations);
        assertEquals(1, translations.size());
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapContainer_Null() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_I18nStringMap() {
        final I18nStringMap bean = mock(I18nStringMap.class);
        final String text = randomXmlText();
        willReturn(text).given(bean).get();
        final I18nStringMapContainer container = new I18nStringMapContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertHasNoTranslations(text, beanNode);
    }

    /**
     * Test JAXB XML marshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_Null() {
        final I18nStringMapAsObjectContainer container = new I18nStringMapAsObjectContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        assertNullNode(beanNode);
    }

    /**
     * Test JAXB XML marshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_I18nStringMap() {
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set("xx", xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringMapAsObjectContainer container = new I18nStringMapAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final Element tree = xmlToRootElement(xml);
        final Element beanNode = assertContainerNode(tree);
        final List<Element> translations = assertMapNode(defaultText, beanNode);
        assertContainsTranslationNode("xx", xxText, translations);
        assertContainsTranslationNode("yy", yyText, translations);
        assertContainsTranslationNode("zz", zzText, translations);
        assertEquals(3, translations.size());
    }

    protected static String toXml(
            final @NotNull Object bean) {
        return assertDoesNotThrow(() -> {
            final JAXBContext context = JAXBContext.newInstance(bean.getClass());
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final StringWriter writer = new StringWriter();
            marshaller.marshal(bean, writer);
            return writer.toString();
        });
    }

    protected static @NotNull Element xmlToRootElement(
            final @NotNull String xml) {
        return assertDoesNotThrow(() -> {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(new InputSource(new StringReader(xml)));
            assertNotNull(document);
            return document.getDocumentElement();
        });
    }

    protected static @NotNull Element assertContainerNode(
            final @NotNull Element element) {
        assertEquals(I18nStringContainer.TEST_NS, element.getNamespaceURI().toString());
        assertEquals(I18nStringContainer.ROOT_ELEMENT, element.getLocalName());
        final List<Element> childs = getChildElements(element);
        if (childs.size() > 0) {
            assertEquals(1, childs.size());
            final Node child = childs.get(0);
            assertEquals(I18nStringContainer.TEST_NS, child.getNamespaceURI().toString());
            assertEquals(I18nStringContainer.BEAN_ELEMENT, child.getLocalName());
            assertTrue(child instanceof Element);
            return (Element) child;
        }
        return null;
    }

    protected static String getTextContent(
            final @NotNull Element element) {
        final StringBuffer result = new StringBuffer();
        Node childNode = element.getFirstChild();
        while (childNode != null) {
            assertEquals(Node.TEXT_NODE, element.getChildNodes().item(0).getNodeType());
            if (Node.TEXT_NODE == childNode.getNodeType()) {
                result.append(childNode.getTextContent().trim());
            }
            childNode = childNode.getNextSibling();
        }
        return result.toString();
    }

    protected static List<Element> getChildElements(
            final @NotNull Element element) {
        final List<Element> result = new ArrayList<>();
        Node childNode = element.getFirstChild();
        while (childNode != null) {
            if (childNode instanceof Element) {
                result.add((Element) childNode);
            }
            childNode = childNode.getNextSibling();
        }
        return result;
    }

    protected static void assertNullNode(
            final @NotNull Element element) {
        assertNull(element);
    }

    protected static void assertHasNoTranslations(
            final @NotNull String text,
            final @NotNull Element element) {
        assertTrue(getChildElements(element).isEmpty());
        assertEquals(text, StringEscapeUtils.unescapeXml(element.getTextContent()));
    }

    protected static @NotNull List<Element> assertMapNode(
            final @NotNull String text,
            final @NotNull Element element) {
        assertEquals(text, StringEscapeUtils.unescapeXml(getTextContent(element)));
        final List<Element> translations = getChildElements(element);
        assertFalse(translations.isEmpty());
        for (final Element tranlation : translations) {
            assertNotNull(tranlation);
            assertEquals(I18nXmlSchema.NS, tranlation.getNamespaceURI().toString());
            assertEquals(I18nXmlSchema.TRANSLATION_ELEMENT, tranlation.getLocalName());
        }
        return translations;
    }

    protected static void assertContainsTranslationNode(
            final @NotNull String lang,
            final @NotNull String text,
            final @NotNull List<Element> translations) {
        boolean found = false;
        for (final Element translation : translations) {
            final String translationLang = translation.getAttribute(I18nXmlSchema.TRANSLATION_LANG_ATTR);
            assertNotNull(translationLang);
            if (lang.equals(translationLang)) {
                found = true;
                assertEquals(text, StringEscapeUtils.unescapeXml(translation.getTextContent()));
                assertTrue(getChildElements(translation).isEmpty());
            }
        }
        assertTrue(found);
    }
}
