package dev.orne.i18n;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Default implementation of {@code I18nContextProvider}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nContextProvider
 * @since 0.1
 */
public class DefaultI18nContextProvider
implements I18nContextProvider {

    /** The UUID of this provider instance. */
    private UUID sessionUUID = UUID.randomUUID();
    /**
     * If the created {@code I18nContext} instances should be in full mode by
     * default.
     * 
     * @see {@link I18nContext#isFullMode()}
     */
    private boolean fullModeByDefault;
    /** The supported languages. */
    private @NotNull Locale[] availableLocales =
            Locale.getAvailableLocales();
    /** The default I18N resources. */
    private @NotNull I18nResources defaultI18nResources =
            DummyI18nResources.INSTANCE;
    /** The alternative I18N resources by key. */
    private final @NotNull Map<String, I18nResources> i18nResources =
            new HashMap<>();
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
     * Returns the UUID of this provider instance and session.
     * Used to check contexts validity. Constant from instance creation to
     * call to {@code invalidate()}
     * 
     * @return The UUID of this provider instance
     */
    public UUID getSessionUUID() {
        return this.sessionUUID;
    }

    /**
     * Returns if the created {@code I18nContext} instances should be in full
     * mode by default.
     * 
     * @return {@code true} if all available translations should be retrieved
     * by default
     */
    public boolean isFullModeByDefault() {
        return fullModeByDefault;
    }

    /**
     * Sets if the created {@code I18nContext} instances should be in full mode
     * by default.
     * <p>
     * If {@code true} all available translations should be retrieved.
     * Otherwise only the translation in user locale is required.
     * 
     * @param value If all available translations should be retrieved by
     * default
     */
    public void setFullModeByDefault(
            final boolean value) {
        this.fullModeByDefault = value;
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull I18nResources getDefaultI18nResources() {
        return this.defaultI18nResources;
    }

    /**
     * Returns the default I18N resources.
     * 
     * @return The default I18N resources
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
    public Map<String, I18nResources> getI18nResources() {
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
            return defaultI18nResources;
        } else {
            return this.i18nResources.getOrDefault(key, defaultI18nResources);
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
     * Returns {@code true} if the {@code I18nContext} instances will be
     * inherited by child {@code Thread}s.
     * 
     * @return If the {@code I18nContext} instances will be inherited by child
     * {@code Thread}s
     */
    public boolean isInheritable() {
        return this.contexts instanceof InheritableThreadLocal;
    }

    /**
     * Returns the internal {@code I18nContext}s per {@code Thread} container.
     * 
     * @return The {@code I18nContext}s per {@code Thread} container
     */
    protected ThreadLocal<I18nContext> getContexts() {
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
        if (context == null || !isContextAlive(context)) {
            context = createContext();
            this.contexts.set(context);
        }
        return context;
    }

    /**
     * Creates a new I18N context with default values.
     * 
     * @return The new I18N context
     */
    public @NotNull I18nContext createContext() {
        final DefaultI18nContext context = new DefaultI18nContext(this.sessionUUID);
        context.setFullMode(this.fullModeByDefault);
        return context;
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
        context.setFullMode(parent.isFullMode());
        return context;
    }

    /**
     * The default contexts don't expire.
     */
    @Override
    public boolean isContextAlive(final I18nContext context) {
        return Validate.notNull(context) == this.contexts.get() &&
                this.sessionUUID.equals(context.getProviderUUID());
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
     * <p>
     * This implementation resets available languages, and I18N resources to
     * defaults and generates a new session UUID to invalidate any existing
     * contexts.
     */
    @Override
    public synchronized void invalidate() {
        this.sessionUUID = UUID.randomUUID();
        this.fullModeByDefault = false;
        this.availableLocales = Locale.getAvailableLocales();
        this.defaultI18nResources = DummyI18nResources.INSTANCE;
        this.i18nResources.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.fullModeByDefault)
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
        final DefaultI18nContextProvider other = (DefaultI18nContextProvider) obj;
        return new EqualsBuilder()
                .append(this.fullModeByDefault, other.fullModeByDefault)
                .append(this.availableLocales, other.availableLocales)
                .append(this.defaultI18nResources, other.defaultI18nResources)
                .append(this.i18nResources, other.i18nResources)
                .isEquals();
    }
}
