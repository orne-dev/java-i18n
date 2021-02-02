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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.core.SpringVersion;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.i18n.DefaultI18nContextProvider;
import dev.orne.i18n.DummyI18nResources;
import dev.orne.i18n.I18N;
import dev.orne.i18n.I18nContextProvider;
import dev.orne.i18n.I18nContextProviderByClassLoaderStrategy;
import dev.orne.i18n.I18nResources;
import jakarta.validation.constraints.NotNull;

/**
 * Unit tests for {@code I18nSpringConfigurer}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18nSpringConfigurer
 */
@Tag("ut")
class I18nSpringConfigurerTest {

    private @Mock ClassLoader classLoader;
    private @Mock I18nContextProvider provider;
    private @Mock MessageSource messageSource;
    private @Mock I18nResources resources;
    private @Mock I18nResources altResources;

    private static ClassLoader bootCL;
    private static ClassLoader springCL;
    private static ClassLoader libCL;
    private static ClassLoader testCL;
    private static ClassLoader threadCL;

    @BeforeAll
    static void createComplexClassLoader() {
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
                    I18nSpringConfigurerTest.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                libCL);
        threadCL = new URLClassLoader(
                new URL[] {
                },
                testCL);
    }

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void resetI18N() {
        I18N.reconfigure();
    }

    /**
     * Test {@link I18nSpringConfigurer#I18nSpringConfigurer()}.
     */
    @Test
    void testConstructor() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        assertNull(configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertTrue(configurer.isInheritableContexts());
        assertNull(configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setTargetClass(Class)}.
     */
    @Test
    void testSetTargetClass() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setTargetClass(I18nSpringConfigurerTest.class);
        assertSame(I18nSpringConfigurerTest.class, configurer.getTargetClass());
    }

    /**
     * Test {@link I18nSpringConfigurer#setTargetClass(Class)}.
     */
    @Test
    void testSetTargetClass_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setTargetClass(I18nSpringConfigurerTest.class);
        configurer.setTargetClass(null);
        assertNull(configurer.getTargetClass());
    }

    /**
     * Test {@link I18nSpringConfigurer#setContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setContextProvider(provider);
        assertSame(provider, configurer.getContextProvider());
    }

    /**
     * Test {@link I18nSpringConfigurer#setContextProvider(I18nContextProvider)}.
     */
    @Test
    void testSetContextProvider_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setContextProvider(provider);
        configurer.setContextProvider(null);
        assertNull(configurer.getContextProvider());
    }

    /**
     * Test {@link I18nSpringConfigurer#setInheritableContexts(boolean)}.
     */
    @Test
    void testSetInheritableContexts() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setInheritableContexts(false);
        assertFalse(configurer.isInheritableContexts());
    }

    /**
     * Test {@link I18nSpringConfigurer#setInheritableContexts(boolean)}.
     */
    @Test
    void testSetInheritableContexts_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setInheritableContexts(false);
        configurer.setInheritableContexts(true);
        assertTrue(configurer.isInheritableContexts());
    }

    /**
     * Test {@link I18nSpringConfigurer#setAvailableLocales(Locale[])}.
     */
    @Test
    void testSetAvailableLocales() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setAvailableLocales(locales);
        assertArrayEquals(locales, configurer.getAvailableLocales());
    }

    /**
     * Test {@link I18nSpringConfigurer#setAvailableLocales(Locale[])}.
     */
    @Test
    void testSetAvailableLocales_Null() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setAvailableLocales(locales);
        configurer.setAvailableLocales(null);
        assertNull(configurer.getAvailableLocales());
    }

    /**
     * Test {@link I18nSpringConfigurer#setFullModeByDefault(boolean)}.
     */
    @Test
    void testSetFullModeByDefault() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setFullModeByDefault(true);
        assertTrue(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setFullModeByDefault(boolean)}.
     */
    @Test
    void testSetFullModeByDefault_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setFullModeByDefault(true);
        configurer.setFullModeByDefault(false);
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setDefaultMessageSource(MessageSource)}.
     */
    @Test
    void testSetDefaultMessageSource() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultMessageSource(messageSource);
        assertSame(messageSource, configurer.getDefaultMessageSource());
    }

    /**
     * Test {@link I18nSpringConfigurer#setDefaultMessageSource(MessageSource)}.
     */
    @Test
    void testSetDefaultMessageSource_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultMessageSource(messageSource);
        configurer.setDefaultMessageSource(null);
        assertNull(configurer.getDefaultMessageSource());
    }

    /**
     * Test {@link I18nSpringConfigurer#setDefaultI18nResources(I18nResources)}.
     */
    @Test
    void testSetDefaultI18nResources() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultI18nResources(resources);
        assertSame(resources, configurer.getDefaultI18nResources());
    }

    /**
     * Test {@link I18nSpringConfigurer#setDefaultI18nResources(I18nResources)}.
     */
    @Test
    void testSetDefaultI18nResources_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultI18nResources(resources);
        configurer.setDefaultI18nResources(null);
        assertNull(configurer.getDefaultI18nResources());
    }

    /**
     * Test {@link I18nSpringConfigurer#setNamedI18nResources(Map)
     */
    @Test
    void testSetNamedI18nResources() {
        final Map<String, I18nResources> namedResources = new HashMap<>();
        namedResources.put("main", resources);
        namedResources.put("alt", altResources);
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setNamedI18nResources(namedResources);
        assertEquals(namedResources, configurer.getNamedI18nResources());
    }

    /**
     * Test {@link I18nSpringConfigurer#setDefaultMessageSource(MessageSource)}.
     */
    @Test
    void testSetNamedI18nResources_Null() {
        final Map<String, I18nResources> namedResources = new HashMap<>();
        namedResources.put("main", resources);
        namedResources.put("alt", altResources);
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setNamedI18nResources(namedResources);
        configurer.setNamedI18nResources(null);
        assertNull(configurer.getNamedI18nResources());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("targetClass", Void.class);
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setImportMetadata(metadata);
        assertNull(configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertNull(configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata_All() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final String[] langs = new String[] {
                Locale.ENGLISH.getLanguage(),
                Locale.FRENCH.getLanguage()
        };
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("targetClass", I18nSpringConfigurerTest.class);
        attrs.put("availableLanguages", langs);
        attrs.put("fullModeByDefault", true);
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setImportMetadata(metadata);
        assertSame(I18nSpringConfigurerTest.class, configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertArrayEquals(locales, configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertTrue(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata_AvailableLocales_Empty() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put("targetClass", Void.class);
        attrs.put("availableLanguages", new String[0]);
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setImportMetadata(metadata);
        assertNull(configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertNull(configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata_Empty() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        final Map<String, Object> attrs = new HashMap<>();
        willReturn(attrs).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setImportMetadata(metadata);
        assertNull(configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertNull(configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata_NoAnnotationAttributes() {
        final AnnotationMetadata metadata = mock(AnnotationMetadata.class);
        willReturn(null).given(metadata).getAnnotationAttributes(EnableI18N.class.getName());
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setImportMetadata(metadata);
        assertNull(configurer.getTargetClass());
        assertNull(configurer.getContextProvider());
        assertNull(configurer.getAvailableLocales());
        assertNull(configurer.getDefaultMessageSource());
        assertNull(configurer.getDefaultI18nResources());
        assertNull(configurer.getNamedI18nResources());
        assertFalse(configurer.isFullModeByDefault());
    }

    /**
     * Test {@link I18nSpringConfigurer#setImportMetadata(AnnotationMetadata)}.
     */
    @Test
    void testSetImportMetadata_Null() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        assertThrows(NullPointerException.class, () -> {
            configurer.setImportMetadata(null);
        });
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_NotInheritable() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setInheritableContexts(false);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertFalse(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_AvailableLocales() {
        final Locale[] locales = new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH
        };
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setAvailableLocales(locales);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(locales, springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_FullMode() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setFullModeByDefault(true);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertTrue(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_MessageSource() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultMessageSource(this.messageSource);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof I18nSpringResources);
        assertSame(this.messageSource, ((I18nSpringResources) provider.getDefaultI18nResources()).getSource());
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_DefaultI18nResources() {
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setDefaultI18nResources(this.resources);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertSame(this.resources, springProvider.getDefaultI18nResources());
        assertNotNull(springProvider.getI18nResources());
        assertTrue(springProvider.getI18nResources().isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#createContextProvider()}.
     */
    @Test
    void testCreateContextProvider_NamedI18nResources() {
        final Map<String, I18nResources> namedResources = new HashMap<>();
        namedResources.put("main", resources);
        namedResources.put("alt", altResources);
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setNamedI18nResources(namedResources);
        final I18nContextProvider provider = configurer.createContextProvider();
        assertTrue(provider instanceof I18nSpringContextProvider);
        final I18nSpringContextProvider springProvider = (I18nSpringContextProvider) provider;
        assertTrue(springProvider.isInheritable());
        assertArrayEquals(Locale.getAvailableLocales(), springProvider.getAvailableLocales());
        assertFalse(springProvider.isFullModeByDefault());
        assertTrue(springProvider.getDefaultI18nResources() instanceof DummyI18nResources);
        assertEquals(namedResources, springProvider.getI18nResources());
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet() {
        final I18nContextProvider previousProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        final I18nContextProvider expected = configurer.createContextProvider();
        configurer.afterPropertiesSet();
        final I18nContextProvider defaultProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        assertNotSame(previousProvider, defaultProvider);
        assertEquals(expected, defaultProvider);
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_ContextProvider() {
        final I18nContextProvider previousProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setContextProvider(this.provider);
        configurer.afterPropertiesSet();
        final I18nContextProvider defaultProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        assertNotSame(previousProvider, defaultProvider);
        assertSame(this.provider, defaultProvider);
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_TargetClass() {
        final I18nContextProvider previousProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
        configurer.setContextProvider(this.provider);
        configurer.setTargetClass(I18nSpringConfigurerTest.class);
        configurer.afterPropertiesSet();
        final I18nContextProvider defaultProvider = I18N.getContextProviderStrategy().getDefaultContextProvider();
        assertNotSame(previousProvider, defaultProvider);
        assertSame(this.provider, defaultProvider);
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertSame(this.provider, test.resultDefaultProvider);
        assertNotNull(test.resultProviders);
        assertTrue(test.resultProviders.isEmpty());
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Runtime()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = Runtime.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(bootCL));
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_System()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = System.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(ClassLoader.getSystemClassLoader()));
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Spring()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = SpringVersion.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(springCL));
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Lib()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = I18N.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(libCL));
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Test()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = I18nSpringConfigurerTest.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(testCL));
    }

    /**
     * Test {@link I18nSpringConfigurer#afterPropertiesSet()}.
     */
    @Test
    void testAfterPropertiesSet_Configurable_TargetClass_Thread()
    throws InterruptedException {
        final ClassLoaderSelectionTestRunnable test = new ClassLoaderSelectionTestRunnable(this.provider);
        test.targetClass = Thread.class;
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(threadCL);
        childThread.start();
        childThread.join();
        assertTrue(test.resultDefaultProvider instanceof DefaultI18nContextProvider);
        assertNotNull(test.resultProviders);
        assertEquals(1, test.resultProviders.size());
        assertSame(this.provider, test.resultProviders.get(threadCL));
    }

    static class TestI18nContextProviderByClassLoaderStrategy
    extends I18nContextProviderByClassLoaderStrategy {

        @Override
        protected Map<ClassLoader, I18nContextProvider> getContextProviders() {
            return super.getContextProviders();
        }
    }

    class ClassLoaderSelectionTestRunnable
    implements Runnable {

        private final @NotNull I18nContextProvider provider;
        private Class<?> targetClass;
        private I18nContextProvider resultDefaultProvider;
        private Map<ClassLoader, I18nContextProvider> resultProviders;

        public ClassLoaderSelectionTestRunnable(
                final @NotNull I18nContextProvider provider) {
            super();
            this.provider = provider;
        }

        @Override
        public void run() {
            final TestI18nContextProviderByClassLoaderStrategy strategy =
                    new TestI18nContextProviderByClassLoaderStrategy();
            I18N.setContextProviderStrategy(strategy);
            final I18nSpringConfigurer configurer = new I18nSpringConfigurer();
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
                    I18N.getContextProviderStrategy();
            resultDefaultProvider = strategy.getDefaultContextProvider();
            resultProviders = strategy.getContextProviders();
        }
    }
}
