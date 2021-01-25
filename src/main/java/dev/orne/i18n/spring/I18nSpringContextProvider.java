package dev.orne.i18n.spring;

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

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import dev.orne.i18n.DefaultI18nContextProvider;
import dev.orne.i18n.I18nContext;
import dev.orne.i18n.I18nContextProvider;

/**
 * Implementation of {@code I18nContextProvider} for Spring contexts.
 * <p>
 * Uses {code I18nSpringContext} as context implementation and supports
 * {@code MessageSource} auto-wiring for default I18N resources lookup.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @see I18nSpringContext
 * @see I18nSpringResources
 * @since 0.1
 */
public class I18nSpringContextProvider
extends DefaultI18nContextProvider {

    /**
     * Creates a new instance with contexts inherited by child {@code Thread}s.
     */
    public I18nSpringContextProvider() {
        super();
    }

    /**
     * Creates a new instance with contexts inherited by child {@code Thread}s
     * and default I18N based on specified {@code MessageSource}.
     * <p>
     * If no {@code MessageSource} is passed default dummy I18N resources are
     * used.
     * <p>
     * Spring auto-wiring support constructor.
     * 
     * @param messageSource The {@code MessageSource} used to create default
     * I18N resources
     */
    @Autowired(required=false)
    public I18nSpringContextProvider(
            final MessageSource messageSource) {
        super();
        if (messageSource != null) {
            setDefaultI18nResources(new I18nSpringResources(messageSource));
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param inheritable If the {@code I18nContext} instances should be
     * inherited by child {@code Thread}s
     */
    public I18nSpringContextProvider(
            final boolean inheritable) {
        super(inheritable);
    }

    /**
     * Creates a new instance with default I18N based on specified
     * {@code MessageSource}.
     * 
     * @param messageSource The {@code MessageSource} used to create default
     * I18N resources
     * @param inheritable If the {@code I18nContext} instances should be
     * inherited by child {@code Thread}s
     */
    public I18nSpringContextProvider(
            final @NotNull MessageSource messageSource,
            final boolean inheritable) {
        super(inheritable);
        setDefaultI18nResources(new I18nSpringResources(
                Validate.notNull(messageSource)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nContext createContext() {
        final I18nSpringContext result = new I18nSpringContext(getSessionUUID());
        result.setFullMode(isFullModeByDefault());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nContext createContext(
            final @NotNull I18nContext parent) {
        final I18nSpringContext result = new I18nSpringContext(getSessionUUID());
        result.setFullMode(Validate.notNull(parent).isFullMode());
        return result;
    }
}
