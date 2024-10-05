package dev.orne.i18n.spring;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2024 Orne Developments
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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.StaticMessageSource;

import dev.orne.i18n.I18N;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.I18nConfiguration;

/**
 * Integration tests for usage of EnableI18n in Spring context hierarchies.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2024-09
 * @since 0.1
 */
@Tag("ut")
class EnableI18nContextHierarchyTest {

    private static final Locale TEST_LOCALE = new Locale("xx");
    private static final String EAR_EN_TEXT = "Test message (English) [EAR]";
    private static final String EAR_FR_TEXT = "Test message (French) [EAR]";
    private static final String EAR_TEST_TEXT = "Test message (Test) [EAR]";
    private static final String WAR_EN_TEXT = "Test message (English) [WAR]";
    private static final String WAR_FR_TEXT = "Test message (French) [WAR]";
    private static final String WAR_TEST_TEXT = "Test message (Test) [WAR]";

    private List<Path> tmpFolder;

    @AfterEach
    void deleteTmpDirs() throws IOException {
        if (this.tmpFolder != null) {
            for (final Path folder : tmpFolder) {
                Files.walk(folder)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        }
    }

    @AfterEach
    void resetI18N() {
        ContextTestUtils.reset();
    }

    private ClassLoader createClassLoader()
    throws IOException {
        return createClassLoader(Thread.currentThread().getContextClassLoader());
    }

    private ClassLoader createClassLoader(
            final @NotNull ClassLoader parent)
    throws IOException {
        if (this.tmpFolder == null) {
            this.tmpFolder = new ArrayList<>();
        }
        final Path folder = Files.createTempDirectory("testTmp");
        this.tmpFolder.add(folder);
        return new URLClassLoader(
                new URL[] { folder.toUri().toURL() },
                parent);
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Target_Spring()
    throws InterruptedException, IOException {
        final ClassLoader earCL = createClassLoader();
        final ClassLoader warCL = createClassLoader(earCL);
        final ClassLoader war2CL = createClassLoader(earCL);
        final SpringContextRunnable ctxRunnable = new SpringContextRunnable();
        Thread cfgThread = new Thread(ctxRunnable);
        cfgThread.setContextClassLoader(warCL);
        try {
            cfgThread.start();
            cfgThread.join();
            I18nContextTestRunnable warTest = new I18nContextTestRunnable();
            Thread warThread = new Thread(warTest);
            warThread.setContextClassLoader(warCL);
            warThread.start();
            warThread.join();
            assertEquals(Locale.FRENCH, warTest.initialLocale);
            assertEquals(WAR_FR_TEXT, warTest.initialText);
            assertEquals(TEST_LOCALE, warTest.finalLocale);
            assertEquals(WAR_TEST_TEXT, warTest.finalText);
            I18nContextTestRunnable earTest = new I18nContextTestRunnable();
            Thread earThread = new Thread(earTest);
            earThread.setContextClassLoader(earCL);
            earThread.start();
            earThread.join();
            assertEquals(Locale.ENGLISH, earTest.initialLocale);
            assertEquals(EAR_EN_TEXT, earTest.initialText);
            assertEquals(TEST_LOCALE, earTest.finalLocale);
            assertEquals(EAR_TEST_TEXT, earTest.finalText);
            I18nContextTestRunnable war2Test = new I18nContextTestRunnable();
            war2Test.nextLocale = Locale.FRENCH;
            Thread war2Thread = new Thread(war2Test);
            war2Thread.setContextClassLoader(war2CL);
            war2Thread.start();
            war2Thread.join();
            assertEquals(Locale.ENGLISH, war2Test.initialLocale);
            assertEquals(EAR_EN_TEXT, war2Test.initialText);
            assertEquals(Locale.FRENCH, war2Test.finalLocale);
            assertEquals(EAR_FR_TEXT, war2Test.finalText);
        } finally {
            if (ctxRunnable.child != null) {
                ctxRunnable.child.close();
            }
            if (ctxRunnable.parent != null) {
                ctxRunnable.parent.close();
            }
        }
    }

    @Configuration
    @EnableI18N(classLoader = EarSpringConfig.class)
    static class EarSpringConfig implements I18nSpringConfigurer {
        
        @Bean
        public Properties appConfiguration() {
            final Properties prop = new Properties();
            prop.setProperty(I18nConfiguration.DEFAULT_LANGUAGE, Locale.ENGLISH.getLanguage());
            return prop;
        }
        
        @Bean
        public MessageSource messageSource() {
            final StaticMessageSource source = new StaticMessageSource();
            source.addMessage("test.message", Locale.ENGLISH, EAR_EN_TEXT);
            source.addMessage("test.message", Locale.FRENCH, EAR_FR_TEXT);
            source.addMessage("test.message", TEST_LOCALE, EAR_TEST_TEXT);
            return source;
        }
        
        @Override
        public void configureI18nContextProvider(
                final @NotNull I18nSpringContextProvider.Builder builder) {
            builder.configure(appConfiguration());
        }
    }

    @Configuration
    @EnableI18N
    static class WarSpringConfig implements I18nSpringConfigurer {
        
        @Bean
        public Properties warConfiguration() {
            final Properties prop = new Properties();
            prop.setProperty(I18nConfiguration.DEFAULT_LANGUAGE, Locale.FRENCH.getLanguage());
            return prop;
        }
        
        @Bean
        @Primary
        public MessageSource messageSource() {
            final StaticMessageSource source = new StaticMessageSource();
            source.addMessage("test.message", Locale.ENGLISH, WAR_EN_TEXT);
            source.addMessage("test.message", Locale.FRENCH, WAR_FR_TEXT);
            source.addMessage("test.message", TEST_LOCALE, WAR_TEST_TEXT);
            return source;
        }
        
        @Bean
        public MessageSource altMessageSource() {
            final StaticMessageSource source = new StaticMessageSource();
            source.addMessage("alt.message", Locale.ENGLISH, WAR_EN_TEXT);
            source.addMessage("alt.message", Locale.FRENCH, WAR_FR_TEXT);
            source.addMessage("alt.message", TEST_LOCALE, WAR_TEST_TEXT);
            return source;
        }
        
        @Override
        public void configureI18nContextProvider(
                final @NotNull I18nSpringContextProvider.Builder builder) {
            builder.configure(warConfiguration())
                    .addI18nResources("alt", altMessageSource());
        }
    }

    static class SpringContextRunnable
    implements Runnable {

        AnnotationConfigApplicationContext parent;
        AnnotationConfigApplicationContext child;

        @Override
        public void run() {
            parent = new AnnotationConfigApplicationContext();
            parent.register(EarSpringConfig.class);
            parent.refresh();
            child = new AnnotationConfigApplicationContext();
            child.setParent(parent);
            child.register(WarSpringConfig.class);
            child.refresh();
        }
    }

    static class I18nContextTestRunnable
    implements Runnable {

        Locale nextLocale = TEST_LOCALE;
        Locale initialLocale;
        String initialText;
        Locale finalLocale;
        String finalText;

        @Override
        public void run() {
            this.initialLocale = I18N.getLocale();
            this.initialText = I18N.getResources().getMessage("Initial text", "test.message");
            I18N.setLocale(nextLocale);
            this.finalLocale = I18N.getLocale();
            this.finalText = I18N.getResources().getMessage("Final text", "test.message");
        }
    }
}
