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

/**
 * Constants of XML schema for library beans 
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @see I18nString
 * @since 0.1
 */
public final class I18nXmlSchema {

    /** The package name space. */
    public static final String NS = "http://orne.dev/i18n/";
    /** The I18n string XML type local name. */
    public static final String STRING_TYPE = "string";
    /** The I18n string XML element local name. */
    public static final String STRING_ELEMENT = "string";
    /** The I18n string translation XML type local name. */
    public static final String TRANSLATION_TYPE = "translation";
    /** The I18n string translation XML element local name. */
    public static final String TRANSLATION_ELEMENT = "translation";
    /** The I18n string translation XML element language attribute. */
    public static final String TRANSLATION_LANG_ATTR = "lang";

    /**
     * Private constructor.
     */
    private I18nXmlSchema() {
        // Utility class
    }
}
