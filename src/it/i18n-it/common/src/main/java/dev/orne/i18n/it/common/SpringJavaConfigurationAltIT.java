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

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.i18n.spring.I18nSpringConfiguration;
import dev.orne.i18n.spring.I18nSpringConfigurer;
import dev.orne.i18n.spring.I18nSpringContextProvider;
import dev.orne.i18n.spring.I18nSpringResources;

/**
 * Integration tests for Spring Java configuration of Orne I18N.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @since 0.1
 */
@Tag("spring")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringJavaConfigurationAltIT.SpringConfig.class)
public class SpringJavaConfigurationAltIT
extends AbstractSpringConfigurationAltIT {

    @Configuration
    static class SpringConfig implements I18nSpringConfigurer {
        @Bean
        @Primary
        public MessageSource messageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames(TestMessages.BUNDLE_PATH);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
            return source;
        }
        @Bean
        public MessageSource altMessageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames(TestMessages.BUNDLE_ALT_PATH);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
            return source;
        }
        @Bean
        public I18nSpringConfiguration I18nSpringBaseConfiguration(
                final @Autowired MessageSource messageSource) {
            final I18nSpringConfiguration configurer = new I18nSpringConfiguration();
            return configurer;
        }
        @Override
        public void configureI18nContextProvider(
                final @NotNull I18nSpringContextProvider.Builder builder) {
            builder.configure()
                    .setAvailableLocales(new Locale[] {
                            TestMessages.DEFAULT_LOCALE,
                            TestMessages.YY_LOCALE,
                            TestMessages.ZZ_LOCALE
                    })
                    .addI18nResources(ALT_I18N_RESOURCES_KEY, new I18nSpringResources(altMessageSource()));
        }
    }
}
