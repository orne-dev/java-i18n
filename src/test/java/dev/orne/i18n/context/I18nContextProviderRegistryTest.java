package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023-2024 Orne Developments
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Locale;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nConfigurationException;
import dev.orne.i18n.I18nResources;

/**
 * Unit tests for {@code I18nContextProvider.Registry}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-12
 * @since 0.1
 * @see I18nContextProvider.Registry
 */
@Tag("ut")
class I18nContextProviderRegistryTest {

    private Path tmpFolder;

    @BeforeAll
    public static void resetPreviousConfiguration() {
        ContextTestUtils.reset();
    }

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
        ContextTestUtils.reset();
    }

    private @NotNull ClassLoader createTestClassLoader(
            final Properties config)
    throws IOException {
        this.tmpFolder = Files.createTempDirectory("testTmp");
        if (config != null) {
            final Path cfgFile = tmpFolder.resolve(I18nConfiguration.FILE);
            try (final OutputStream out = Files.newOutputStream(cfgFile)) {
                config.store(out, null);
            }
        }
        final Path servicesFolder = tmpFolder.resolve(Paths.get(
                "META-INF",
                "services"));
        Files.createDirectories(servicesFolder);
        final Path serviceFile = servicesFolder.resolve(I18nContextProviderFactory.class.getName());
        try (final BufferedWriter out = Files.newBufferedWriter(serviceFile, StandardCharsets.UTF_8)) {
            out.write(TestConfigurer.class.getName());
            out.newLine();
        }
        return new URLClassLoader(
                new URL[] { this.tmpFolder.toUri().toURL() },
                Thread.currentThread().getContextClassLoader());
        
    }

    /**
     * Test {@link I18nContextProvider.Registry#set(I18nContextProvider)}.
     */
    @Test
    void testSet() {
        final I18nContextProvider provider = mock(I18nContextProvider.class);
        I18nContextProvider.Registry.set(provider);
        assertSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader()));
        assertNotSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
        ContextTestUtils.reset();
        I18nContextProvider.Registry.set(
                Thread.currentThread().getContextClassLoader(),
                provider);
        assertSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader()));
        assertNotSame(
                provider,
                I18nContextProvider.Registry.get(
                    Thread.currentThread().getContextClassLoader().getParent()));
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure()}.
     */
    @Test
    void testConfigure()
    throws IOException, InterruptedException {
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.CONTEXT_PROVIDER, TestProvider.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigureTestRunnable childTest = new ConfigureTestRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        final TestProvider strategy = assertInstanceOf(TestProvider.class, childTest.result);
        assertEquals(config, strategy.config); 
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure(Properties)}.
     */
    @Test
    void testConfigure_Properties()
    throws IOException, InterruptedException {
        createTestClassLoader(null);
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.CONTEXT_PROVIDER, TestProvider.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigurePropertiesTestRunnable childTest = new ConfigurePropertiesTestRunnable(config);
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        final TestProvider strategy = assertInstanceOf(TestProvider.class, childTest.result);
        assertEquals(config, strategy.config); 
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure(Properties)}.
     */
    @Test
    void testConfigure_NoType()
    throws IOException, InterruptedException {
        createTestClassLoader(null);
        final Properties config = new Properties();
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigurePropertiesTestRunnable childTest = new ConfigurePropertiesTestRunnable(config);
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertInstanceOf(DefaultI18nContextProvider.class, childTest.result);
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure(Properties)}.
     */
    @Test
    void testConfigure_Default()
    throws IOException, InterruptedException {
        createTestClassLoader(null);
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.CONTEXT_PROVIDER, DefaultI18nContextProvider.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigurePropertiesTestRunnable childTest = new ConfigurePropertiesTestRunnable(config);
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertInstanceOf(DefaultI18nContextProvider.class, childTest.result);
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure(Properties)}.
     */
    @Test
    void testConfigure_Shared()
    throws IOException, InterruptedException {
        createTestClassLoader(null);
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.CONTEXT_PROVIDER, SharedI18nContextProvider.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigurePropertiesTestRunnable childTest = new ConfigurePropertiesTestRunnable(config);
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertInstanceOf(SharedI18nContextProvider.class, childTest.result);
    }

    /**
     * Test {@link I18nContextProvider.Registry#configure(Properties)}.
     */
    @Test
    void testConfigure_Missing() {
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.CONTEXT_PROVIDER, "MISSING");
        assertThrows(I18nConfigurationException.class, () -> {
            I18nContextProvider.Registry.configure(config);
        });
    }

    public static class TestProvider
    implements I18nContextProvider {

        /** The I18N context provider strategy type. */
        public static final String TYPE = "TEST";

        private final Properties config;

        public TestProvider(Properties config) {
            super();
            this.config = config;
        }

        @Override
        public @NotNull Locale[] getAvailableLocales() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull I18nResources getDefaultI18nResources() {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull I18nResources getI18nResources(String key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @NotNull I18nContext getContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isContextValid(@NotNull I18nContext context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clearContext() {
            // NOP
        }

        @Override
        public void invalidate() {
            // NOP
        }
    }

    public static class TestConfigurer
    implements I18nContextProviderFactory {

        @Override
        public @NotNull String getType() {
            return TestProvider.TYPE;
        }

        @Override
        public @NotNull I18nContextProvider create(
                @NotNull Properties config) {
            return new TestProvider(config);
        }
    }

    private static class ConfigureTestRunnable
    implements Runnable {
        private I18nContextProvider result;
        @Override
        public void run() {
            this.result = I18nContextProvider.Registry.configure(
                    Thread.currentThread().getContextClassLoader());
        }
    }

    private static class ConfigurePropertiesTestRunnable
    implements Runnable {
        private final Properties config;
        private I18nContextProvider result;
        public ConfigurePropertiesTestRunnable(Properties config) {
            super();
            this.config = config;
        }
        @Override
        public void run() {
            this.result = I18nContextProvider.Registry.configure(config);
        }
    }
}
