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

import jakarta.xml.bind.annotation.XmlRegistry;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * This object contains factory methods for each Java content interface and
 * Java element interface in the dev.orne.i18n package.
 * <p>
 * An ObjectFactory allows you to programmatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing
 * the binding of schema type definitions, element declarations and model
 * groups.
 * Factory methods for each of these are provided in this class.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @since 0.1
 */
@XmlRegistry
@API(status=Status.INTERNAL, since="0.1")
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: dev.orne.i18n.
     */
    public ObjectFactory() {
        super();
    }

    /**
     * Create an instance of {@link XmlI18nString}.
     * 
     * @return The instance of {@link XmlI18nString}.
     */
    public XmlI18nString createXmlI18NString() {
        return new XmlI18nString();
    }

    /**
     * Create an instance of {@link XmlI18nStringTranslation}.
     * 
     * @return The instance of {@link XmlI18nStringTranslation}.
     */
    public XmlI18nStringTranslation createTranslation() {
        return new XmlI18nStringTranslation();
    }
}
