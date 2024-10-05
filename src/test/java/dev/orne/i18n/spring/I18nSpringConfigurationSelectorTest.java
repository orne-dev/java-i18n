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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.i18n.I18N;

/**
 * Unit tests for {@code I18nSpringConfigurationSelector}.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2023-10
 * @since 0.1
 * @see I18nSpringConfigurationSelector
 */
@Tag("ut")
@ExtendWith(MockitoExtension.class)
class I18nSpringConfigurationSelectorTest {

    private @Mock AnnotationMetadata metadata;

    private static ClassLoader bootCL;
    private static ClassLoader simpleCL;
    private static ClassLoader webCL;

    @BeforeAll
    static void createComplexClassLoader() {
        bootCL = ClassLoader.getSystemClassLoader();
        while (bootCL.getParent() != null) {
            bootCL = bootCL.getParent();
        }
        simpleCL = new URLClassLoader(
                new URL[] {
                    org.slf4j.Logger.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.apache.commons.logging.impl.SLF4JLog.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.apache.commons.lang3.AnnotationUtils.class.getProtectionDomain().getCodeSource().getLocation(),
                    ch.qos.logback.classic.Logger.class.getProtectionDomain().getCodeSource().getLocation(),
                    ch.qos.logback.core.Context.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.core.SpringVersion.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.beans.BeanUtils.class.getProtectionDomain().getCodeSource().getLocation(),
                    org.springframework.context.ApplicationContext.class.getProtectionDomain().getCodeSource().getLocation(),
                    I18N.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                bootCL);
        webCL = new URLClassLoader(
                new URL[] {
                    org.springframework.web.context.support.RequestHandledEvent.class.getProtectionDomain().getCodeSource().getLocation(),
                },
                simpleCL);
    }

    /**
     * Test {@link I18nSpringConfigurationSelector#I18nSpringConfiguration()}.
     */
    @Test
    void testDefaultConstructor() {
        assertDoesNotThrow(() -> new I18nSpringConfigurationSelector());
    }

    /**
     * Test {@link I18nSpringConfigurationSelector#selectImports(AnnotationMetadata)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void testNoSpringWebConfigurationSelection(
            final boolean fromAnnotation)
    throws InterruptedException {
        final SelectorTestRunnable test = new SelectorTestRunnable();
        if (fromAnnotation) {
            test.metadata = metadata;
        }
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(simpleCL);
        childThread.start();
        childThread.join();
        assertNotNull(test.result);
        assertEquals(1, test.result.length);
        assertEquals(I18nSpringConfiguration.class.getName(), test.result[0]);
        then(metadata).shouldHaveNoInteractions();
    }

    /**
     * Test {@link I18nSpringConfigurationSelector#selectImports(AnnotationMetadata)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={true, false})
    void testSpringWebConfigurationSelection(
            final boolean fromAnnotation)
    throws InterruptedException {
        final SelectorTestRunnable test = new SelectorTestRunnable();
        if (fromAnnotation) {
            test.metadata = metadata;
        }
        final Thread childThread = new Thread(test);
        childThread.setContextClassLoader(webCL);
        childThread.start();
        childThread.join();
        assertNotNull(test.result);
        assertEquals(1, test.result.length);
        assertEquals(I18nSpringWebConfiguration.class.getName(), test.result[0]);
        then(metadata).shouldHaveNoInteractions();
    }

    class SelectorTestRunnable
    implements Runnable {

        private AnnotationMetadata metadata;
        private String[] result;

        @Override
        public void run() {
            I18nSpringConfigurationSelector selector = new I18nSpringConfigurationSelector();
            result = selector.selectImports(metadata);
        }
    }
}
