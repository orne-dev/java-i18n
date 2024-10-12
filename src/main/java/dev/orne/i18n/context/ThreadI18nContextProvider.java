package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2024 Orne Developments
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

import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Implementation of {@code I18nContextProvider} that provides a
 * different {@code I18nContext} for each thread.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 2.0, 2022-12
 * @see I18nContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class ThreadI18nContextProvider
extends AbstractI18nContextProvider {

    /** The I18N context provider type. */
    public static final String TYPE = "THREAD";

    /** The {@code I18nContext}s per {@code Thread} container. */
    private final @NotNull ThreadLocal<I18nContext> contexts;

    /**
     * Creates a new instance based on specified builder.
     * 
     * @param builder The I18N context provider builder.
     */
    protected ThreadI18nContextProvider(
            @NotNull BuilderImpl<?, ?> builder) {
        super(builder);
        if (builder.inheritableContexts) {
            this.contexts = new InheritableThreadLocal<>();
        } else {
            this.contexts = new ThreadLocal<>();
        }
    }

    /**
     * Creates a new {@code ThreadI18nContextProvider} instances builder.
     * 
     * @return The {@code ThreadI18nContextProvider} instances builder.
     */
    public static @NotNull Builder builder() {
        return new BuilderImpl<>();
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
        final ThreadI18nContextProvider other = (ThreadI18nContextProvider) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.isInheritable(), other.isInheritable())
                .isEquals();
    }

    /**
     * Interface for {@code ThreadI18nContextProvider} instance builders.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @since 0.1
     */
    public interface Builder
    extends I18nContextProvider.Builder {

        /**
         * {@inheritDoc}
         */
        @Override
        default @NotNull Builder configure() {
            return configure(I18nConfiguration.get());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder configure(
                @NotNull Properties config);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder setDefaultLocaleSupplier(
                @NotNull Supplier<@NotNull Locale> supplier);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder setAvailableLocales(
                @NotNull Locale[] locales);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder setDefaultI18nResources(
                @NotNull I18nResources resources);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder addI18nResources(
                @NotNull String key,
                @NotNull I18nResources resource);

        /**
         * Sets if the {@code I18nContext} instances will be inherited by child
         * threads.
         * 
         * @param inheritable If the {@code I18nContext} instances will be
         * inherited by child threads.
         * @return This builder, for method chaining.
         */
        @NotNull Builder setInheritableContexts(
                @NotNull boolean inheritable);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull ThreadI18nContextProvider build();
    }

    /**
     * Abstract builder of {@code ThreadI18nContextProvider} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @param <T> The type of I18N context provider build by the builder.
     * @param <B> The type of builder returned for method chaining.
     * @since 0.1
     */
    protected static class BuilderImpl<
            T extends ThreadI18nContextProvider,
            B extends BuilderImpl<T, B>>
    extends AbstractI18nContextProvider.BuilderImpl<T, B>
    implements Builder {

        /**
         * If the {@code I18nContext} instances should be inherited by child
         * {@code Thread}s.
         */
        protected boolean inheritableContexts = true;

        /**
         * Creates a new instance.
         */
        protected BuilderImpl() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        public B configure(
                final @NotNull Properties config) {
            super.configure(config);
            configureInheritable(config);
            return (B) this;
        }

        /**
         * Configures the default language based on specified configuration.
         * 
         * @param config The I18N configuration.
         */
        protected void configureInheritable(
                final @NotNull Properties config) {
            if (config.containsKey(I18nConfiguration.CONTEXT_INHERITED)) {
                setInheritableContexts(Boolean.valueOf(
                        config.getProperty(I18nConfiguration.CONTEXT_INHERITED)));
            }
        }

        /**
         * Sets if the {@code I18nContext} instances will be inherited by child
         * threads.
         * 
         * @param inheritable If the {@code I18nContext} instances will be
         * inherited by child threads.
         * @return This builder, for method chaining.
         */
        @SuppressWarnings("unchecked")
        public @NotNull B setInheritableContexts(
                @NotNull boolean inheritable) {
            this.inheritableContexts = inheritable;
            return (B) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull ThreadI18nContextProvider build() {
            return new ThreadI18nContextProvider(this);
        }
    }

    /**
     * Factory for {@code ThreadI18nContextProvider} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-08
     * @since 0.1
     */
    public static class Factory
    implements I18nContextProviderFactory {

        /**
         * Creates a new instance.
         */
        public Factory() {
            super();
        }

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
        public @NotNull ThreadI18nContextProvider create(
                final @NotNull Properties config) {
            return ThreadI18nContextProvider.builder()
                    .configure(config)
                    .build();
        }
    }
}
