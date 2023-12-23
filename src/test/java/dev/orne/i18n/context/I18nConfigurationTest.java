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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18N;
import jakarta.validation.constraints.NotNull;

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

    private Path tmpFolder;
    private Path cfgFile;

    @AfterEach
    public void removeTmpFolder()
    throws IOException {
        if (this.tmpFolder != null) {
            Files.walk(this.tmpFolder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @AfterEach
    public void resetI18N() {
        I18nContextProviderStrategy.setInstance(null);
    }

    private ClassLoader createConfigClassLoader(
            final @NotNull Properties config)
    throws IOException {
        this.tmpFolder = Files.createTempDirectory("testTmp");
        this.cfgFile = tmpFolder.resolve(I18nConfiguration.FILE);
        try (final OutputStream out = Files.newOutputStream(cfgFile)) {
            config.store(out, null);
        }
        final Path servicesFolder = tmpFolder.resolve(Paths.get(
                "META-INF",
                "services"));
        Files.createDirectories(servicesFolder);
        final Path serviceFile = servicesFolder.resolve(
                "dev.orne.i18n.context.I18nContextProviderStrategyConfigurer");
        try (final BufferedWriter out = Files.newBufferedWriter(serviceFile, StandardCharsets.UTF_8)) {
            out.write(I18nContextProviderStrategyConfigurerTest.TestConfigurer.class.getName());
            out.newLine();
        }
        return new URLClassLoader(
                new URL[] { this.tmpFolder.toUri().toURL() },
                Thread.currentThread().getContextClassLoader());
        
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration()
    throws IOException {
        final Properties expected = new Properties();
        expected.setProperty(I18nConfiguration.STRATEGY, I18nContextProviderStrategyConfigurerTest.TestStrategy.TYPE);
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(expected);
        final Properties result = I18nConfiguration.load(cl);
        assertEquals(expected, result);
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration_NotFound()
    throws Exception {
        final Properties result = I18nConfiguration.load(
                Thread.currentThread().getContextClassLoader());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test {@link I18nConfiguration#load(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration_IOException()
    throws Exception {
        final ClassLoader cl = mock(ClassLoader.class);
        final InputStream is = mock(InputStream.class);
        willReturn(is).given(cl).getResourceAsStream(I18nConfiguration.FILE);
        willThrow(IOException.class).given(is).read();
        willThrow(IOException.class).given(is).read(any());
        willThrow(IOException.class).given(is).read(any(), anyInt(), anyInt());
        final Properties result = I18nConfiguration.load(cl);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test {@link I18nContextProviderStrategy#getInstance()}.
     */
    @Test
    void testGetContextProviderStrategy()
    throws IOException, InterruptedException {
        final Properties expected = new Properties();
        expected.setProperty(I18nConfiguration.STRATEGY, I18nContextProviderStrategyConfigurerTest.TestStrategy.TYPE);
        final ClassLoader cl = createConfigClassLoader(expected);
        final GetContextProviderStrategyRunnable childTest = new GetContextProviderStrategyRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertInstanceOf(I18nContextProviderStrategyConfigurerTest.TestStrategy.class, childTest.result);
        assertSame(childTest.result, I18nContextProviderStrategy.getInstance());
    }

    /**
     * Test {@link I18N#reconfigure()}.
     */
    @Test
    void testReconfigure()
    throws IOException, InterruptedException {
        final I18nContextProviderStrategy defaultStrategy = I18nContextProviderStrategy.getInstance();
        assertTrue(defaultStrategy instanceof DefaultI18nContextProviderStrategy);
        final Properties expected = new Properties();
        expected.setProperty(I18nConfiguration.STRATEGY, I18nContextProviderStrategyConfigurerTest.TestStrategy.TYPE);
        final ClassLoader cl = createConfigClassLoader(expected);
        final GetContextProviderStrategyRunnable childTest = new GetContextProviderStrategyRunnable();
        Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertSame(defaultStrategy, childTest.result);
        I18nContextProviderStrategy.setInstance(null);
        child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertInstanceOf(I18nContextProviderStrategyConfigurerTest.TestStrategy.class, childTest.result);
        assertSame(childTest.result, I18nContextProviderStrategy.getInstance());
        I18nContextProviderStrategy.setInstance(null);
        child = new Thread(childTest);
        child.start();
        child.join();
        assertTrue(defaultStrategy instanceof DefaultI18nContextProviderStrategy);
    }

    private static class GetContextProviderStrategyRunnable
    implements Runnable {
        private I18nContextProviderStrategy result;
        @Override
        public void run() {
            this.result = I18nContextProviderStrategy.getInstance();
        }
    }
}
