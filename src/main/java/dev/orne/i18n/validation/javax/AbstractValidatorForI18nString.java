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

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

import dev.orne.i18n.I18nString;
import dev.orne.i18n.I18nStringMap;

/**
 * Abstract implementation of Javax Bean Validation
 * {@code ConstraintValidator} for {@code I18nString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @param <T> The constraint type to validate
 * @see ConstraintValidator
 * @see I18nString
 * @since 0.1
 */
public abstract class AbstractValidatorForI18nString<T extends Annotation>
implements ConstraintValidator<T, I18nString> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid(
            final I18nString value,
            final @NotNull ConstraintValidatorContext context) {
        if ( value == null ) {
            return true;
        }
        boolean valid = true;
        if (value instanceof I18nStringMap) {
            final I18nStringMap map = (I18nStringMap) value;
            valid = isTextValid(map.getDefaultText());
            if (valid) {
                for (final String translation : map.getI18n().values()) {
                    valid = isTextValid(translation);
                    if (!valid) {
                        break;
                    }
                }
            }
        } else {
            valid = isTextValid(value.get());
        }
        return valid;
    }

    /**
     * Validates if the text is valid.
     * 
     * @param text The text to validate
     * @return If the text is valid
     */
    protected abstract boolean isTextValid(
            @NotNull String text);
}
