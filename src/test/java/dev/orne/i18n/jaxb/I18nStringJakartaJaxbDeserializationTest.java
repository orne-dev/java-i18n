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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nFixedStringAsObjectContainer;
import dev.orne.i18n.I18nFixedStringContainer;
import dev.orne.i18n.I18nResourcesStringAsObjectContainer;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringAsObjectContainer;
import dev.orne.i18n.I18nStringContainer;
import dev.orne.i18n.I18nStringMap;
import dev.orne.i18n.I18nStringMapAsObjectContainer;
import dev.orne.i18n.I18nStringMapContainer;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * Unit test for {@code I18nString} JAXB unmarshalling.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nString.JaxbAdapter
 * @see I18nString.FullJaxbAdapter
 */
@Tag("ut")
class I18nStringJakartaJaxbDeserializationTest {

    private static final int RND_STR_LENGTH = 20;
    private static final String MOCK_LANG = "xx";
    private static final Locale MOCK_LOCALE = new Locale(MOCK_LANG);

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
     * Test JAXB {@link ObjectFactory}.
     */
    @Test
    void testObjectFactory() {
        final ObjectFactory factory = new ObjectFactory();
        assertTrue(factory.createXmlI18NString() instanceof XmlI18nString);
        assertTrue(factory.createTranslation() instanceof XmlI18nStringTranslation);
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringContainer result = fromXml(xml, I18nStringContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringContainer result = fromXml(xml, I18nStringContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testContainer_Full() {
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
        final I18nStringContainer result = fromXml(xml, I18nStringContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringAsObjectContainer result = fromXml(xml, I18nStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testAsObjectContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringAsObjectContainer result = fromXml(xml, I18nStringAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testAsObjectContainer_Full() {
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
        final I18nStringAsObjectContainer result = fromXml(xml, I18nStringAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringContainer result = fromXml(xml, I18nFixedStringContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringContainer result = fromXml(xml, I18nFixedStringContainer.class);
        assertNotNull(result);
        assertEquals(text, result.getBean().get());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedContainer_I18nStringMap() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set(MOCK_LANG, xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringContainer result = fromXml(xml, I18nFixedStringContainer.class);
        assertNotNull(result);
        assertEquals(xxText, result.getBean().get());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringAsObjectContainer result = fromXml(xml, I18nFixedStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringAsObjectContainer result = fromXml(xml, I18nFixedStringAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(text, result.getBean().get());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testFixedAsObjectContainer_I18nStringMap() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set(MOCK_LANG, xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nFixedStringAsObjectContainer result = fromXml(xml, I18nFixedStringAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(xxText, result.getBean().get());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesContainer_I18nStringMap() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set(MOCK_LANG, xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nFixedString}
     * in containers.
     */
    @Test
    void testResourcesAsObjectContainer_I18nStringMap() {
        I18N.setLocale(MOCK_LOCALE);
        final String defaultText = randomXmlText();
        final String xxText = randomXmlText();
        final String yyText = randomXmlText();
        final String zzText = randomXmlText();
        final I18nStringMap bean = new I18nStringMap(defaultText)
                .set(MOCK_LANG, xxText)
                .set("yy", yyText)
                .set("zz", zzText);
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nResourcesStringAsObjectContainer result = fromXml(xml, I18nResourcesStringAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringMapContainer result = fromXml(xml, I18nStringMapContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringMapContainer result = fromXml(xml, I18nStringMapContainer.class);
        assertNotNull(result);
        assertEquals(text, result.getBean().getDefaultText());
        assertTrue(result.getBean().getI18n().isEmpty());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapContainer_I18nStringMap() {
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
        final I18nStringMapContainer result = fromXml(xml, I18nStringMapContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for null {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_Null() {
        final I18nStringContainer container = new I18nStringContainer();
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringMapAsObjectContainer result = fromXml(xml, I18nStringMapAsObjectContainer.class);
        assertNotNull(result);
        assertNull(result.getBean());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
     * in containers.
     */
    @Test
    void testMapAsObjectContainer_String() {
        final String text = randomXmlText();
        final I18nFixedString bean = I18nFixedString.from(text);
        final I18nStringContainer container = new I18nStringContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringMapAsObjectContainer result = fromXml(xml, I18nStringMapAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(text, result.getBean().getDefaultText());
        assertTrue(result.getBean().getI18n().isEmpty());
    }

    /**
     * Test JAXB XML unmarshalling support for {@code I18nStringMap}
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
        final I18nStringAsObjectContainer container = new I18nStringAsObjectContainer();
        container.setBean(bean);
        final String xml = toXml(container);
        assertNotNull(xml);
        final I18nStringMapAsObjectContainer result = fromXml(xml, I18nStringMapAsObjectContainer.class);
        assertNotNull(result);
        assertEquals(bean, result.getBean());
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

    protected static <T> T fromXml(
            final @NotNull String xml,
            final Class<T> type) {
        return assertDoesNotThrow(() -> {
            final JAXBContext context = JAXBContext.newInstance(type);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final StringReader reader = new StringReader(xml);
            final Object result = unmarshaller.unmarshal(reader);
            return type.cast(result);
        });
    }
}
