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
module dev.orne.i18n {
    exports dev.orne.i18n;
    exports dev.orne.i18n.spring;
    exports dev.orne.i18n.validation;
    exports dev.orne.i18n.validation.javax;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires static org.apiguardian.api;
    // Java Beans Support
    requires static java.desktop;
    // JAXB Support
    requires static java.xml.bind;
    requires static org.apache.commons.text;
    // Java Validation Support
    requires static java.validation;
    // Jakarta Validation Support
    requires static jakarta.validation;
    // Jackson JSON Support
    requires static transitive com.fasterxml.jackson.databind;
    opens dev.orne.i18n to com.fasterxml.jackson.databind;
    // Spring Support
    requires static spring.beans;
    requires static transitive spring.core;
    requires static spring.context;
}
