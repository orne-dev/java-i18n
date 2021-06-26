package dev.orne.i18n.validation;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

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

import dev.orne.i18n.I18nString;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Implementation of Jakarta Bean Validation {@code ConstraintValidator} for
 * {@code Size} constraint on {@code I18nString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see Size
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.MAINTAINED, since="0.1")
public class SizeValidatorForI18nString
extends AbstractValidatorForI18nString<Size> {

    private int min;
    private int max;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final @NotNull Size parameters) {
        this.min = parameters.min();
        this.max = parameters.max();
        validateParameters();
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isTextValid(
            final @NotNull String text) {
        int length = text.length();
        return length >= min && length <= max;
    }

    protected void validateParameters() {
        if ( this.min < 0 ) {
            throw new IllegalArgumentException("The min parameter cannot be negative.");
        }
        if ( this.max < 0 ) {
            throw new IllegalArgumentException("The max parameter cannot be negative.");
        }
        if ( this.max < this.min ) {
            throw new IllegalArgumentException("The min parameter cannot be greater that the max parameter.");
        }
    }
}
