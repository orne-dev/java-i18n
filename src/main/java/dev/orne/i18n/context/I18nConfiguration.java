package dev.orne.i18n.context;

/**
 * Utility class with I18N configuration constants.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-11
 * @since 0.1
 */
public final class I18nConfiguration {

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
}
