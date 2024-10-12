package dev.orne.i18n.spring;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.i18n.I18N;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.DummyI18nResources;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Unit tests for {@code I18nSpringConfiguration}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringConfiguration
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nSpringConfigurationTest {

    private @Mock ClassLoader classLoader;
    private @Mock ApplicationContext applicationContext;
    private @Mock MessageSource messageSource;

    private static ClassLoader bootCL;
    private static ClassLoader springCL;
    private static ClassLoader libCL;
    private static ClassLoader testCL;
    private static ClassLoader threadCL;

    @BeforeAll
    static void createComplexClassLoader() {
        bootCL = ClassLoader.getSystemClassLoader();
        springCL = new URLClassLoader(
                new URL[] {
                    org.slf4j.Logger.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.apache.commons.logging.impl.SLF4JLog.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.apache.commons.lang3.AnnotationUtils.class.getProtectionDomain().getCodeSource().getLocation(),
                    ch.qos.logback.classic.Logger.class.getProtectionDomain().getCodeSource().getLocation(),
                    ch.qos.logback.core.Context.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.core.SpringVersion.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.beans.BeanUtils.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.context.ApplicationContext.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                bootCL);
        libCL = new URLClassLoader(
                new URL[] {
                    I18N.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                springCL);
        testCL = new URLClassLoader(
                new URL[] {
                    I18nSpringConfigurationTest.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                libCL);
        threadCL = new URLClassLoader(
                new URL[] {
                },
                testCL);
    }

    @AfterEach
    void resetI18N() {
        ContextTestUtils.reset();
    }

    /**
     * Test {@link I18nSpringConfiguration#I18nSpringBaseConfigurer()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringConfiguration config = new I18nSpringConfiguration();
        assertNull(config.getApplicationContext());
        assertNull(config.getMessageSource());
        assertNull(config.getTarget());
    }

    /**
     * Test {@link I18nSpringConfiguration#setApplicationContext(ApplicationContext)}.
     */
    @Test
    void testSetApplicationContext() {
        final I18nSpringConfiguration config = new I18nSpringConfiguration();
        config.setApplicationContext(applicationContext);
        assertSame(applicationContext, config.getApplicationContext());
    }

    /**
     * Test {@link I18nSpringConfiguration#setMessageSource(MessageSource)}.
     */
    @Test
    void testSetMessageSource() {
        final I18nSpringConfiguration config = new I18nSpringConfiguration();
        config.setMessageSource(messageSource);
        assertSame(messageSource, config.getMessageSource());
    }

    /**
     * Test {@link I18nSpringConfiguration#setTarget(ClassLoader)}.
     */
    @Test
    void testSetTarget() {
        final I18nSpringConfiguration config = new I18nSpringConfiguration();
        config.setTarget(I18nSpringConfigurationTest.class.getClassLoader());
        assertSame(I18nSpringConfigurationTest.class.getClassLoader(), config.getTarget());
    }

    /**
     * Test {@link I18nSpringConfiguration#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("classLoader", Void.class);
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfiguration configurer = new I18nSpringConfiguration();
        assertNull(configurer.getTarget());
        attrs.put("classLoader", I18nSpringConfigurationTest.class);
        configurer.setImportMetadata(metadata);
        assertSame(I18nSpringConfigurationTest.class.getClassLoader(), configurer.getTarget());
    }

    /**
     * Test {@link I18nSpringConfiguration#determineConfigurer()}.
     */
    @Test
    void testDetermineConfigurer() {
        final I18nSpringConfiguration config = new I18nSpringConfiguration();
        assertSame(I18nSpringConfiguration.DEFAULT_CONFIGURER, config.determineConfigurer());
        config.setApplicationContext(applicationContext);
        final Map<String, I18nSpringConfigurer> configurers = new HashMap<>();
        given(applicationContext.getBeansOfType(I18nSpringConfigurer.class)).willReturn(configurers);
        assertSame(I18nSpringConfiguration.DEFAULT_CONFIGURER, config.determineConfigurer());
        final I18nSpringConfigurer configurer = mock(I18nSpringConfigurer.class);
        configurers.put("someBeanName", configurer);
        assertSame(configurer, config.determineConfigurer());
        final I18nSpringConfigurer configurer2 = mock(I18nSpringConfigurer.class);
        configurers.put("anotherBeanName", configurer2);
        assertThrows(IllegalStateException.class, () -> {
            config.determineConfigurer();
        });
    }

    /**
     * Test {@link I18nSpringConfigurer}.
     */
    @Test
    void testDefaultConfigurer() {
        final I18nSpringConfigurer configurer = I18nSpringConfiguration.DEFAULT_CONFIGURER;
        final I18nSpringContextProvider.Builder builder = configurer.getI18nContextProviderBuilder();
        configurer.configureI18nContextProvider(builder);
        final I18nSpringContextProvider provider = builder.build();
        assertTrue(provider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(provider.getI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        assertEquals(Locale.getDefault(), provider.getContext().getLocale());
    }

    /**
     * Test {@link I18nSpringConfiguration#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider() {
        final I18nSpringConfiguration config = spy(new I18nSpringConfiguration());
        final I18nSpringConfigurer configurer = mock(I18nSpringConfigurer.class);
        willReturn(configurer).given(config).determineConfigurer();
        given(configurer.getI18nContextProviderBuilder()).will(inv -> I18nSpringContextProvider.builder());
        I18nSpringContextProvider provider = config.createContextProvider();
        assertTrue(provider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(provider.getI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        assertEquals(Locale.getDefault(), provider.getContext().getLocale());
        config.setMessageSource(messageSource);
        provider = config.createContextProvider();
        assertTrue(provider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), provider.getAvailableLocales());
        assertTrue(provider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(((I18nSpringResources) provider.getDefaultI18nResources()).getSource(), messageSource);
        assertNotNull(provider.getI18nResources());
        assertTrue(provider.getI18nResources().isEmpty());
        assertEquals(Locale.getDefault(), provider.getContext().getLocale());
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet() {
        final I18nContextProvider previousProvider = I18nContextProvider.getInstance();
        final I18nSpringConfiguration configurer = new I18nSpringConfiguration();
        final I18nContextProvider expected = configurer.createContextProvider();
        configurer.afterPropertiesSet();
        final I18nContextProvider defaultProvider = I18nContextProvider.getInstance();
        assertNotSame(previousProvider, defaultProvider);
        assertEquals(expected, defaultProvider);
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Target_Spring()
    throws InterruptedException {
        final I18nSpringContextProvider provider = mock(I18nSpringContextProvider.class);
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider);
        test.target = springCL;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertSame(provider, I18nContextProvider.Registry.get(springCL));
        assertSame(provider, test.resultProvider);
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Target_Lib()
    throws InterruptedException {
        final I18nSpringContextProvider provider = mock(I18nSpringContextProvider.class);
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider);
        test.target = libCL;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertSame(provider, I18nContextProvider.Registry.get(libCL));
        assertSame(provider, test.resultProvider);
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Target_Test()
    throws InterruptedException {
        final I18nSpringContextProvider provider = mock(I18nSpringContextProvider.class);
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider);
        test.target = testCL;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertSame(provider, I18nContextProvider.Registry.get(testCL));
        assertSame(provider, test.resultProvider);
    }

    /**
     * Test {@link I18nSpringConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Target_Thread()
    throws InterruptedException {
        final I18nSpringContextProvider provider = mock(I18nSpringContextProvider.class);
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider);
        test.target = threadCL;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertSame(provider, I18nContextProvider.Registry.get(threadCL));
        assertSame(provider, test.resultProvider);
    }

    class ClassLoaderSelectionTestRunnable
    implements Runnable {

        private final @NotNull I18nContextProvider provider;
        private ClassLoader target;
        private I18nContextProvider resultProvider;

        public ClassLoaderSelectionTestRunnable(
                final @NotNull I18nContextProvider provider) {
            super();
            this.provider = provider;
        }

        @Override
        public void run() {
            final I18nSpringConfiguration config = spy(new I18nSpringConfiguration());
            willReturn(this.provider).given(config).createContextProvider();
            if (this.target != null) {
                config.setTarget(target);
            }
            config.afterPropertiesSet();
            if (this.target == null) {
                resultProvider = I18nContextProvider.Registry.get();
            } else {
                resultProvider = I18nContextProvider.Registry.get(this.target);
            }
        }
    }
}
