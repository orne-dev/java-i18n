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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.jaxb.I18nStringMapAdapter;

/**
 * Implementation of {@code I18nString} populated with translations.
 * <p>
 * Useful for translations edition and visualization.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@XmlJavaTypeAdapter(I18nStringMapAdapter.class)
public class I18nStringMap
implements I18nString {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The default text. */
    private @NotNull String defaultText;
    /** The text translations. */
    private final @NotNull Map<@NotNull String, @NotNull String> i18n = new HashMap<>();

    /**
     * Creates a new instance with empty default text and no translations.
     */
    public I18nStringMap() {
        this("");
    }

    /**
     * Creates a new instance  with specified default text and no translations.
     * 
     * @param defaultText The default text
     */
    public I18nStringMap(
            final @NotNull String defaultText) {
        super();
        this.defaultText = Validate.notNull(defaultText);
    }

    /**
     * Copy constructor.
     * <p>
     * If {@code copy} is of {@code I18nStringMap} type a full copy is
     * performed.
     * If {@code copy} is of {@code I18nResourcesString} type the formatted
     * default text is set as default text and the result of {@code get()} as
     * translation for current language.
     * Otherwise the result of {@code get()} is used as default text.
     * 
     * @param copy The instance to copy
     */
    public I18nStringMap(
            final @NotNull I18nString copy) {
        super();
        Validate.notNull(copy);
        if (copy instanceof I18nStringMap) {
            final I18nStringMap mapcopy = (I18nStringMap) copy;
            this.defaultText = mapcopy.getDefaultText();
            this.i18n.putAll(mapcopy.i18n);
        } else if (copy instanceof I18nResourcesString) {
            final I18nResourcesString rescopy = (I18nResourcesString) copy;
            this.defaultText = rescopy.getFormattedDefaultText();
            this.i18n.put(I18N.getLocale().getLanguage(), copy.get());
        } else {
            this.defaultText = copy.get();
        }
    }

    /**
     * Returns the default text.
     * 
     * @return The default text
     */
    public @NotNull String getDefaultText() {
        return this.defaultText;
    }

    /**
     * Sets the default text.
     * 
     * @param defaultText The default text
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap setDefaultText(
            final @NotNull String defaultText) {
        this.defaultText = Validate.notNull(defaultText);
        return this;
    }

    /**
     * Returns the text translations.
     * 
     * @return The text translations
     */
    public @NotNull Map<@NotNull String, @NotNull String> getI18n() {
        return this.i18n;
    }

    /**
     * Sets the text translations. This methods modifies the internal
     * {@code Map}. Further modifications on argument will have no effect in
     * internal translations.
     * 
     * @param translations The text translations
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap setI18n(
            final @NotNull Map<@NotNull String, @NotNull String> translations) {
        Validate.notNull(translations);
        Validate.noNullElements(translations.keySet());
        Validate.noNullElements(translations.values());
        this.i18n.clear();
        this.i18n.putAll(translations);
        return this;
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
    public @NotNull String get(final @NotNull String language) {
        String tmp = Validate.notNull(language);
        while (!this.i18n.containsKey(tmp) &&
                tmp.contains("-")) {
            tmp = tmp.substring(0, tmp.lastIndexOf('-'));
        }
        final String result;
        if (this.i18n.containsKey(tmp)) {
            result = this.i18n.get(tmp);
        } else {
            result = this.defaultText;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get(final @NotNull Locale locale) {
        return get(Validate.notNull(locale).getLanguage());
    }

    /**
     * Sets the translation for the specified locale.
     * 
     * @param language The language of the translation
     * @param text The translated text
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap set(
            final @NotNull String language,
            final @NotNull String text) {
        this.i18n.put(
                Validate.notNull(language),
                Validate.notNull(text));
        return this;
    }

    /**
     * Sets the translation for the specified locale.
     * 
     * @param locale The language of the translation
     * @param text The translated text
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap set(
            final @NotNull Locale locale,
            final @NotNull String text) {
        set(Validate.notNull(locale).getLanguage(), text);
        return this;
    }

    /**
     * Removes the translation for the specified language.
     * 
     * @param language The language of the translation
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap remove(
            final @NotNull String language) {
        this.i18n.remove(Validate.notNull(language));
        return this;
    }

    /**
     * Removes the translation for the specified language.
     * 
     * @param locale The language of the translation
     * @return This instance for method chaining
     */
    public @NotNull I18nStringMap remove(
            final @NotNull Locale locale) {
        this.i18n.remove(Validate.notNull(locale).getLanguage());
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.defaultText)
                .append(this.i18n)
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
        final I18nStringMap other = (I18nStringMap) obj;
        return new EqualsBuilder()
                .append(this.defaultText, other.defaultText)
                .append(this.i18n, other.i18n)
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
        return get().equals(obj.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nStringMap asMap() {
        return this;
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
}
