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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomUtils;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.test.rnd.Generators;
import dev.orne.test.rnd.Priority;
import dev.orne.test.rnd.params.AbstractTypedParameterizableGenerator;
import dev.orne.test.rnd.params.StringGenerationParameters;

/**
 * Generator of {@code I18nResourcesString} values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Priority(Priority.NATIVE_GENERATORS)
public class I18nResourcesStringGenerator
extends AbstractTypedParameterizableGenerator<I18nResourcesString, StringGenerationParameters> {

    /** The minimum random message codes. */
    protected static final int MIN_CODES = 1;
    /** The maximum random message codes. */
    protected static final int MAX_CODES = 3;
    /** The minimum random message arguments. */
    protected static final int MIN_ARGS = 0;
    /** The maximum random message arguments. */
    protected static final int MAX_ARGS = 3;

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nResourcesString defaultValue(
            final @NotNull StringGenerationParameters parameters) {
        return I18nResourcesString
                .forDefault(Generators.defaultValue(String.class, parameters))
                .withCode(Generators.defaultValue(String.class))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nResourcesString randomValue(
            final @NotNull StringGenerationParameters parameters) {
        final I18nResourcesString.Builder builder = I18nResourcesString
                .forDefault(Generators.randomValue(String.class, parameters))
                .withCode(Generators.randomValue(String.class));
        final int codes = RandomUtils.nextInt(MIN_CODES, MAX_CODES + 1);
        for (int i = 1; i < codes; i++) {
            builder.withCode(Generators.randomValue(String.class));
        }
        if (RandomUtils.nextBoolean()) {
            builder.ofResources(Generators.randomValue(String.class));
        }
        final int args = RandomUtils.nextInt(MIN_ARGS, MAX_ARGS + 1);
        for (int i = 0; i < args; i++) {
            builder.withArg(Generators.randomValue(String.class));
        }
        return builder.build();
    }
}
