package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2023 Orne Developments
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;

import dev.orne.i18n.I18nConfigurationException;

/**
 * Unit tests for {@code I18nConfiguration} runtime configuration.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-12
 * @since 0.1
 * @see I18nConfiguration
 */
@Tag("ut")
class I18nConfigurationTest {

    private static Properties defaultConfig;

    private List<Path> tmpFolder;

    @BeforeAll
    public static void loadDefaultConfiguration() throws IOException {
        defaultConfig = new Properties();
        try (final InputStream is = I18nConfiguration.class.getResourceAsStream(I18nConfiguration.DEFAULT_CFG)) {
            defaultConfig.load(is);
        }
    }

    @BeforeAll
    public static void resetPreviousConfiguration() {
        ContextTestUtils.reset();
    }

    @AfterEach
    public void removeTmpFolder()
    throws IOException {
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
    public void resetI18N() {
        ContextTestUtils.reset();
    }

    private ClassLoader createConfigClassLoader(
            final Properties config)
    throws IOException {
        return createConfigClassLoader(config, Thread.currentThread().getContextClassLoader());
    }

    private ClassLoader createConfigClassLoader(
            final Properties config,
            final @NotNull ClassLoader parent)
    throws IOException {
        if (this.tmpFolder == null) {
            this.tmpFolder = new ArrayList<>();
        }
        final Path folder = Files.createTempDirectory("testTmp");
        this.tmpFolder.add(folder);
        if (config != null) {
            final Path cfgFile = folder.resolve(I18nConfiguration.FILE);
            try (final OutputStream out = Files.newOutputStream(cfgFile)) {
                config.store(out, null);
            }
        }
        return new URLClassLoader(
                new URL[] { folder.toUri().toURL() },
                parent);
    }

    /**
     * Test {@link I18nConfiguration#loadConfiguration(ClassLoader)}.
     */
    @Test
    @DisabledForJreRange(min=JRE.JAVA_12, disabledReason="Works in JDK 11. Mock misbehaves in JDK 17+")
    void testLoadConfiguration_ResourcesIOException()
    throws IOException {
        final ClassLoader cl = spy(createConfigClassLoader(null));
        final IOException exception = new IOException();
        willThrow(exception).given(cl).getResources(I18nConfiguration.FILE);
        assertThrows(I18nConfigurationException.class, () -> {
            I18nConfiguration.loadConfiguration(cl);
        });
    }

    /**
     * Test {@link I18nConfiguration#loadConfiguration(ClassLoader)}.
     */
    @Test
    @DisabledForJreRange(min=JRE.JAVA_12, disabledReason="Works in JDK 11. Mock misbehaves in JDK 17+")
    void testLoadConfiguration_ReadIOException()
    throws IOException {
        final ClassLoader cl = spy(createConfigClassLoader(null));
        final URL url = Paths.get("missing", "config", "file.properties").toUri().toURL();
        final List<URL> urls = Arrays.asList(url);
        willReturn(Collections.enumeration(urls)).given(cl).getResources(I18nConfiguration.FILE);
        assertThrows(I18nConfigurationException.class, () -> {
            I18nConfiguration.loadConfiguration(cl);
        });
    }

    /**
     * Test {@link I18nConfiguration#loadConfiguration(ClassLoader)}.
     */
    @Test
    @DisabledForJreRange(min=JRE.JAVA_12, disabledReason="Works in JDK 11. Mock misbehaves in JDK 17+")
    void testLoadConfiguration_MultipleFiles()
    throws IOException {
        final ClassLoader cl = spy(createConfigClassLoader(null));
        final URL url1 = Paths.get("mock", "config", "file.properties").toUri().toURL();
        final URL url2 = Paths.get("mock", "config", "file2.properties").toUri().toURL();
        final List<URL> urls = Arrays.asList(url1, url2);
        willReturn(Collections.enumeration(urls)).given(cl).getResources(I18nConfiguration.FILE);
        assertThrows(I18nConfigurationException.class, () -> {
            I18nConfiguration.loadConfiguration(cl);
        });
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoad()
    throws Exception {
        final Properties expected = new Properties();
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(expected);
        final Properties result = I18nConfiguration.get(cl);
        assertEquals(expected, result);
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoad_NotFound() {
        final Properties result = I18nConfiguration.get(
                Thread.currentThread().getContextClassLoader());
        assertNotNull(result);
        assertEquals(defaultConfig, result);
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoad_IOException()
    throws IOException {
        final ClassLoader cl = mock(ClassLoader.class);
        final InputStream is = mock(InputStream.class);
        willReturn(is).given(cl).getResourceAsStream(I18nConfiguration.FILE);
        willThrow(IOException.class).given(is).read();
        willThrow(IOException.class).given(is).read(any());
        willThrow(IOException.class).given(is).read(any(), anyInt(), anyInt());
        final Properties result = I18nConfiguration.get(cl);
        assertNotNull(result);
        assertEquals(defaultConfig, result);
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoad_Inherited()
    throws IOException {
        final Properties expected = new Properties();
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader parentCL = createConfigClassLoader(expected);
        final ClassLoader cl = new URLClassLoader(
                new URL[] {  },
                parentCL);
        final Properties result = I18nConfiguration.get(cl);
        assertEquals(expected, result);
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoad_Overriden()
    throws IOException {
        final Properties inherited = new Properties();
        inherited.setProperty("inherited.prop", "mock.value");
        final ClassLoader parentCL = createConfigClassLoader(inherited);
        final Properties expected = new Properties();
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(expected, parentCL);
        final Properties result = I18nConfiguration.get(cl);
        assertEquals(expected, result);
    }

    /**
     * Test {@link I18nConfiguration#get()}.
     */
    @Test
    void testGet()
    throws Exception {
        final Properties expected = new Properties();
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(expected);
        final ConfigurationTestRunnable childTest = new ConfigurationTestRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertEquals(expected, childTest.result);
    }

    /**
     * Test {@link I18nConfiguration#get()}.
     */
    @Test
    void testSet()
    throws Exception {
        final Properties old = new Properties();
        old.setProperty("mock.prop", "mock.value");
        final Properties child2Cfg = new Properties();
        child2Cfg.setProperty("mock.prop", "child2.mock.value");
        final Properties grandchildCfg = new Properties();
        grandchildCfg.setProperty("mock.prop", "grandchild.mock.value");
        final Properties expected = new Properties();
        expected.setProperty("mock.new.prop", "new.mock.value");
        final ClassLoader parent = createConfigClassLoader(null);
        final ClassLoader cl = createConfigClassLoader(old, parent);
        final ClassLoader child = createConfigClassLoader(null, cl);
        final ClassLoader child2 = createConfigClassLoader(child2Cfg, cl);
        final ClassLoader grandchild = createConfigClassLoader(grandchildCfg, child);
        final ClassLoader grandchild2 = createConfigClassLoader(null, child);
        assertEquals(defaultConfig, I18nConfiguration.get(parent));
        assertEquals(old, I18nConfiguration.get(cl));
        assertEquals(old, I18nConfiguration.get(child));
        assertEquals(child2Cfg, I18nConfiguration.get(child2));
        assertEquals(grandchildCfg, I18nConfiguration.get(grandchild));
        assertEquals(old, I18nConfiguration.get(grandchild2));
        I18nConfiguration.set(cl, expected);
        assertEquals(defaultConfig, I18nConfiguration.get(parent));
        assertEquals(expected, I18nConfiguration.get(cl));
        assertEquals(expected, I18nConfiguration.get(child));
        assertEquals(child2Cfg, I18nConfiguration.get(child2));
        assertEquals(grandchildCfg, I18nConfiguration.get(grandchild));
        assertEquals(expected, I18nConfiguration.get(grandchild2));
    }

    private static class ConfigurationTestRunnable
    implements Runnable {
        private Properties result;
        @Override
        public void run() {
            this.result = I18nConfiguration.get();
        }
    }
}
