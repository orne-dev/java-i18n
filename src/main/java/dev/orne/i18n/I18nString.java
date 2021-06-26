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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.text.StringEscapeUtils;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotNull;

/**
 * Interface for I18N texts.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@XmlJavaTypeAdapter(I18nString.JaxbAdapter.class)
@JsonSerialize(using=I18nStringJacksonSerializer.class)
@JsonDeserialize(using=I18nStringJacksonDeserializer.class)
public interface I18nString
extends Serializable {

    /**
     * Returns the text for the current language.
     * 
     * @return The text for the current language
     */
    @NotNull String get();

    /**
     * Returns the text for the specified language, or the default text if no
     * translation is available.
     * 
     * @param language The language of the requested text
     * @return The text for the specified language
     */
    @NotNull String get(@NotNull String language);

    /**
     * Returns the text for language of the specified locale, or the default
     * text if no translation is available.
     * 
     * @param locale The language of the requested text
     * @return The text for the specified language
     */
    @NotNull String get(@NotNull Locale locale);

    /**
     * Returns {@code true} if the specified {@code I18nString} is equivalent
     * to this instance.
     * <ol>
     * <li>If {@code other} is {@code null} returns {@code false}.</li>
     * <li>If {@code other} is of the same type calls {@code equals()}.</li>
     * <li>Otherwise checks that the translations for current language of both
     * instances are equal.</li>
     * </ol>
     * 
     * @param other The instance to compare against
     * @return If both instances are equivalent
     */
    boolean isEquivalent(I18nString other);

    /**
     * Returns this instance represented as an {@code I18nStringMap} instance.
     * 
     * @return This instance represented as an {@code I18nStringMap} instance
     */
    I18nStringMap asMap();

    /**
     * JAXB adapter for {@code I18nString} instances.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-02
     * @see I18nString
     * @since 0.1
     */
    @API(status=Status.INTERNAL, since="0.1")
    class JaxbAdapter
    extends XmlAdapter<XmlI18nString, I18nString> {

        /**
         * {@inheritDoc}
         */
        @Override
        public XmlI18nString marshal(final I18nString value) {
            return toXml(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public I18nString unmarshal(final XmlI18nString value) {
            return fromXml(value);
        }

        /**
         * Converts the specified {@code I18nString} to {@code XmlI18NString}
         * containing only the current language translation.
         * 
         * @param value The {@code I18nString} to convert
         * @return The {@code XmlI18NString} to marshall
         */
        public static XmlI18nString toXml(
                final I18nString value) {
            final XmlI18nString result;
            if (value == null) {
                result = null;
            } else {
                result = new XmlI18nString();
                result.getContent().add(StringEscapeUtils.escapeXml10(value.get()));
            }
            return result;
        }

        /**
         * Converts the specified {@code XmlI18NString} to {@code I18nString}.
         * If {@code XmlI18NString} contains {@code XmlI18NStringTranslation}
         * instances a {@code I18nStringMap} is returned.
         * Otherwise a {@code I18nFixedString} is returned.
         * 
         * @param value The {@code XmlI18NString} to convert
         * @return The equivalent {@code I18nString}
         */
        public static I18nString fromXml(
                final @NotNull XmlI18nString value) {
            final StringBuilder buffer = new StringBuilder();
            final List<XmlI18nStringTranslation> translations = new ArrayList<>();
            for (final Serializable part : value.getContent()) {
                if (part instanceof XmlI18nStringTranslation) {
                    translations.add((XmlI18nStringTranslation) part);
                } else {
                    buffer.append(StringEscapeUtils.unescapeXml(part.toString()));
                }
            }
            final String text = buffer.toString().trim();
            final I18nString result;
            if (translations.isEmpty()) {
                result = I18nFixedString.from(text);
            } else {
                final I18nStringMap tmp = new I18nStringMap(text);
                for (final XmlI18nStringTranslation trans : translations) {
                    tmp.set(
                            StringEscapeUtils.unescapeXml(trans.getLang()),
                            StringEscapeUtils.unescapeXml(trans.getValue()));
                }
                result = tmp;
            }
            return result;
        }
    }

    /**
     * JAXB adapter for {@code I18nString} instances that marshalls
     * all available translations.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-02
     * @see I18nString
     * @since 0.1
     */
    @API(status=Status.INTERNAL, since="0.1")
    class FullJaxbAdapter
    extends JaxbAdapter {

        /**
         * {@inheritDoc}
         */
        @Override
        public XmlI18nString marshal(final I18nString value) {
            return toXml(value);
        }

        /**
         * Converts the specified {@code I18nString} to {@code XmlI18NString}
         * containing all the available translations.
         * Calls {@code I18nString.asMap()} to retrieve the full translations
         * version.
         * 
         * @param value The {@code I18nString} to convert
         * @return The {@code XmlI18NString} to marshall
         */
        public static XmlI18nString toXml(
                final I18nString value) {
            final XmlI18nString result;
            if (value == null) {
                result = null;
            } else {
                final I18nStringMap map = value.asMap();
                result = new XmlI18nString();
                result.getContent().add(StringEscapeUtils.escapeXml10(map.getDefaultText()));
                for (final Map.Entry<String,String> entry : map.getI18n().entrySet()) {
                    final XmlI18nStringTranslation translation = new XmlI18nStringTranslation();
                    translation.setLang(StringEscapeUtils.escapeXml10(entry.getKey()));
                    translation.setValue(StringEscapeUtils.escapeXml10(entry.getValue()));
                    result.getContent().add(translation);
                }
            }
            return result;
        }
    }
}
