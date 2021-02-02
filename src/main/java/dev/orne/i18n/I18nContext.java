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
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Interface for I18N context. Contains the data required to return the I18N
 * texts in user's language.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
public interface I18nContext {

    /**
     * Returns the UUID of the provider owner of this context.
     * 
     * @return The UUID of the provider owner of this context
     */
    @NotNull UUID getProviderUUID();

    /**
     * Returns the user's language.
     * 
     * @return The user's language
     */
    @NotNull Locale getLocale();

    /**
     * Sets the user's language. If the argument is {@code null} the JVM
     * default language is set.
     * 
     * @param language The user's language to set
     */
    void setLocale(Locale language);

    /**
     * Returns if all available translations should be retrieved.
     * 
     * @return {@code true} if all available translations should be retrieved.
     * {@code false} if only the translation in user locale is required
     */
    boolean isFullMode();

    /**
     * Sets if all available translations should be retrieved.
     * <p>
     * If {@code true} all available translations should be retrieved.
     * Otherwise only the translation in user locale is required.
     * 
     * @param value If all available translations should be retrieved
     */
    void setFullMode(boolean value);
}
