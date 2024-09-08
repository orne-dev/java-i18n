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

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.net.URLClassLoader;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.SpringVersion;

import dev.orne.i18n.I18N;
import dev.orne.i18n.context.ContextTestUtils;
import dev.orne.i18n.context.DefaultI18nContextProvider;
import dev.orne.i18n.context.I18nContextProvider;
import dev.orne.i18n.spring.I18nSpringBaseConfiguration;

/**
 * Integrity test for {@code I18nSpringBaseConfiguration.targetClass}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-10
 * @since 0.1
 * @see I18nSpringBaseConfiguration
 */
@Tag("spring-classloader")
public class SpringClassLoaderI18nConfigurerTest {

    private static I18nContextProvider provider;
    private static ClassLoader bootCL;
    private static ClassLoader springCL;
    private static ClassLoader libCL;
    private static ClassLoader testCL;
    private static ClassLoader threadCL;

    @BeforeAll
    static void createComplexClassLoader() {
        provider = new DefaultI18nContextProvider();
        bootCL = ClassLoader.getSystemClassLoader();
        while (bootCL.getParent() != null) {
            bootCL = bootCL.getParent();
        }
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
                    SpringClassLoaderI18nConfigurerTest.class.getProtectionDomain().getCodeSource().getLocation(),
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
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Runtime()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, bootCL);
        test.targetClass = Runtime.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_System()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, ClassLoader.getSystemClassLoader());
        test.targetClass = System.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Spring()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, springCL);
        test.targetClass = SpringVersion.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Lib()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, libCL);
        test.targetClass = I18N.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Test()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, testCL);
        test.targetClass = SpringClassLoaderI18nConfigurerTest.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }

    /**
     * Test {@link I18nSpringBaseConfiguration#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Thread()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(provider, threadCL);
        test.targetClass = Thread.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertSame(provider, test.resultClassLoaderProvider);
    }


    class ClassLoaderSelectionTestRunnable
    implements Runnable {

        private final @NotNull I18nContextProvider provider;
        private final @NotNull ClassLoader cl;
        private Class<?> targetClass;
        private I18nContextProvider resultDefaultProvider;
        private I18nContextProvider resultClassLoaderProvider;

        public ClassLoaderSelectionTestRunnable(
                final @NotNull I18nContextProvider provider,
                final @NotNull ClassLoader cl) {
            super();
            this.provider = provider;
            this.cl = cl;
        }

        @Override
        public void run() {
            final I18nSpringBaseConfiguration configurer = new I18nSpringBaseConfiguration();
            configurer.setContextProvider(this.provider);
            if (this.targetClass != null) {
                try {
                    configurer.setTargetClass(Class.forName(
                            this.targetClass.getName(),
                            false,
                            Thread.currentThread().getContextClassLoader()));
                } catch (final ClassNotFoundException ignore) {
                    // Ignored
                }
            }
            configurer.afterPropertiesSet();
            I18nContextProvider.getInstance();
            resultDefaultProvider = I18nContextProvider.getInstance();
            resultClassLoaderProvider = I18nContextProvider.Registry.get(cl);
        }
    }
}
