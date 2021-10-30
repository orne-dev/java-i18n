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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

import dev.orne.i18n.jaxb.javax.I18nResourcesStringAdapter;

/**
 * {@code I18nResourcesString} Jackson serialization test container.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.ROOT_ELEMENT)
@jakarta.xml.bind.annotation.XmlAccessorType(jakarta.xml.bind.annotation.XmlAccessType.FIELD)
@jakarta.xml.bind.annotation.XmlRootElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.ROOT_ELEMENT)
public class I18nResourcesStringAsObjectContainer {

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    @XmlJavaTypeAdapter(I18nResourcesStringAdapter.Full.class)
    @XmlElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.BEAN_ELEMENT)
    @jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(
            dev.orne.i18n.jaxb.I18nResourcesStringAdapter.Full.class)
    @jakarta.xml.bind.annotation.XmlElement(namespace=I18nStringContainer.TEST_NS, name=I18nStringContainer.BEAN_ELEMENT)
    private I18nResourcesString bean;

    public I18nResourcesString getBean() {
        return this.bean;
    }

    public void setBean(final I18nResourcesString bean) {
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
        final I18nResourcesStringAsObjectContainer other = (I18nResourcesStringAsObjectContainer) obj;
        return new EqualsBuilder()
                .append(this.bean, other.bean)
                .isEquals();
    }
}
