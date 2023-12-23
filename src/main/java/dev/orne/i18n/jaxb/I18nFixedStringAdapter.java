package dev.orne.i18n.jaxb;

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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nFixedString;

/**
 * JAXB adapter for {@code I18nFixedString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @see I18nFixedString
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class I18nFixedStringAdapter
extends XmlAdapter<XmlI18nString, I18nFixedString> {

    /**
     * Creates a new instance.
     */
    public I18nFixedStringAdapter() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XmlI18nString marshal(final I18nFixedString value) {
        return JaxbUtils.toXml(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nFixedString unmarshal(final XmlI18nString value) {
        return I18nFixedString.from(JaxbUtils.fromXml(value));
    }
}
