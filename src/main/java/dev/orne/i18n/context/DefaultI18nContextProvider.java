package dev.orne.i18n.context;

import java.util.Properties;

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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Default implementation of {@code I18nContextProvider} that provides a
 * different {@code I18nContext} for each thread.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2022-12
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class DefaultI18nContextProvider
extends AbstractI18nContextProvider {

    /** The I18N context provider type. */
    public static final String TYPE = "DEFAULT";

    /** The {@code I18nContext}s per {@code Thread} container. */
    private final @NotNull ThreadLocal<I18nContext> contexts;

    /**
     * Creates a new instance with {@code I18nContext} instances inherited by
     * child {@code Thread}s
     */
    public DefaultI18nContextProvider() {
        this(true);
    }

    /**
     * Creates a new instance based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    public DefaultI18nContextProvider(
            final @NotNull Properties config) {
        super(config);
        if (!config.containsKey(I18nConfiguration.CONTEXT_INHERITED)) {
            this.contexts = new InheritableThreadLocal<>();
        } else if (Boolean.parseBoolean(config.getProperty(I18nConfiguration.CONTEXT_INHERITED))) {
            this.contexts = new InheritableThreadLocal<>();
        } else {
            this.contexts = new ThreadLocal<>();
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param inheritable If the {@code I18nContext} instances should be
     * inherited by child {@code Thread}s
     */
    public DefaultI18nContextProvider(
            final boolean inheritable) {
        super();
        if (inheritable) {
            this.contexts = new InheritableThreadLocal<>();
        } else {
            this.contexts = new ThreadLocal<>();
        }
    }

    /**
     * Returns {@code true} if the {@code I18nContext} instances will be
     * inherited by child threads.
     * 
     * @return If the {@code I18nContext} instances will be inherited by child
     * threads.
     */
    public boolean isInheritable() {
        return this.contexts instanceof InheritableThreadLocal;
    }

    /**
     * Returns the internal {@code I18nContext}s per {@code Thread} container.
     * 
     * @return The {@code I18nContext}s per {@code Thread} container
     */
    protected @NotNull ThreadLocal<I18nContext> getContexts() {
        return this.contexts;
    }

    /**
     * Return the {@code I18nContext} associated with the current
     * {@code Thread}.
     * <p>
     * If no {@code I18nContext} exists for the current {@code Thread} or the
     * existing one is not alive anymore a new one is created.
     * 
     * @return The current {@code I18nContext}. Never {@code null}.
     */
    public @NotNull I18nContext getContext() {
        I18nContext context = this.contexts.get();
        if (context == null || !isContextValid(context)) {
            context = createContext();
            this.contexts.set(context);
        }
        return context;
    }

    /**
     * The default contexts don't expire.
     */
    @Override
    public boolean isContextValid(
            final @NotNull I18nContext context) {
        return Validate.notNull(context) == this.contexts.get() &&
                getSessionUUID().equals(context.getProviderUUID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearContext() {
        this.contexts.remove();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(isInheritable())
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
        final DefaultI18nContextProvider other = (DefaultI18nContextProvider) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.isInheritable(), other.isInheritable())
                .isEquals();
    }

    /**
     * Factory for {@code DefaultI18nContextProvider} instances.
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
            return new DefaultI18nContextProvider(config);
        }
    }
}
