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

import java.beans.PropertyEditorSupport;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Java Beans Editor for {@code I18nString} that converts between
 * {@code String} and {@code I18nFixedString}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 */
@API(status=Status.INTERNAL, since="0.1")
public class I18nStringEditor
extends PropertyEditorSupport {

    /**
     * Creates a new instance.
     */
    public I18nStringEditor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(final String text) {
        if (text == null) {
            super.setValue(null);
        } else {
            super.setValue(I18nFixedString.from(text));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAsText() {
        final I18nString value = (I18nString) getValue();
        if (value == null) {
            return null;
        } else {
            return value.get();
        }
    }
}
