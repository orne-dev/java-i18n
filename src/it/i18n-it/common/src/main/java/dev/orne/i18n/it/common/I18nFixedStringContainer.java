package dev.orne.i18n.it.common;

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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import dev.orne.i18n.I18nFixedString;

/**
 * {@code I18nFixedString} JSON/XML serialization test container.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.ROOT_ELEMENT)
public class I18nFixedStringContainer {

    @XmlElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.BEAN_ELEMENT)
    private I18nFixedString bean;

    public I18nFixedString getBean() {
        return this.bean;
    }

    public void setBean(final I18nFixedString bean) {
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
        final I18nFixedStringContainer other = (I18nFixedStringContainer) obj;
        return new EqualsBuilder()
                .append(this.bean, other.bean)
                .isEquals();
    }
}
