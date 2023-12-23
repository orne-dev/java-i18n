package dev.orne.i18n.it.common;

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
import java.util.ResourceBundle;

import dev.orne.i18n.context.DefaultI18nContextProvider;
import dev.orne.i18n.context.DefaultI18nContextProviderStrategy;
import dev.orne.i18n.context.I18nBundleResources;
import dev.orne.i18n.context.I18nContextProviderStrategy;

public final class TestMessages {

    public static final String BUNDLE_PATH =
            "dev/orne/i18n/it/common/test-messages";
    public static final Object BUNDLE_ID_DEFAULT_VALUE =
            "default-test-messages";
    public static final Object BUNDLE_ID_YY_VALUE =
            "yy-test-messages";
    public static final Object BUNDLE_ID_ZZ_VALUE =
            "zz-test-messages";
    public static final String BUNDLE_ALT_PATH =
            "dev/orne/i18n/it/common/test-messages-alt";
    public static final Object BUNDLE_ID_ALT_DEFAULT_VALUE =
            "default-alt-test-messages";
    public static final Object BUNDLE_ID_ALT_YY_VALUE =
            "yy-alt-test-messages";
    public static final Object BUNDLE_ID_ALT_ZZ_VALUE =
            "zz-alt-test-messages";

    public static final String DEFAULT_LANG = "xx";
    public static final Locale DEFAULT_LOCALE = new Locale(DEFAULT_LANG);
    public static final String YY_LANG = "yy";
    public static final Locale YY_LOCALE = new Locale(YY_LANG);
    public static final String ZZ_LANG = "zz";
    public static final Locale ZZ_LOCALE = new Locale(ZZ_LANG);
    public static final ResourceBundle DEFAULT_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_PATH, DEFAULT_LOCALE);
    public static final I18nBundleResources DEFAULT_RESOURCES =
            new I18nBundleResources(DEFAULT_BUNDLE);

    private TestMessages() {
        // Utility class
    }

    public static ResourceBundle getBundle(String lang) {
        return getBundle(new Locale(lang));
    }

    public static ResourceBundle getBundle(Locale lang) {
        return ResourceBundle.getBundle(BUNDLE_PATH, lang);
    }

    public static void configureI18N() {
        configureI18N(DEFAULT_BUNDLE);
    }

    public static void configureI18N(ResourceBundle bundle) {
        final I18nBundleResources resources = new I18nBundleResources(bundle);
        final DefaultI18nContextProvider provider = new DefaultI18nContextProvider();
        provider.setDefaultI18nResources(resources);
        final DefaultI18nContextProviderStrategy strategy = new DefaultI18nContextProviderStrategy(provider);
        I18nContextProviderStrategy.setInstance(strategy);
    }

    public final static class Entries {

        public static final String BUNDLE_ID =
                "dev.orne.i18n.test.bundle";
        public static final String BLANK =
                "dev.orne.i18n.test.blank";
        public static final String VALID =
                "dev.orne.i18n.test.valid";
        public static final String INVALID =
                "dev.orne.i18n.test.invalid";
        public static final String SHORT =
                "dev.orne.i18n.test.short";
        public static final String LONG =
                "dev.orne.i18n.test.long";
        public static final String EMPTY =
                "dev.orne.i18n.test.empty";

        private Entries() {
            // Utility class
        }
    }
}
