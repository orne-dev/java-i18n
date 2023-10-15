package dev.orne.i18n.spring;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.RequestHandledEvent;

import dev.orne.i18n.I18N;

/**
 * Listener for Spring web contexts that removes the I18N context when the HTTP
 * request has been handled.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-05
 * @since 0.1
 */
public class I18nSpringWebContextClearer
implements ApplicationListener<RequestHandledEvent> {

    /** El logger de la clase.*/
    private static final Logger LOG = LoggerFactory.getLogger(I18nSpringWebContextClearer.class);

    /**
     * {@inheritDoc}
     * <p>
     * Clears the I18N context.
     */
    @Override
    public void onApplicationEvent(
            final RequestHandledEvent event) {
        LOG.debug("Clearing I18nContext...");
        I18N.clearContext();
    }
}
