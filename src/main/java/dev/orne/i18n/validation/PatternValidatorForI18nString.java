package dev.orne.i18n.validation;

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

import java.util.regex.PatternSyntaxException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nString;

/**
 * Implementation of Javax Bean Validation {@code ConstraintValidator} for
 * {@code Pattern} constraint on {@code I18nString} instances.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see Pattern
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
public class PatternValidatorForI18nString
extends AbstractValidatorForI18nString<Pattern> {

    /** The regular expression to match. */
    private java.util.regex.Pattern pattern;

    /**
     * Creates a new instance.
     */
    public PatternValidatorForI18nString() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final @NotNull Pattern parameters) {
        final Pattern.Flag[] flags = parameters.flags();
        int intFlag = 0;
        for (final Pattern.Flag flag : flags) {
            intFlag = intFlag | flag.getValue();
        }
        try {
            pattern = java.util.regex.Pattern.compile(parameters.regexp(), intFlag);
        } catch (final PatternSyntaxException pse) {
            throw new IllegalArgumentException("Invalid regular expression.", pse);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isTextValid(
            final @NotNull String text) {
        return this.pattern.matcher(text).matches();
    }
}
