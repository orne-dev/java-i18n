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

import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.WeakHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Implementation of {@code I18nString} that doesn't contain translations.
 * <p>
 * Useful for constant values and I18N texts associated with current thread
 * language (texts provided by users, translations retrieved from DB in lazy
 * mode, etc.).
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
public final class I18nFixedString
implements I18nString {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The cache of {@code String} to {@code I18nFixedString} relations. */
    private static final WeakHashMap<String, WeakReference<I18nFixedString>> CACHE =
            new WeakHashMap<>();

    /** The fixed text for all languages. */
    private final @NotNull String text;

    /**
     * Creates a new instance.
     * 
     * @param text The fixed text for all languages
     */
    private I18nFixedString(
            final @NotNull String text) {
        super();
        this.text = Validate.notNull(text);
    }

    /**
     * Returns an instance for the specified text.
     * <p>
     * Will return {@code null} if {@code text} is {@code null}.
     * 
     * @param text The fixed text for all languages
     * @return The instance for the specified text, or {@code null} if
     * {@code text} is {@code null}
     */
    @JsonCreator
    public static I18nFixedString from(
            final String text) {
        if (text == null) { return null; }
        final WeakReference<I18nFixedString> cachedRef = CACHE.get(text);
        I18nFixedString result = null;
        if (cachedRef != null) {
            result = cachedRef.get();
        }
        if (result == null) {
            result = new I18nFixedString(text);
            CACHE.put(text, new WeakReference<>(result));
        }
        return result;
    }

    /**
     * Creates a new instance with the translation for the current
     * language of the specified source {@code I18nString}.
     * <p>
     * Will return {@code null} if {@code source} is {@code null}.
     * 
     * @param source The source of the I18N text
     * @return The instance for the current language translation, or
     * {@code null} if {@code source} is {@code null}
     */
    public static I18nFixedString from(
            final I18nString source) {
        if (source == null) { return null; }
        return from(source.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get(
            final @NotNull String language) {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String get(
            final @NotNull Locale locale) {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.text)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (!getClass().equals(obj.getClass())) { return false; }
        final I18nFixedString other = (I18nFixedString) obj;
        return new EqualsBuilder()
                .append(this.text, other.text)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEquivalent(final I18nString obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        return get().equals(obj.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.text;
    }
}
