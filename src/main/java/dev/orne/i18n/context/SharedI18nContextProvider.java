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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Implementation of {@code I18nContextProvider} that shares a single
 * {@code I18nContext} instance for all threads.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
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
     * Creates a new instance based on specified builder.
     */
    protected SharedI18nContextProvider(
            final @NotNull BuilderImpl<?, ?> builder) {
        super(builder);
    }

    /**
     * Creates a new {@code SharedI18nContextProvider} instances builder.
     * 
     * @return The {@code SharedI18nContextProvider} instances builder.
     */
    public static @NotNull Builder builder() {
        return new BuilderImpl<>();
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
     * Interface for {@code SharedI18nContextProvider} instance builders.
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
         * {@inheritDoc}
         */
        @Override
        @NotNull SharedI18nContextProvider build();
    }

    /**
     * Builder of {@code SharedI18nContextProvider} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @param <T> The type of I18N context provider build by the builder.
     * @param <B> The type of builder returned for method chaining.
     * @since 0.1
     */
    protected static class BuilderImpl<
            T extends SharedI18nContextProvider,
            B extends BuilderImpl<T, B>>
    extends AbstractI18nContextProvider.BuilderImpl<T, B>
    implements Builder {

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
        public @NotNull SharedI18nContextProvider build() {
            return new SharedI18nContextProvider(this);
        }
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
        public @NotNull SharedI18nContextProvider create(@NotNull Properties config) {
            return SharedI18nContextProvider.builder()
                    .configure(config)
                    .build();
        }
   }
}
