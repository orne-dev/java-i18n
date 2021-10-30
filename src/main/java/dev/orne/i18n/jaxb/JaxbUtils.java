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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nFixedString;
import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;
import jakarta.validation.constraints.NotNull;

/**
 * Utility class for Jakarta XML Binding (JAXB)
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
public final class JaxbUtils {

    /**
     * Private constructor.
     */
    private JaxbUtils() {
        // Utility class
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
     * Converts the specified {@code I18nString} to {@code XmlI18NString}
     * containing all the available translations.
     * Calls {@code I18nString.asMap()} to retrieve the full translations
     * version.
     * 
     * @param value The {@code I18nString} to convert
     * @return The {@code XmlI18NString} to marshall
     */
    public static XmlI18nString toFullXml(
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

    /**
     * Converts the specified {@code XmlI18NString} to {@code I18nString}.
     * If {@code XmlI18NString} contains {@code XmlI18NStringTranslation}
     * instances a {@code I18nStringMap} is returned.
     * Otherwise a {@code I18nFixedString} is returned.
     * 
     * @param value The {@code XmlI18NString} to convert
     * @return The equivalent {@code I18nString}
     */
    public static @NotNull I18nString fromXml(
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
