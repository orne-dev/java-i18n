package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2022 Orne Developments
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import dev.orne.i18n.I18nResources;

/**
 * Abstract implementation of {@code I18nContextProvider}.
 * 
 * @author <a href="mailto:ihernaez@users.noreply.github.com">(w) Iker Hernaez</a>
 * @version 1.0, 2022-12
 * @see I18nContextProvider
 * @since 0.2
 */
@API(status=Status.STABLE, since="0.1")
public abstract class AbstractI18nContextProvider
implements I18nContextProvider {

    /** The UUID of this provider instance. */
    private @NotNull UUID sessionUUID = UUID.randomUUID();
    /** The supported languages. */
    private @NotNull Locale[] availableLocales =
            Locale.getAvailableLocales();
    /** The default I18N resources. */
    private @NotNull I18nResources defaultI18nResources =
            DummyI18nResources.getInstance();
    /** The alternative I18N resources by key. */
    private final @NotNull Map<@NotNull String, @NotNull I18nResources> i18nResources =
            new HashMap<>();

    /**
     * Creates a new instance.
     */
    protected AbstractI18nContextProvider() {
        super();
    }

    /**
     * Creates a new instance based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    protected AbstractI18nContextProvider(
            final @NotNull Properties config) {
        super();
        configureAvailableLocales(config);
        configureDefaultI18nResources(config);
        configureAlternativeI18nResources(config);
    }

    /**
     * Returns the UUID of this provider instance and session.
     * Used to check contexts validity. Constant from instance creation to
     * call to {@code invalidate()}
     * 
     * @return The UUID of this provider instance
     */
    public @NotNull UUID getSessionUUID() {
        return this.sessionUUID;
    }

    /**
     * Configures the supported languages based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    protected void configureAvailableLocales(
            final @NotNull Properties config) {
        if (config.containsKey(I18nConfiguration.AVAILABLE_LANGUAGES)) {
            final String[] langs = StringUtils.split(
                    config.getProperty(I18nConfiguration.AVAILABLE_LANGUAGES),
                    ",");
            final Locale[] locales = new Locale[langs.length];
            for (int i = 0; i < langs.length; i++) {
                locales[i] = new Locale(langs[i]);
            }
            setAvailableLocales(locales);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Locale[] getAvailableLocales() {
        return Arrays.copyOf(availableLocales, availableLocales.length);
    }

    /**
     * Sets the supported languages.
     * 
     * @param locales The supported languages
     */
    public void setAvailableLocales(
            final @NotNull Locale[] locales) {
        Validate.notNull(locales);
        Validate.noNullElements(locales);
        availableLocales = Arrays.copyOf(locales, locales.length);
    }

    /**
     * Configures the default I18N resources based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    protected void configureDefaultI18nResources(
            final @NotNull Properties config) {
        if (config.containsKey(I18nConfiguration.DEFAULT_RESOURCES)) {
            setDefaultI18nResources(I18nBundleResources.forBasename(
                    config.getProperty(I18nConfiguration.DEFAULT_RESOURCES)));
        }
    }

    /**
     * Configures the alternative I18N resources based on specified configuration.
     * 
     * @param config The I18N configuration.
     */
    protected void configureAlternativeI18nResources(
            final @NotNull Properties config) {
        for (final String prop : config.stringPropertyNames()) {
            if (prop.startsWith(I18nConfiguration.NAMED_RESOURCES_PREFIX)) {
                final String resourceName = prop.substring(I18nConfiguration.NAMED_RESOURCES_PREFIX.length());
                addI18nResources(
                        resourceName,
                        I18nBundleResources.forBasename(config.getProperty(prop)));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nResources getDefaultI18nResources() {
        return this.defaultI18nResources;
    }

    /**
     * Returns the default I18N resources.
     * 
     * @param resources The default I18N resources
     */
    public void setDefaultI18nResources(
            final @NotNull I18nResources resources) {
        this.defaultI18nResources = Validate.notNull(resources);
    }

    /**
     * Returns the alternative I18N resources by key.
     * 
     * @return The alternative I18N resources by key
     */
    public @NotNull Map<@NotNull String, @NotNull I18nResources> getI18nResources() {
        return Collections.unmodifiableMap(this.i18nResources);
    }

    /**
     * Returns the I18N resources identified by the specified key.
     * If key is {@code null} or no resources is associated for such key
     * returns the default I18N resources.
     * 
     * @param key The key of the alternative I18N resources
     * @return The I18N resources to use for the key
     */
    public @NotNull I18nResources getI18nResources(
            final String key) {
        if (key == null) {
            return this.defaultI18nResources;
        } else {
            return this.i18nResources.getOrDefault(key, this.defaultI18nResources);
        }
    }

    /**
     * Adds alternative I18N resources to be used when the specified key is
     * used.
     * 
     * @param key The key of the alternative I18N resources
     * @param resource The alternative I18N resources
     */
    public void addI18nResources(
            final @NotNull String key,
            final @NotNull I18nResources resource) {
        this.i18nResources.put(
                Validate.notNull(key),
                Validate.notNull(resource));
    }

    /**
     * Clears the registered alternative I18N resources.
     * Default I18N resources remain unmodified.
     */
    public void clearI18nResources() {
        this.i18nResources.clear();
    }

    /**
     * Creates a new I18N context with default values.
     * 
     * @return The new I18N context
     */
    public @NotNull I18nContext createContext() {
        return new DefaultI18nContext(this.sessionUUID);
    }

    /**
     * Creates a new I18N context with values inherited from the specified
     * parent I18N context.
     * 
     * @param parent The parent I18N context
     * @return The new I18N context
     */
    public @NotNull I18nContext createContext(
            final @NotNull I18nContext parent) {
        Validate.notNull(parent);
        final I18nContext context = createContext();
        context.setLocale(parent.getLocale());
        return context;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation resets available languages, and I18N resources to
     * defaults and generates a new session UUID to invalidate any existing
     * contexts.
     */
    @Override
    public synchronized void invalidate() {
        this.sessionUUID = UUID.randomUUID();
        this.availableLocales = Locale.getAvailableLocales();
        this.defaultI18nResources = DummyI18nResources.getInstance();
        this.i18nResources.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.availableLocales)
                .append(this.defaultI18nResources)
                .append(this.i18nResources)
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
        final AbstractI18nContextProvider other = (AbstractI18nContextProvider) obj;
        return new EqualsBuilder()
                .append(this.availableLocales, other.availableLocales)
                .append(this.defaultI18nResources, other.defaultI18nResources)
                .append(this.i18nResources, other.i18nResources)
                .isEquals();
    }
}
