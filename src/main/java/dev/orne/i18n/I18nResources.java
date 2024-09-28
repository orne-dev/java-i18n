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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.context.I18nContext;

/**
 * Interface for I18N messages source. Provides an implementation agnostic way
 * to retrieve I18N messages based in the current {@code I18nContext} locale.
 * <p>
 * The default messages are expect to be {@code MessageFormat} compliant.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @see MessageFormat
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public interface I18nResources {

    /**
     * Retrieves the I18N message for the specified message code and the 18N
     * context locale.
     * If the message code is not defined in this I18N resources the specified
     * default message is used.
     * The message is formatted with the provided parameters.
     * 
     * @param defaultMessage The default message format
     * @param code The message code
     * @param params The message parameters
     * @return The formatted message in the I18N context locale
     */
    default @NotNull String getMessage(
            @NotNull String defaultMessage,
            @NotNull String code,
            Object... params) {
        return getMessage(defaultMessage, code, I18N.getLocale(), params);
    }

    /**
     * Retrieves the I18N message for the specified message code and locale.
     * If the message code is not defined in this I18N resources the specified
     * default message is used.
     * The message is formatted with the provided parameters.
     * 
     * @param defaultMessage The default message format
     * @param code The message code
     * @param locale The locale to retrieve the message for
     * @param params The message parameters
     * @return The formatted message in the specified locale
     */
    @NotNull String getMessage(
            @NotNull String defaultMessage,
            @NotNull String code,
            @NotNull Locale locale,
            Object... params);

    /**
     * Retrieves the I18N message for the specified locale of the first
     * message code efined in this I18N resources of specified message codes.
     * If no message code is defined in this I18N resources the specified
     * default message is used.
     * The message is formatted with the provided parameters.
     * 
     * @param defaultMessage The default message format
     * @param codes The message codes, in order of preference
     * @param params The message parameters
     * @return The formatted message in the I18N context locale
     */
    default @NotNull String getMessage(
            @NotNull String defaultMessage,
            @NotNull String[] codes,
            Object... params) {
        return getMessage(defaultMessage, codes, I18N.getLocale(), params);
    }

    /**
     * Retrieves the I18N message for the specified locale of the first
     * message code efined in this I18N resources of specified message codes.
     * If no message code is defined in this I18N resources the specified
     * default message is used.
     * The message is formatted with the provided parameters.
     * 
     * 
     * @param defaultMessage The default message format
     * @param codes The message codes, in order of preference
     * @param locale The locale to retrieve the message for
     * @param params The message parameters
     * @return The formatted message in the specified locale
     */
    @NotNull String getMessage(
            @NotNull String defaultMessage,
            @NotNull String[] codes,
            @NotNull Locale locale,
            Object... params);
}
