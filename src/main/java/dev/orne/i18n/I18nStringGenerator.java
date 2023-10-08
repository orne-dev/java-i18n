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
 * Generator of {@code I18nFixedString} values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Priority(Priority.NATIVE_GENERATORS)
public class I18nStringGenerator
extends AbstractTypedParameterizableGenerator<I18nString, StringGenerationParameters> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nString defaultValue(
            final @NotNull StringGenerationParameters parameters) {
        return Generators.defaultValue(I18nFixedString.class, parameters);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nString randomValue(
            final @NotNull StringGenerationParameters parameters) {
        final int type = RandomUtils.nextInt(0, 3);
        switch (type) {
            case 0:
                return Generators.randomValue(I18nFixedString.class, parameters);
            case 1:
                return Generators.randomValue(I18nStringMap.class, parameters);
            default:
                return Generators.randomValue(I18nResourcesString.class, parameters);
        }
    }
}
