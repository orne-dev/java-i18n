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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.i18n.spring.EnableI18N;
import dev.orne.i18n.spring.I18nSpringContextProvider;

/**
 * Unit tests for {@code I18nSpringContextProvider}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringContextProvider
 */
@Tag("spring")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringAnnotationConfigurationIT.SpringConfig.class)
public class SpringAnnotationConfigurationIT
extends AbstractSpringConfigurationIT {

    @Configuration
    @EnableI18N
    static class SpringConfig {
        @Bean
        public MessageSource messageSource() {
            final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
            source.addBasenames(TestMessages.BUNDLE_PATH);
            source.setDefaultEncoding(StandardCharsets.UTF_8.name());
            return source;
        }
    }
}
