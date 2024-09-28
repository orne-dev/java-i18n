package dev.orne.i18n.spring;

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
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import dev.orne.i18n.context.ThreadI18nContextProvider;
import dev.orne.i18n.I18nResources;
import dev.orne.i18n.context.I18nConfiguration;
import dev.orne.i18n.context.I18nContext;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Implementation of {@code I18nContextProvider} for Spring contexts.
 * <p>
 * Uses {code I18nSpringContext} as context implementation and supports
 * {@code MessageSource} auto-wiring for default I18N resources lookup.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @see I18nSpringContext
 * @see I18nSpringResources
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class I18nSpringContextProvider
extends ThreadI18nContextProvider {

    /**
     * Creates a new instance based on specified builder.
     * 
     * @param builder The I18N context provider builder.
     */
    protected I18nSpringContextProvider(
            final @NotNull BuilderImpl<?, ?> builder) {
        super(builder);
    }

    /**
     * Creates a new {@code I18nSpringContextProvider} instances builder.
     * 
     * @return The {@code I18nSpringContextProvider} instances builder.
     */
    public static @NotNull Builder builder() {
        return new BuilderImpl<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nContext createContext() {
        return new I18nSpringContext(getSessionUUID());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nContext createContext(
            final @NotNull I18nContext parent) {
        Validate.notNull(parent);
        return new I18nSpringContext(getSessionUUID());
    }

    @Override
    public void clearContext() {
        super.clearContext();
        LocaleContextHolder.resetLocaleContext();
    }

    /**
     * Interface for {@code I18nSpringContextProvider} instance builders.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @since 0.1
     */
    public interface Builder
    extends ThreadI18nContextProvider.Builder {

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
         * Sets the default I18N resources.
         * 
         * @param messageSource The default Spring message source
         * @return This builder, for method chaining.
         */
        @NotNull Builder setDefaultI18nResources(
                @NotNull MessageSource messageSource);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder addI18nResources(
                @NotNull String key,
                @NotNull I18nResources resource);

        /**
         * Adds alternative I18N resources to be used when the specified key is
         * used.
         * 
         * @param key The key of the alternative I18N resources
         * @param messageSource The default Spring message source
         * @return This builder, for method chaining.
         */
        @NotNull Builder addI18nResources(
                @NotNull String key,
                @NotNull MessageSource messageSource);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull Builder setInheritableContexts(
                @NotNull boolean inheritable);

        /**
         * {@inheritDoc}
         */
        @Override
        @NotNull I18nSpringContextProvider build();
    }

    /**
     * Builder of {@code I18nSpringContextProvider} instances.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2024-09
     * @param <T> The type of I18N context provider build by the builder.
     * @param <B> The type of builder returned for method chaining.
     * @since 0.1
     */
    protected static class BuilderImpl<
            T extends I18nSpringContextProvider,
            B extends BuilderImpl<T, B>>
    extends ThreadI18nContextProvider.BuilderImpl<T, B>
    implements Builder {

        /**
         * Creates a new instance.
         */
        protected BuilderImpl() {
            super();
        }

        /**
         * Sets the default I18N resources.
         * 
         * @param messageSource The default Spring message source
         * @return This builder, for method chaining.
         */
        @SuppressWarnings("unchecked")
        public @NotNull B setDefaultI18nResources(
                final @NotNull MessageSource messageSource) {
            setDefaultI18nResources(new I18nSpringResources(messageSource));
            return (B) this;
        }

        /**
         * Adds alternative I18N resources to be used when the specified key is
         * used.
         * 
         * @param key The key of the alternative I18N resources
         * @param messageSource The default Spring message source
         * @return This builder, for method chaining.
         */
        @SuppressWarnings("unchecked")
        public @NotNull B addI18nResources(
                @NotNull String key,
                @NotNull MessageSource messageSource) {
            addI18nResources(key, new I18nSpringResources(messageSource));
            return (B) this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull I18nSpringContextProvider build() {
            return new I18nSpringContextProvider(this);
        }
    }
}
