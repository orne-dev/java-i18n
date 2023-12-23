package dev.orne.i18n;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2023 Orne Developments
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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.Priority;
import dev.orne.test.rnd.params.AbstractTypedParameterizableGenerator;
import dev.orne.test.rnd.params.StringGenerationParameters;

/**
 * Generator of {@code I18nStringMap} values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
@Priority(Priority.NATIVE_GENERATORS)
public class I18nStringMapGenerator
extends AbstractTypedParameterizableGenerator<I18nStringMap, StringGenerationParameters> {

    /** The minimum random translations. */
    protected static final int MIN_TRANSLATIONS = 0;
    /** The maximum random translations. */
    protected static final int MAX_TRANSLATIONS = 5;

    /**
     * Creates a new instance.
     */
    public I18nStringMapGenerator() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nStringMap defaultValue(
            final @NotNull StringGenerationParameters parameters) {
        return new I18nStringMap(Generators.defaultValue(String.class, parameters));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nStringMap randomValue(
            final @NotNull StringGenerationParameters parameters) {
        final I18nStringMap value = new I18nStringMap(
                Generators.randomValue(String.class, parameters));
        final int translations = RandomUtils.nextInt(MIN_TRANSLATIONS, MAX_TRANSLATIONS + 1);
        for (int i = 0; i < translations; i++) {
            value.set(
                    Generators.randomValue(Locale.class),
                    Generators.randomValue(String.class, parameters));
        }
        return value;
    }
}
