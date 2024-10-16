package dev.orne.i18n.context;

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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nResources;

/**
 * Implementation of {@code I18nResources} based on {@code ResourceBundle}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nResources
 * @see ResourceBundle
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
public class I18nBundleResources
implements I18nResources {

    /** The logger of the class. */
    private static final Logger LOG = LoggerFactory.getLogger(I18nBundleResources.class);

    /** The I18N resources bundle base name. */
    private final @NotNull String baseName;

    /**
     * Creates a new instance for the specified bundle.
     * 
     * @param baseName The I18N resources bundle base name
     */
    public I18nBundleResources(
            final @NotNull String baseName) {
        super();
        this.baseName = Validate.notNull(baseName);
    }

    /**
     * Returns a non null I18N resources for the specified bundle base name.
     * If the bundle cannot be found a dommy I18N resources is returned.
     * 
     * @param baseName The I18N resources bundle base name
     * @return The I18N resources instance.
     */
    public static @NotNull I18nResources forBasename(
            final @NotNull String baseName) {
        try {
            ResourceBundle.getBundle(baseName, Locale.getDefault());
            return new I18nBundleResources(baseName);
        } catch (final MissingResourceException e) {
            LOG.warn("No bundle for I18N resources found. Using dummy resources.");
            return DummyI18nResources.getInstance();
        }
    }

    /**
     * Returns the I18N resources bundle for the current locale.
     * 
     * @return The I18N resources bundle
     */
    protected @NotNull ResourceBundle getBundle() {
        return getBundle(I18N.getLocale());
    }

    /**
     * Returns the I18N resources bundle for the specified locale.
     * 
     * @param locale The desired locale
     * @return The I18N resources bundle
     */
    protected @NotNull ResourceBundle getBundle(
            final @NotNull Locale locale) {
        return ResourceBundle.getBundle(this.baseName, locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String[] codes,
            final Locale locale,
            final Object... params) {
        Validate.notNull(defaultMessage);
        Validate.notNull(codes);
        Validate.noNullElements(codes);
        final ResourceBundle bundle = getBundle(locale);
        String format = null;
        for (final String code : codes) {
            try {
                format = bundle.getString(code);
                break;
            } catch (final MissingResourceException ignore) {
                // Ignored
            }
        }
        if (format == null) {
            format = defaultMessage;
        }
        try {
            return MessageFormat.format(format, params);
        } catch (final IllegalArgumentException ignore) {
            return format;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage(
            final @NotNull String defaultMessage,
            final @NotNull String code,
            final Locale locale,
            final Object... params) {
        Validate.notNull(defaultMessage);
        Validate.notNull(code);
        final ResourceBundle bundle = getBundle(locale);
        String format;
        try {
            format = bundle.getString(code);
        } catch (final MissingResourceException mre) {
            format = defaultMessage;
        }
        try {
            return MessageFormat.format(format, params);
        } catch (final IllegalArgumentException ignore) {
            return format;
        }
    }
}
