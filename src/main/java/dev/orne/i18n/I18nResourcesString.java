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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Implementation of {@code I18nString} based on {@code I18nResources}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
public class I18nResourcesString
implements I18nString {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The key of the I18N resources to use. */
    private final String i18nResourcesKey;
    /** The default text. */
    private final @NotNull String defaultText;
    /** The I18N text codes, in order of priority. */
    private final @NotNull String[] codes;
    /** The text arguments, applied to resolved text or default text. */
    private final @NotNull Serializable[] arguments;

    /**
     * Creates a new instance.
     * 
     * @param resources The I18N resources to use. If {@code null} will use
     * default ones
     * @param defaultMessage The default text
     * @param codes The I18N text codes, in order of priority
     * @param arguments The text arguments, applied to resolved text or
     * default text
     */
    public I18nResourcesString(
            final String resources,
            final @NotNull String defaultMessage,
            final @NotNull String[] codes,
            final Serializable... arguments) {
        super();
        this.defaultText = Validate.notNull(defaultMessage);
        Validate.notNull(codes);
        Validate.noNullElements(codes);
        this.codes = Arrays.copyOf(codes, codes.length);
        Validate.notNull(arguments);
        this.arguments = Arrays.copyOf(arguments, arguments.length);
        this.i18nResourcesKey = resources;
    }

    /**
     * Creates a builder for of a new instance with the specified default
     * text.
     * 
     * @param defaultMessage The default text
     * @return The new instance builder
     */
    public static Builder forDefault(
            final @NotNull String defaultMessage) {
        return new Builder(defaultMessage);
    }

    /**
     * Returns the key of the I18N resources to use.
     * If {@code null} default ones will be used.
     * 
     * @return The key of the I18N resources to use
     */
    public String getI18nResourcesKey() {
        return this.i18nResourcesKey;
    }

    /**
     * Returns the unformatted default text.
     * 
     * @return The unformatted default text
     */
    public @NotNull String getDefaultText() {
        return this.defaultText;
    }

    /**
     * Returns the formatted default text.
     * 
     * @return The formatted default text
     */
    public @NotNull String getFormattedDefaultText() {
        try {
            return MessageFormat.format(this.defaultText, (Object[]) this.arguments);
        } catch (final IllegalArgumentException ignore) {
            return this.defaultText;
        }
    }

    /**
     * Returns the I18N text codes, in order of priority.
     * 
     * @return The I18N text codes
     */
    public @NotNull String[] getCodes() {
        return Arrays.copyOf(this.codes, this.codes.length);
    }

    /**
     * Returns the text arguments, applied to resolved text or default
     * text.
     * 
     * @return The text arguments
     */
    public @NotNull Serializable[] getArguments() {
        return Arrays.copyOf(this.arguments, this.arguments.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get() {
        return I18N.getI18nResources(this.i18nResourcesKey).getMessage(
                this.defaultText,
                this.codes,
                (Object[]) this.arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final @NotNull String language) {
        return I18N.getI18nResources(this.i18nResourcesKey).getMessage(
                this.defaultText,
                this.codes,
                new Locale(Validate.notNull(language)),
                (Object[]) this.arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(final @NotNull Locale locale) {
        return I18N.getI18nResources(this.i18nResourcesKey).getMessage(
                this.defaultText,
                this.codes,
                Validate.notNull(locale),
                (Object[]) this.arguments);
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
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.defaultText)
                .append(this.codes)
                .append(this.arguments)
                .append(this.i18nResourcesKey)
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
        final I18nResourcesString other = (I18nResourcesString) obj;
        return new EqualsBuilder()
                .append(this.defaultText, other.defaultText)
                .append(this.codes, other.codes)
                .append(this.arguments, other.arguments)
                .append(this.i18nResourcesKey, other.i18nResourcesKey)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getFormattedDefaultText();
    }

    /**
     * Builder of {@code I18nResourcesString} instances. 
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-01
     * @since 0.1
     */
    public static class Builder {

        /** The key of the I18N resources to use. */
        private String i18nResourcesKey;
        /** The default text. */
        private final String defaultText;
        /** The I18N text codes, in order of priority. */
        private final List<String> codes = new ArrayList<>();
        /** The text arguments, applied to resolved text or default text. */
        private final List<Serializable> arguments = new ArrayList<>();

        /**
         * Creates a new {@code I18nResourcesString} builder with the specified
         * default text.
         * 
         * @param defaultText The default text
         */
        public Builder(final @NotNull String defaultText) {
            super();
            this.defaultText = Validate.notNull(defaultText);
        }

        /**
         * Sets the I18N resources to use. If {@code null} will use
         * default ones.
         * 
         * @param key The I18N resources to use
         * @return This builder for method chaining
         */
        public Builder ofResources(final String key) {
            this.i18nResourcesKey = key;
            return this;
        }

        /**
         * Appends the specified text code to the codes configured of this
         * builder.
         * 
         * @param code The I18N text code
         * @return This builder for method chaining
         */
        public Builder withCode(final @NotNull String code) {
            this.codes.add(Validate.notNull(code));
            return this;
        }

        /**
         * Appends the specified text codes to the codes configured
         * of this builder. No {@code null} codes are allowed.
         * 
         * @param codes The I18N text codes, in order of priority
         * @return This builder for method chaining
         */
        public Builder withCodes(final @NotNull String... codes) {
            this.codes.addAll(Arrays.asList(Validate.noNullElements(codes)));
            return this;
        }

        /**
         * 
         * @param arg The text argument, applied to resolved or
         * default text
         * @return This builder for method chaining
         */
        public Builder withArg(final Serializable arg) {
            this.arguments.add(arg);
            return this;
        }

        /**
         * 
         * @param args The text arguments, applied to resolved text or
         * default text
         * @return This builder for method chaining
         */
        public Builder withArgs(final Serializable... args) {
            this.arguments.addAll(Arrays.asList(args));
            return this;
        }

        /**
         * Returns the key of the I18N resources to use.
         * 
         * @return The key of the I18N resources to use
         */
        protected String getI18nResourcesKey() {
            return this.i18nResourcesKey;
        }

        /**
         * Returns the default text.
         * 
         * @return The default text
         */
        protected String getDefaultText() {
            return this.defaultText;
        }

        /**
         * Returns the I18N text codes, in order of priority.
         * 
         * @return The I18N text codes, in order of priority
         */
        protected List<String> getCodes() {
            return this.codes;
        }

        /**
         * Returns the text arguments, applied to resolved or
         * default text.
         * 
         * @return The text arguments
         */
        protected List<Serializable> getArguments() {
            return this.arguments;
        }

        /**
         * Builds a {@code I18nResourcesString} with the configured properties.
         * Validates that the default text and that at least one text code
         * are set.
         * 
         * @return The {@code I18nResourcesString} with the configured
         * properties
         */
        public I18nResourcesString build() {
            Validate.notEmpty(codes);
            return new I18nResourcesString(
                    this.i18nResourcesKey,
                    this.defaultText,
                    codes.toArray(new String[0]),
                    arguments.toArray(new Serializable[0]));
        }
    }
}
