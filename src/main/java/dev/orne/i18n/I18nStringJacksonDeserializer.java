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
import java.util.HashMap;
import java.util.Map;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import jakarta.validation.constraints.NotNull;

/**
 * Jackson JSON deserializer for {@code I18nString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @see I18nString
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
public class I18nStringJacksonDeserializer
extends JsonDeserializer<I18nString>
implements ContextualDeserializer {

    /** The by target type cache. */
    private final Map<Class<?>, I18nStringJacksonDeserializer> cache =
            new HashMap<>();
    /** The converter from {@code I18nString} to the target type. */
    private final I18nStringConverter converter;

    /**
     * Creates a new instance.
     */
    public I18nStringJacksonDeserializer() {
        super();
        this.converter = null;
    }

    /**
     * Creates a new instance.
     * 
     * @param converter The converter from {@code I18nString} to the target type
     */
    protected I18nStringJacksonDeserializer(
            final I18nStringConverter converter) {
        super();
        this.converter = converter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonDeserializer<?> createContextual(
            final @NotNull DeserializationContext ctxt,
            final @NotNull BeanProperty property)
    throws JsonMappingException {
        final Class<?> targetType;
        if (property == null) {
            targetType = ctxt.getContextualType().getRawClass();
        } else {
            targetType = property.getType().getRawClass();
        }
        if (!I18nString.class.equals(targetType) ) {
            return cache.computeIfAbsent(targetType, type -> {
                final I18nStringConverter typeConverter = getConversor(targetType);
                if (typeConverter == null) {
                    return this;
                } else {
                    return new I18nStringJacksonDeserializer(typeConverter);
                }
            });
        }
        return this;
    }

    protected I18nStringConverter getConversor(
            final @NotNull Class<?> targetType) {
        final I18nStringConverter result;
        if (I18nFixedString.class.equals(targetType)) {
            result = I18nFixedString::from;
        } else if (I18nStringMap.class.equals(targetType)) {
            result = I18nStringMap::new;
        } else {
            result = null;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nString deserialize(
            final @NotNull JsonParser parser,
            final @NotNull DeserializationContext context)
    throws IOException {
        I18nString result = null;
        if (parser.hasCurrentToken()) {
            JsonToken token = parser.getCurrentToken();
            if (token == JsonToken.START_OBJECT) {
                result = new I18nStringMap(parser.readValueAs(MapFromObject.class));
            } else if (token == JsonToken.VALUE_STRING) {
                result = I18nFixedString.from(parser.getText());
            } else if (token != JsonToken.VALUE_NULL) {
                throw new JsonParseException(parser, "Unexpected token.", parser.getTokenLocation());
            }
        }
        if (this.converter != null) {
            result = this.converter.convert(result);
        }
        return result;
    }

    /**
     * Extension of {@code I18nStringMap} for deserialization from
     * JSON object.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-02
     * @see I18nStringMap
     * @since 0.1
     */
    @API(status=Status.INTERNAL, since="0.1")
    @JsonDeserialize(using = JsonDeserializer.None.class)
    private static  class MapFromObject
    extends I18nStringMap {

        /** The serial version UID. */
        private static final long serialVersionUID = 1L;
    }
}
