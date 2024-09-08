package dev.orne.i18n.it.common;

import java.nio.charset.StandardCharsets;

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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.i18n.spring.EnableI18N;

/**
 * Integration tests for Spring Java annotation based configuration of Orne I18N.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @since 0.1
 */
@Tag("spring")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringAnnotationConfigurationAltIT.SpringConfig.class)
public class SpringAnnotationConfigurationAltIT
extends AbstractSpringConfigurationAltIT {

    @Configuration
    @EnableI18N(availableLanguages= {
            TestMessages.DEFAULT_LANG,
            TestMessages.YY_LANG,
            TestMessages.ZZ_LANG
    })
    static class SpringConfig {
        @Bean
        @Primary
        public MessageSource messageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames(TestMessages.BUNDLE_PATH);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
            return source;
        }
        @Bean(name = { ALT_RESOURCES_BEAN, ALT_I18N_RESOURCES_KEY})
        public MessageSource altMessageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames(TestMessages.BUNDLE_ALT_PATH);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
            return source;
        }
    }
}
