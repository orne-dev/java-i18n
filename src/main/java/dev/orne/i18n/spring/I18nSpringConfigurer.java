package dev.orne.i18n.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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

import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nContextProvider;
import dev.orne.i18n.I18nContextProviderConfigurableStrategy;
import dev.orne.i18n.I18nContextProviderStrategy;
import dev.orne.i18n.I18nResources;
import jakarta.validation.constraints.NotNull;

/**
 * I18N context provider configuration for Spring. Configures a
 * {@code I18nContextProvider} (an instance of
 * {@code I18nSpringContextProvider} by default) for a {@code ClassLoader}
 * (the {@code ClassLoader} of this library by default).
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @see I18nSpringContextProvider
 * @since 0.1
 */
@Configuration
public class I18nSpringConfigurer
implements InitializingBean, ImportAware {

    /**
     * The {@code Class} whose {@code ClassLoader} this configuration will be
     * applied to.
     */
    private Class<?> targetClass;
    /**
     * If the {@code I18nContext} instances should be inherited by child
     * {@code Thread}s.
     */
    private boolean inheritableContexts = true;
    /** The supported languages. */
    private Locale[] availableLocales;
    /** The {@code MessageSource} to use for default I18N resources. */
    private MessageSource defaultMessageSource;
    /** The default I18N resources. */
    private I18nResources defaultI18nResources;
    /** The additional named I18N resources. */
    private Map<String, I18nResources> namedI18nResources;
    /** The custom I18N context provider to use. */
    private I18nContextProvider contextProvider;

    /**
     * Returns the {@code Class} whose {@code ClassLoader} this configuration
     * will be applied to.
     * <p>
     * If {@code null} the configuration will be applied as default provider.
     * 
     * @return The target class
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * Sets the {@code Class} whose {@code ClassLoader} this configuration
     * will be applied to.
     * <p>
     * If {@code null} the configuration will be applied as default provider.
     * 
     * @param targetClass The target class
     */
    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Return the {@code ClassLoader} this configuration will be applied to.
     * <p>
     * Requires a non {@code null} {@code targetClass}.
     * 
     * @return The {@code ClassLoader} this configuration will be applied to
     * @throws NullPointerException If {@code targetClass} is {@code null}
     */
    protected @NotNull ClassLoader getTargetClassLoader() {
        ClassLoader result = Validate.notNull(this.targetClass).getClassLoader();
        if (result == null) {
            if (Thread.class.equals(this.targetClass)) {
                result = Thread.currentThread().getContextClassLoader();
            } else if (System.class.equals(this.targetClass)) {
                result = ClassLoader.getSystemClassLoader();
            } else {
                result = Thread.currentThread().getContextClassLoader();
                while (result.getParent() != null) {
                    result = result.getParent();
                }
            }
        }
        return result;
    }

    /**
     * Returns {@code true} if the {@code I18nContext} instances will be
     * inherited by child {@code Thread}s.
     * 
     * @return If the {@code I18nContext} instances will be inherited by child
     * {@code Thread}s
     */
    public boolean isInheritableContexts() {
        return this.inheritableContexts;
    }

    /**
     * Sets if the {@code I18nContext} instances will be inherited by child
     * {@code Thread}s.
     * 
     * @param inheritable If the {@code I18nContext} instances will be
     * inherited by child {@code Thread}s
     */
    public void setInheritableContexts(final boolean inheritable) {
        this.inheritableContexts = inheritable;
    }

    /**
     * Returns the supported languages.
     * 
     * @return The supported languages
     */
    public Locale[] getAvailableLocales() {
        if (this.availableLocales == null) {
            return null;
        } else {
            return Arrays.copyOf(this.availableLocales, this.availableLocales.length);
        }
    }

    /**
     * Sets the supported languages.
     * 
     * @param locales The supported languages
     */
    public void setAvailableLocales(
            final Locale[] locales) {
        if (locales == null) {
            this.availableLocales = null;
        } else {
            this.availableLocales = Arrays.copyOf(locales, locales.length);
        }
    }

    /**
     * Returns the {@code MessageSource} to use for default I18N resources.
     * 
     * @return The {@code MessageSource} to use for default I18N resources
     */
    public MessageSource getDefaultMessageSource() {
        return this.defaultMessageSource;
    }

    /**
     * Sets the {@code MessageSource} to use for default I18N resources.
     * 
     * @param source The {@code MessageSource} to use
     */
    @Autowired(required=false)
    public void setDefaultMessageSource(
            final MessageSource source) {
        this.defaultMessageSource = source;
    }

    /**
     * Returns the default I18N resources.
     * 
     * @return The default I18N resources
     */
    public I18nResources getDefaultI18nResources() {
        return this.defaultI18nResources;
    }

    /**
     * Sets the default I18N resources.
     * 
     * @param resources The default I18N resources
     */
    public void setDefaultI18nResources(
            final I18nResources resources) {
        this.defaultI18nResources = resources;
    }

    /**
     * Returns the additional named I18N resources.
     * 
     * @return The additional named I18N resources
     */
    public Map<@NotNull String, @NotNull I18nResources> getNamedI18nResources() {
        if (this.namedI18nResources == null) {
            return null;
        } else {
            return Collections.unmodifiableMap(this.namedI18nResources);
        }
    }

    /**
     * Sets the additional named I18N resources.
     * 
     * @param resources The additional named I18N resources
     */
    public void setNamedI18nResources(
            final Map<@NotNull String, @NotNull I18nResources> resources) {
        if (resources == null) {
            this.namedI18nResources = null;
        } else {
            Validate.noNullElements(resources.keySet());
            Validate.noNullElements(resources.values());
            this.namedI18nResources = new HashMap<>(resources);
        }
    }

    /**
     * Returns the custom I18N context provider to use.
     * 
     * @return The custom I18N context provider to use
     */
    public I18nContextProvider getContextProvider() {
        return this.contextProvider;
    }

    /**
     * Sets the custom I18N context provider to use.
     * 
     * @param provider The custom I18N context provider to use
     */
    public void setContextProvider(
            final I18nContextProvider provider) {
        this.contextProvider = provider;
    }

    /**
     * Creates a new {@code I18nContextProvider} based on configured
     * properties.
     * <p>
     * This implementation creates an instance of
     * {@code I18nSpringContextProvider}.
     * 
     * @return The new configured {@code I18nContextProvider}
     */
    protected @NotNull I18nContextProvider createContextProvider() {
        final @NotNull I18nSpringContextProvider bean;
        if (this.defaultI18nResources != null) {
            bean = new I18nSpringContextProvider(
                    this.inheritableContexts);
            bean.setDefaultI18nResources(this.defaultI18nResources);
        } else if (this.defaultMessageSource == null) {
            bean = new I18nSpringContextProvider(
                    this.inheritableContexts);
        } else {
            bean = new I18nSpringContextProvider(
                    this.defaultMessageSource,
                    this.inheritableContexts);
        }
        if (this.availableLocales != null) {
            bean.setAvailableLocales(this.availableLocales);
        }
        if (this.namedI18nResources != null) {
            for (final Map.Entry<String, I18nResources> entry : this.namedI18nResources.entrySet()) {
                bean.addI18nResources(entry.getKey(), entry.getValue());
            }
        }
        return bean;
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
            final Class<?> targetCls = (Class<?>) annotAttrs.get("targetClass");
            if (targetCls != null && !Void.class.equals(targetCls)) {
                this.targetClass = targetCls;
            }
            final String[] availableLanguages = (String[]) annotAttrs.get("availableLanguages");
            if (availableLanguages != null && availableLanguages.length != 0) {
                Validate.noNullElements(availableLanguages);
                this.availableLocales = Stream.of(availableLanguages)
                        .map(Locale::new)
                        .toArray(Locale[]::new);
            }
        }
    }

    /**
     * Sets the configured {@code I18nContextProvider} for the configured
     * {@code ClassLoader}.
     */
    @Override
    public void afterPropertiesSet() {
        final @NotNull I18nContextProvider provider;
        if (this.contextProvider == null) {
            provider = createContextProvider();
        } else {
            provider = this.contextProvider;
        }
        final I18nContextProviderStrategy strategy = I18N.getContextProviderStrategy();
        if (this.targetClass != null &&
                strategy instanceof I18nContextProviderConfigurableStrategy) {
            ((I18nContextProviderConfigurableStrategy) I18N.getContextProviderStrategy()).setContextProvider(
                    getTargetClassLoader(),
                    provider);
        } else {
            strategy.setDefaultContextProvider(provider);
        }
    }
}
