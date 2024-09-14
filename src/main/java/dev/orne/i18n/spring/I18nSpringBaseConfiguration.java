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

import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.CollectionUtils;

import dev.orne.i18n.context.DummyI18nResources;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * I18N context provider configuration for Spring. Configures a
 * {@code I18nContextProvider} (an instance of
 * {@code I18nSpringContextProvider} by default).
 * <p>
 * By default configures the provider for all {@code ClassLoader}s.
 * Supports selecting a target class that will configure
 * the provider for the {@code ClassLoader} of the given class and all its
 * children {@code ClassLoader}, allowing different configurations for each
 * application even when the library is deployed as a shared library or
 * different configurations for each sub-module when the library is deployed
 * at EAR level in JavaEE environments.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-05
 * @see I18nContextProvider
 * @see I18nSpringContextProvider
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Configuration
public class I18nSpringBaseConfiguration
implements InitializingBean,
        ImportAware,
        MessageSourceAware,
        I18nContextProvider.Configurer {

    /** The application default {@code MessageSource}. */
    private MessageSource messageSource;
    /** The I18N context provider configuration customizer. */
    private I18nSpringConfigurer configurer = new I18nSpringConfigurer() {};
    /** The {@code ClassLoader} this configuration will be applied to. */
    private ClassLoader target;

    /**
     * Creates a new instance.
     */
    public I18nSpringBaseConfiguration() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessageSource(
            final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    /**
     * Collect any I18N context provider configuration customizer beans through
     * autowiring.
     * Only one bean allowed.
     */
    @Autowired(required = false)
    void setConfigurers(
            final @NotNull Collection<I18nSpringConfigurer> configurers) {
        if (CollectionUtils.isEmpty(configurers)) {
            return;
        }
        if (configurers.size() > 1) {
            throw new IllegalStateException("Only one I18nSpringConfigurer may exist");
        }
        this.configurer = configurers.iterator().next();
    }

    /**
     * Returns the I18N context provider configuration customizer.
     * 
     * @return The I18N context provider configuration customizer.
     */
    protected @NotNull I18nSpringConfigurer getConfigurer() {
        return this.configurer;
    }

    /**
     * Returns the {@code ClassLoader} this configuration will be applied to.
     * <p>
     * If {@code null} the configuration will be applied to current thread's
     * context class loader.
     * 
     * @return The target class loader.
     */
    protected ClassLoader getTarget() {
        return target;
    }

    /**
     * Sets the {@code ClassLoader} this configuration will be applied to.
     * <p>
     * <p>
     * If {@code null} the configuration will be applied to current thread's
     * context class loader.
     * 
     * @param target The target class loader.
     */
    public void setTarget(
            final ClassLoader target) {
        this.target = target;
    }

    /**
     * Inherits configuration from {@code EnableI18N} annotation.
     * 
     * @param importMetadata The import metadata
     * @see EnableI18N
     */
    @Override
    public void setImportMetadata(
            final @NotNull AnnotationMetadata importMetadata) {
        Validate.notNull(importMetadata);
        final Map<String, Object> annotAttrs = importMetadata.getAnnotationAttributes(
                EnableI18N.class.getName());
        if (annotAttrs != null) {
            final Class<?> targetCls = (Class<?>) annotAttrs.get("classLoader");
            if (targetCls != null && !Void.class.equals(targetCls)) {
                this.target = targetCls.getClassLoader();
            }
        }
    }

    /**
     * Creates a new {@code I18nSpringContextProvider} based on configured
     * properties.
     * <p>
     * This implementation creates an instance of
     * {@code I18nSpringContextProvider}.
     * 
     * @return The new configured {@code I18nSpringContextProvider}
     */
    protected @NotNull I18nSpringContextProvider createContextProvider() {
        final I18nSpringContextProvider.Builder builder = this.configurer.getI18nContextProviderBuilder();
        this.configurer.configureI18nContextProvider(builder);
        if (builder.build().getDefaultI18nResources() instanceof DummyI18nResources
                && this.messageSource != null) {
            builder.setDefaultI18nResources(new I18nSpringResources(this.messageSource));
        }
        return builder.build();
    }

    /**
     * Sets the configured {@code I18nContextProvider} for the configured
     * {@code ClassLoader}.
     */
    @Override
    public void afterPropertiesSet() {
        final I18nContextProvider provider = createContextProvider();
        if (this.target != null) {
            setI18nContextProvider(this.target, provider);
        } else {
            setI18nContextProvider(provider);
        }
    }
}
