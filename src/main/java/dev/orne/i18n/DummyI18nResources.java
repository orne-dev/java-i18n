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

import java.text.MessageFormat;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import jakarta.validation.constraints.NotNull;

/**
 * Dummy implementation of {@code I18nResources} that always returns the
 * {@code defaultMessage}, formated with passed arguments.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nResources
 * @since 0.1
 */
public class DummyI18nResources
implements I18nResources {

    /** The invariant shared instance. */
    public static final DummyI18nResources INSTANCE = new DummyI18nResources();

    /**
     * Private constructor. Use shared instance.
     * 
     * @see DummyI18nResources#INSTANCE
     */
    private DummyI18nResources() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final String code,
            final Object... params) {
        Validate.notNull(defaultMessage, "The default message cannot be null");
        try {
            return MessageFormat.format(defaultMessage, params);
        } catch (final IllegalArgumentException ignore) {
            return defaultMessage;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final String code,
            final Locale locale,
            final Object... params) {
        return getMessage(defaultMessage, (String) null, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final String[] codes,
            final Object... params) {
        return getMessage(defaultMessage, (String) null, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final String[] codes,
            final Locale locale,
            final Object... params) {
        return getMessage(defaultMessage, (String) null, params);
    }
}
