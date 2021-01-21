package dev.orne.i18n.spring;

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

import java.text.MessageFormat;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nResources;

/**
 * Implementation of {@code I18nResources} based on Spring
 * {@code MessageSource}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nResources
 * @see MessageSource
 * @since 0.1
 */
public class I18nSpringResources
implements I18nResources {

    /** The I18N messages source. */
    private final @NotNull MessageSource source;

    /**
     * Creates a new instance.
     * 
     * @param source The I18N messages source
     */
    public I18nSpringResources(
            final @NotNull MessageSource source) {
        super();
        this.source = Validate.notNull(source);
    }

    /**
     * Returns the I18N messages source.
     * 
     * @return The I18N messages source
     */
    public MessageSource getSource() {
        return this.source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String code,
            final Object... params) {
        return getMessage(defaultMessage, code, I18N.getLocale(), params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String code,
            final @NotNull Locale locale,
            final Object... params) {
        return getMessage(defaultMessage, new String[] { code }, locale, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String[] codes,
            final Object... params) {
        return getMessage(defaultMessage, codes, I18N.getLocale(), params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String[] codes,
            final @NotNull Locale locale,
            final Object... params) {
        String result = null;
        for (final String code : codes) {
            try {
                result = this.source.getMessage(code, params, locale);
                break;
            } catch (final NoSuchMessageException ignore) {
                // Ignored
            }
        }
        if (result == null) {
            try {
                result = MessageFormat.format(defaultMessage, params);
            } catch (final IllegalArgumentException ignore) {
                result = defaultMessage;
            }
        }
        return result;
    }
}
