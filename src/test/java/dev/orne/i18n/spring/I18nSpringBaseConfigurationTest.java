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
import java.util.Arrays;
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
import org.springframework.context.MessageSource;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.i18n.I18N;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.DummyI18nResources;
import dev.orne.i18n.context.I18nContextProvider;

/**
 * Unit tests for {@code I18nSpringBaseConfiguration}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringBaseConfiguration
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nSpringBaseConfigurationTest {

    private @Mock ClassLoader classLoader;
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
                    I18nSpringBaseConfigurationTest.class.getProtectionDomain().getCodeSource().getLocation(),
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
     * Test {@link I18nSpringBaseConfiguration#I18nSpringBaseConfigurer()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringBaseConfiguration config = new I18nSpringBaseConfiguration();
        assertNull(config.getMessageSource());
        assertNotNull(config.getConfigurer());
        assertNull(config.getTarget());
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#setTarget(ClassLoader)}.
     */
    @Test
    void testSetTarget() {
        final I18nSpringBaseConfiguration config = new I18nSpringBaseConfiguration();
        config.setTarget(I18nSpringBaseConfigurationTest.class.getClassLoader());
        assertSame(I18nSpringBaseConfigurationTest.class.getClassLoader(), config.getTarget());
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#setContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetConfigurers() {
        final I18nSpringBaseConfiguration config = new I18nSpringBaseConfiguration();
        final I18nSpringConfigurer defaultConfigurer = config.getConfigurer();
        config.setConfigurers(Arrays.asList());
        assertSame(defaultConfigurer, config.getConfigurer());
        final I18nSpringConfigurer configurer = mock(I18nSpringConfigurer.class);
        config.setConfigurers(Arrays.asList(configurer));
        assertSame(configurer, config.getConfigurer());
        final I18nSpringConfigurer configurer2 = mock(I18nSpringConfigurer.class);
        assertThrows(IllegalStateException.class, () -> {
            config.setConfigurers(Arrays.asList(configurer, configurer2));
        });
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("classLoader", Void.class);
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringBaseConfiguration configurer = new I18nSpringBaseConfiguration();
        assertNull(configurer.getTarget());
        attrs.put("classLoader", I18nSpringBaseConfigurationTest.class);
        configurer.setImportMetadata(metadata);
        assertSame(I18nSpringBaseConfigurationTest.class.getClassLoader(), configurer.getTarget());
    }

    /**
     * Test {@link I18nSpringConfigurer}.
     */
    @Test
    void testDefaultConfigurer() {
        final I18nSpringBaseConfiguration config = new I18nSpringBaseConfiguration();
        final I18nSpringConfigurer configurer = config.getConfigurer();
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
     * Test {@link I18nSpringBaseConfiguration#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider() {
        final I18nSpringBaseConfiguration config = new I18nSpringBaseConfiguration();
        final I18nSpringConfigurer configurer = mock(I18nSpringConfigurer.class);
        given(configurer.getI18nContextProviderBuilder()).will((inv) -> I18nSpringContextProvider.builder());
        config.setConfigurers(Arrays.asList(configurer));
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
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet() {
        final I18nContextProvider previousProvider = I18nContextProvider.getInstance();
        final I18nSpringBaseConfiguration configurer = new I18nSpringBaseConfiguration();
        final I18nContextProvider expected = configurer.createContextProvider();
        configurer.afterPropertiesSet();
        final I18nContextProvider defaultProvider = I18nContextProvider.getInstance();
        assertNotSame(previousProvider, defaultProvider);
        assertEquals(expected, defaultProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
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
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
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
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
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
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
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
            final I18nSpringBaseConfiguration config = spy(new I18nSpringBaseConfiguration());
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
