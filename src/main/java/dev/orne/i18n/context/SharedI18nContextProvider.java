package dev.orne.i18n.context;

import java.util.Properties;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2023 Orne Developments
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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Implementation of {@code I18nContextProvider} that shares a single
 * {@code I18nContext} instance for all threads.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2022-10
 * @see I18nContextProvider
 * @since 0.2
 */
@API(status=Status.STABLE, since="0.1")
public class SharedI18nContextProvider
extends AbstractI18nContextProvider {

    /** The I18N context provider type. */
    public static final String TYPE = "SHARED";

    /** The shared I18N context. */
    private I18nContext context;

    /**
     * Creates a new instance.
     */
    public SharedI18nContextProvider() {
        super();
    }

    /**
     * Creates a new instance based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    public SharedI18nContextProvider(
            final @NotNull Properties config) {
        super(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized @NotNull I18nContext getContext() {
        if (this.context == null) {
            this.context = createContext();
        }
        return this.context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isContextValid(
            final @NotNull I18nContext context) {
        return this.context == context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void clearContext() {
        this.context = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.context)
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
        final SharedI18nContextProvider other = (SharedI18nContextProvider) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.context, other.context)
                .isEquals();
    }

    /**
     * Factory for {@code SharedI18nContextProvider} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-08
     * @since 0.1
     */
    public static class Factory
    implements I18nContextProviderFactory {

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull String getType() {
            return TYPE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull I18nContextProvider create(
                final @NotNull Properties config) {
            return new SharedI18nContextProvider(config);
        }
    }
}
