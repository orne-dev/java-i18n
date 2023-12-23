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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Functional interface for methods able to convert from any {@code I18nString}
 * to the desired {@code I18nString} subtype.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public @FunctionalInterface interface I18nStringConverter {

    /**
     * Converts the specified {@code I18nString} to an instance of the
     * desired {@code I18nString} subtype.
     * 
     * @param value The original {@code I18nString} instance
     * @return The converted {@code I18nString} instance
     */
    @NotNull I18nString convert(@NotNull I18nString value);
}
