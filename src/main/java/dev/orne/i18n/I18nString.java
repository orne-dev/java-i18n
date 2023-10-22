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
import java.util.Locale;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dev.orne.i18n.jaxb.I18nStringAdapter;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Interface for I18N texts.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@XmlJavaTypeAdapter(I18nStringAdapter.class)
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(
        dev.orne.i18n.jaxb.javax.I18nStringAdapter.class)
@JsonSerialize(using=I18nStringJacksonSerializer.class)
@JsonDeserialize(using=I18nStringJacksonDeserializer.class)
public interface I18nString
extends Serializable {

    /**
     * Returns a {@code I18nFixedString} instance for the specified text.
     * <p>
     * Will return {@code null} if {@code text} is {@code null}.
     * 
     * @param text The fixed text for all languages
     * @return The instance for the specified text, or {@code null} if
     * {@code text} is {@code null}
     * @see I18nFixedString#from(String)
     */
    public static I18nFixedString fixed(
            final String text) {
        return I18nFixedString.from(text);
    }

    /**
     * Creates a new {@code I18nStringMap} instance with specified default
     * text and no translations.
     * 
     * @param defaultText The default text
     * @return The created instance
     */
    public static I18nStringMap mapped(
            final @NotNull String defaultText) {
        return new I18nStringMap(defaultText);
    }

    /**
     * Creates a new {@code I18nResourcesString} builder with the specified
     * default text.
     * 
     * @param defaultMessage The default text
     * @return The new {@code I18nResourcesString} builder
     * @see I18nResourcesString#forDefault(String)
     */
    public static I18nResourcesString.Builder fromResources(
            final @NotNull String defaultMessage) {
        return I18nResourcesString.forDefault(defaultMessage);
    }


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
}
