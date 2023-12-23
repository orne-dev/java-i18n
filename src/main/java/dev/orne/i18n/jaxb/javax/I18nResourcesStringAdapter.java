package dev.orne.i18n.jaxb.javax;

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

import dev.orne.i18n.I18nResourcesString;

/**
 * JAXB adapter for {@code I18nResourcesString} instances.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @see I18nResourcesString
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class I18nResourcesStringAdapter
extends XmlAdapter<XmlI18nString, I18nResourcesString> {

    /**
     * Creates a new instance.
     */
    public I18nResourcesStringAdapter() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public XmlI18nString marshal(final I18nResourcesString value) {
        return JaxbUtils.toXml(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public I18nResourcesString unmarshal(final XmlI18nString value) {
        throw new UnsupportedOperationException(
                "Unmarshalling I18nResourcesString from XmlI18NString is not supported");
    }

    /**
     * JAXB adapter for {@code I18nResourcesString} instances that marshalls
     * all available translations.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2021-10
     * @see I18nResourcesString
     * @since {@link I18nResourcesStringAdapter} 1.0
     */
    @API(status=Status.STABLE, since="0.1")
    public static class Full
    extends I18nResourcesStringAdapter {

        /**
         * Creates a new instance.
         */
        public Full() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public XmlI18nString marshal(final I18nResourcesString value) {
            return JaxbUtils.toFullXml(value);
        }
    }
}
