package dev.orne.i18n.context;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotNull;

/**
 * Utility class with I18N configuration constants.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-11
 * @since 0.1
 */
public final class I18nConfiguration {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(I18nConfiguration.class);

    /** The configuration file path. */
    public static final String FILE = "dev.orne.i18n.config.properties";
    /**
     * The configuration file property for {@code I18nContextProviderStrategy}
     * implementation to use.
     */
    public static final String STRATEGY = "strategy";

    /**
     * Private constructor.
     */
    private I18nConfiguration() {
        // Utility class
    }

    /**
     * Loads the configuration properties from the configuration file
     * {@value FILE} from current class loader.
     * If no such file exists returns an empty {@code Properties}.
     * 
     * @return The configuration properties
     * @see #FILE
     */
    public static @NotNull Properties load() {
        return load(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Loads the configuration properties from the configuration file
     * {@value FILE} from specified class loader.
     * If no such file exists returns an empty {@code Properties}.
     * 
     * @param cl The class loader to load the configuration from.
     * @return The configuration properties.
     * @see #FILE
     */
    public static @NotNull Properties load(
            final @NotNull ClassLoader cl) {
        Validate.notNull(cl);
        final Properties config = new Properties();
        try (final InputStream input = cl.getResourceAsStream(I18nConfiguration.FILE)) {
            if (input != null) {
                config.load(input);
            }
        } catch (final IOException ioe) {
            LOG.error("Error loading I18N configuration file. Using default configuration.", ioe);
        }
        return config;
    }
}
