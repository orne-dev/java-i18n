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

import java.io.Serializable;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Default implementation of {@code I18nContext}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContext
 * @since 0.1
 */
public class DefaultI18nContext
implements I18nContext, Serializable {

    /** The serial verion UID. */
    private static final long serialVersionUID = 1L;

    /** The user's locale. */
    private @NotNull Locale locale = Locale.getDefault();
    /** If all available translations should be retrieved. */
    private boolean fullMode;

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Locale getLocale() {
        return this.locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocale(final Locale locale) {
        if (locale == null) {
            this.locale = Locale.getDefault();
        } else {
            this.locale = locale;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFullMode() {
        return this.fullMode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFullMode(final boolean value) {
        this.fullMode = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.locale)
                .append(this.fullMode)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (!getClass().equals(obj.getClass())) { return false; }
        final DefaultI18nContext other = (DefaultI18nContext) obj;
        return new EqualsBuilder()
                .append(this.locale, other.locale)
                .append(this.fullMode, other.fullMode)
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("locale", this.locale)
                .append("fullMode", this.fullMode)
                .toString();
    }
}
