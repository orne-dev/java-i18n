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

import java.io.IOException;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import jakarta.validation.constraints.NotNull;

/**
 * Jackson JSON serializer for {@code I18nString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
public class I18nStringJacksonSerializer
extends StdSerializer<I18nString>
implements ContextualSerializer {

    /** The serial version UID.  */
    private static final long serialVersionUID = 1L;
    /** The shared instance for {@code } as object serialization. */
    private static final I18nStringJacksonSerializer OBJECT_INSTANCE =
            new I18nStringJacksonSerializer(true);

    /** If {@code I18nStringMap} instances should be serialized as objects. */
    private final boolean mapsAsObject;

    /**
     * Creates a new instance that serializes all instances as current language
     * translation text.
     */
    public I18nStringJacksonSerializer() {
        super(I18nString.class);
        this.mapsAsObject = false;
    }

    /**
     * Creates a new instance.
     * 
     * @param mapsAsObject If {@code I18nStringMap} instances should be
     * serialized as objects
     */
    protected I18nStringJacksonSerializer(
            final boolean mapsAsObject) {
        super(I18nString.class);
        this.mapsAsObject = mapsAsObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonSerializer<?> createContextual(
            final @NotNull SerializerProvider provider,
            final @NotNull BeanProperty property)
    throws JsonMappingException {
        final JsonFormat.Value format = findFormatOverrides(provider, property, handledType());
        if (format != null) {
            final JsonFormat.Shape shape = format.getShape();
            if (JsonFormat.Shape.OBJECT == shape) {
                return OBJECT_INSTANCE;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(
            final @NotNull I18nString value,
            final @NotNull JsonGenerator jgen,
            final @NotNull SerializerProvider provider)
    throws IOException {
        if (this.mapsAsObject) {
            provider.defaultSerializeValue(new MapAsObject(value.asMap()), jgen);
        } else {
            jgen.writeString(value.get());
        }
    }

    /**
     * Extension of {@code I18nStringMap} for serialization as
     * JSON object.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-02
     * @see I18nStringMap
     * @since 0.1
     */
    @API(status=Status.INTERNAL, since="0.1")
    @JsonSerialize(using = JsonSerializer.None.class)
    private static class MapAsObject
    extends I18nStringMap {

        /** The serial version UID. */
        private static final long serialVersionUID = 1L;

        public MapAsObject(
                final @NotNull I18nString copy) {
            super(copy);
        }
    }
}
