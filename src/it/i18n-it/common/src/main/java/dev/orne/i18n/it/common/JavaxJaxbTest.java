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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nResourcesString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;

/**
 * Tests for {@code I18nString} JAXB serialization support.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 0.1
 * @see I18nString
 */
@Tag("jaxb")
public class JavaxJaxbTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String EXPECTED_TEXT = "expected text";
    private static final String UNEXPECTED_TEXT = "unexpected text";

    @BeforeEach
    void configureI18nContext() {
        I18N.setLocale(TestMessages.DEFAULT_LOCALE);
    }

    @AfterEach
    void clearI18nContext() {
        I18N.clearContext();
    }

    @AfterAll
    static void resetI18N() {
        I18N.reconfigure();
    }

    private String randomXmlText() {
        final String raw = RandomStringUtils.random(RND_STR_LENGTH);
        final String escaped = StringEscapeUtils.escapeXml10(raw);
        return StringEscapeUtils.unescapeXml(escaped);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testContainer_I18nResourcesString() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(UNEXPECTED_TEXT)
                .withCode(TestMessages.Entries.BUNDLE_ID)
                .build();
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringContainer result = fromXML(I18nStringContainer.class, xml);
        assertNotNull(result);
        assertFixedWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testContainer_I18nStringMap() {
        final I18nStringMap bean = new I18nStringMap(UNEXPECTED_TEXT)
                .set(TestMessages.DEFAULT_LOCALE, EXPECTED_TEXT);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringContainer result = fromXML(I18nStringContainer.class, xml);
        assertNotNull(result);
        assertFixedWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testContainer_I18nStringMap_Default() {
        final I18nStringMap bean = new I18nStringMap(EXPECTED_TEXT)
                .set(TestMessages.YY_LOCALE, UNEXPECTED_TEXT);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringContainer result = fromXML(I18nStringContainer.class, xml);
        assertNotNull(result);
        assertFixedWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testContainer_I18nBilingualString() {
        final I18nBilingualString bean = new I18nBilingualString(
                EXPECTED_TEXT, UNEXPECTED_TEXT);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringContainer result = fromXML(I18nStringContainer.class, xml);
        assertNotNull(result);
        assertFixedWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Null() {
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        final String xml = toXML(container);
        final I18nStringAsObjectContainer result = fromXML(I18nStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nFixedString() {
        final I18nFixedString bean = I18nFixedString.from(EXPECTED_TEXT);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringAsObjectContainer result = fromXML(I18nStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nResourcesString() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(EXPECTED_TEXT)
                .withCode(TestMessages.Entries.BUNDLE_ID)
                .build();
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringAsObjectContainer result = fromXML(I18nStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertInstanceOf(I18nStringMap.class, result.getBean());
        assertEquals(bean.getDefaultText(), result.getBean().asMap().getDefaultText());
        assertEquals(bean.get(I18N.getLocale()), result.getBean().asMap().get(I18N.getLocale()));
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
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
        final String xml = toXML(container);
        final I18nStringAsObjectContainer result = fromXML(I18nStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nBilingualString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_I18nBilingualString() {
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final I18nBilingualString bean = new I18nBilingualString(xxText, yyText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringAsObjectContainer result = fromXML(I18nStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertInstanceOf(I18nStringMap.class, result.getBean());
        assertEquals(xxText, result.getBean().asMap().getDefaultText());
        assertEquals(yyText, result.getBean().asMap().get(I18nBilingualString.TRANSLATION_LANG));
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedContainer_Null() {
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        final String xml = toXML(container);
        final I18nFixedStringContainer result = fromXML(I18nFixedStringContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_I18nFixedString() {
        final I18nFixedString bean = I18nFixedString.from(EXPECTED_TEXT);
        final I18nFixedStringContainer container = new I18nFixedStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nFixedStringContainer result = fromXML(I18nFixedStringContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_Null() {
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        final String xml = toXML(container);
        final I18nFixedStringAsObjectContainer result = fromXML(I18nFixedStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_I18nFixedString() {
        final I18nFixedString bean = I18nFixedString.from(EXPECTED_TEXT);
        final I18nFixedStringAsObjectContainer container = new I18nFixedStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nFixedStringAsObjectContainer result = fromXML(I18nFixedStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesContainer_Null() {
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        final String xml = toXML(container);
        final I18nResourcesStringContainer result = fromXML(I18nResourcesStringContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesContainer_I18nResourcesString() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(UNEXPECTED_TEXT)
                .withCode(TestMessages.Entries.BUNDLE_ID)
                .build();
        final I18nResourcesStringContainer container = new I18nResourcesStringContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nResourcesStringContainer result = fromXML(I18nResourcesStringContainer.class, xml);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_Null() {
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        final String xml = toXML(container);
        final I18nResourcesStringAsObjectContainer result = fromXML(I18nResourcesStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nResourcesString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_I18nResourcesString() {
        final I18nResourcesString bean = I18nResourcesString
                .forDefault(EXPECTED_TEXT)
                .withCode(TestMessages.Entries.BUNDLE_ID)
                .build();
        final I18nResourcesStringAsObjectContainer container = new I18nResourcesStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nResourcesStringAsObjectContainer result = fromXML(I18nResourcesStringAsObjectContainer.class, xml);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapContainer_Null() {
        final I18nStringMapContainer container = new I18nStringMapContainer();
        final String xml = toXML(container);
        final I18nStringMapContainer result = fromXML(I18nStringMapContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_I18nStringMap() {
        final I18nStringMap bean = new I18nStringMap("Default text")
                .set(TestMessages.DEFAULT_LANG, EXPECTED_TEXT);
        final I18nStringMapContainer container = new I18nStringMapContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringMapContainer result = fromXML(I18nStringMapContainer.class, xml);
        assertNotNull(result);
        assertMapWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_I18nStringMap_Default() {
        final I18nStringMap bean = new I18nStringMap(EXPECTED_TEXT)
                .set(TestMessages.YY_LANG, UNEXPECTED_TEXT);
        final I18nStringMapContainer container = new I18nStringMapContainer();
        container.setBean(bean);
        final String xml = toXML(container);
        final I18nStringMapContainer result = fromXML(I18nStringMapContainer.class, xml);
        assertNotNull(result);
        assertMapWithCurrentText(bean, result.getBean());
    }

    /**
     * Test Jackson JSON serialization support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_Null() {
        final I18nStringMapAsObjectContainer container = new I18nStringMapAsObjectContainer();
        final String xml = toXML(container);
        final I18nStringMapAsObjectContainer result = fromXML(I18nStringMapAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    /**
     * Test Jackson JSON serialization support for {@code I18nStringMap}
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
        final String xml = toXML(container);
        final I18nStringMapAsObjectContainer result = fromXML(I18nStringMapAsObjectContainer.class, xml);
        assertNotNull(result);
        assertEquals(container, result);
    }

    protected String toXML(final Object bean) {
        return assertDoesNotThrow(() -> {
            final JAXBContext context = JAXBContext.newInstance(bean.getClass());
            final Marshaller marshaller = context.createMarshaller();
            try (final StringWriter writer = new StringWriter()) {
                marshaller.marshal(bean, writer);
                return writer.toString();
            }
        });
    }

    protected <T> T fromXML(
            final Class<T> type,
            final String xml) {
        return assertDoesNotThrow(() -> {
            final JAXBContext context = JAXBContext.newInstance(type);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            try (final StringReader reader = new StringReader(xml)) {
                final Object result = unmarshaller.unmarshal(reader);
                assertInstanceOf(type, result);
                return type.cast(result);
            }
        });
    }

    protected void assertFixedWithCurrentText(
            I18nString expected,
            I18nString result) {
        assertInstanceOf(I18nFixedString.class, result);
        assertEquals(expected.get(), result.get());
    }

    protected void assertMapWithCurrentText(
            I18nString expected,
            I18nString result) {
        assertInstanceOf(I18nStringMap.class, result);
        assertEquals(expected.get(), ((I18nStringMap) result).getDefaultText());
        assertTrue(((I18nStringMap) result).asMap().getI18n().isEmpty());
    }
}
