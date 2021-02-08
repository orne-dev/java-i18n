package dev.orne.i18n.validation.javax;

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

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import dev.orne.i18n.I18nString;

/**
 * Implementation of Javax Bean Validation {@code ConstraintValidator} for
 * {@code NotEmpty} constraint on {@code I18nString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see NotEmpty
 * @see I18nString
 * @since 0.1
 */
public class NotEmptyValidatorForI18nString
extends AbstractValidatorForI18nString<NotEmpty> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(
            final I18nString value,
            final @NotNull ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return super.isValid(value, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTextValid(
            final @NotNull String text) {
        return text.length() > 0;
    }
}
