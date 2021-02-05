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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@code I18nString} JSON/XML serialization test container.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
@XmlRootElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.ROOT_ELEMENT)
public class I18nStringContainer {

    public static final String TEST_NS = "http://orne.dev/i18n/test";
    public static final String ROOT_ELEMENT = "container";
    public static final String BEAN_ELEMENT = "bean";
    public static final String BEAN_PROPERTY = "bean";

    private I18nString bean;

    public I18nString getBean() {
        return this.bean;
    }

    @XmlElement(namespace=TEST_NS, name=BEAN_ELEMENT)
    public void setBean(final I18nString bean) {
        this.bean = bean;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.bean)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (!getClass().equals(obj.getClass())) { return false; }
        final I18nStringContainer other = (I18nStringContainer) obj;
        return new EqualsBuilder()
                .append(this.bean, other.bean)
                .isEquals();
    }
}
