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

import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * {@code I18nString} implementation for two supported languages.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 */
@JsonDeserialize(
        using=I18nStringJacksonDeserializer.class,
        converter=I18nBilingualString.Converter.class)
public class I18nBilingualString
implements I18nString {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;
    /** The default text language. */
    public static final String DEFAULT_LANG = "xx";
    /** The translation text language. */
    public static final String TRANSLATION_LANG = "yy";

    /** The default text. */
    private final @NotNull String defaultText;
    /** The translation text. */
    private final @NotNull String translation;

    /**
     * Creates a new instance.
     * 
     * @param defaultText The default text.
     * @param translation The translation text.
     */
    public I18nBilingualString(
            final @NotNull String defaultText,
            final @NotNull String translation) {
        super();
        this.defaultText = Validate.notNull(defaultText);
        this.translation = Validate.notNull(translation);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public I18nBilingualString(
            final @NotNull I18nString copy) {
        super();
        Validate.notNull(copy);
        this.defaultText = copy.get(DEFAULT_LANG);
        this.translation = copy.get(TRANSLATION_LANG);
    }

    /**
     * Returns the default text.
     * 
     * @return The default text.
     */
    public String getDefaultText() {
        return this.defaultText;
    }

    /**
     * Returns the translation text.
     * 
     * @return The translation text.
     */
    public String getTranslation() {
        return this.translation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get() {
        return get(I18N.getLocale());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get(@NotNull String language) {
        if (TRANSLATION_LANG.equals(Validate.notNull(language))) {
            return translation;
        } else {
            return defaultText;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get(@NotNull Locale locale) {
        return get(Validate.notNull(locale).getLanguage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.defaultText)
                .append(this.translation)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (this == obj) { return true; }
        if (!this.getClass().equals(obj.getClass())) { return false; }
        final I18nBilingualString other = (I18nBilingualString) obj;
        return new EqualsBuilder()
                .append(this.defaultText, other.defaultText)
                .append(this.translation, other.translation)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEquivalent(final I18nString obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (getClass().equals(obj.getClass())) { return equals(obj); }
        return get(DEFAULT_LANG).equals(obj.get(DEFAULT_LANG)) &&
                get(TRANSLATION_LANG).equals(obj.get(TRANSLATION_LANG));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nStringMap asMap() {
        return new I18nStringMap(this.defaultText)
                .set(TRANSLATION_LANG, this.translation);
    }

    /**
     * Returns the default text.
     * 
     * @return The default text.
     */
    @Override
    public String toString() {
        return this.getDefaultText();
    }

    /**
     * Example Apache commons converter.
     */
    public static class Converter
    extends StdConverter<I18nString, I18nBilingualString> {

        /**
         * Creates a new instance.
         */
        public Converter() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull I18nBilingualString convert(
                final @NotNull I18nString value) {
            return new I18nBilingualString(value);
        }
    }
}
