package dev.orne.i18n.context;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2023 Orne Developments
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
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.i18n.I18nConfigurationException;

/**
 * Unit tests for {@code I18nContextProviderStrategyConfigurer}.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2023-12
 * @since 0.1
 * @see I18nContextProviderStrategyConfigurer
 */
@Tag("ut")
class I18nContextProviderStrategyConfigurerTest {

    private Path tmpFolder;

    @BeforeAll
    public static void resetPreviousConfiguration() {
        I18nContextProviderStrategy.setInstance(null);
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
        I18nContextProviderStrategy.setInstance(null);
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
        final Path serviceFile = servicesFolder.resolve(
                "dev.orne.i18n.context.I18nContextProviderStrategyConfigurer");
        try (final BufferedWriter out = Files.newBufferedWriter(serviceFile, StandardCharsets.UTF_8)) {
            out.write(TestConfigurer.class.getName());
            out.newLine();
        }
        return new URLClassLoader(
                new URL[] { this.tmpFolder.toUri().toURL() },
                Thread.currentThread().getContextClassLoader());
        
    }

    /**
     * Test {@link I18nContextProviderStrategyConfigurer#configure()}.
     */
    @Test
    void testConfigure()
    throws IOException, InterruptedException {
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.STRATEGY, TestStrategy.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigureTestRunnable childTest = new ConfigureTestRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        final TestStrategy strategy = assertInstanceOf(TestStrategy.class, childTest.result);
        assertEquals(config, strategy.config); 
    }

    /**
     * Test {@link I18nContextProviderStrategyConfigurer#configure(Properties)}.
     */
    @Test
    void testConfigure_Properties()
    throws IOException, InterruptedException {
        createTestClassLoader(null);
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.STRATEGY, TestStrategy.TYPE);
        final ClassLoader cl = createTestClassLoader(config);
        final ConfigurePropertiesTestRunnable childTest = new ConfigurePropertiesTestRunnable(config);
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        final TestStrategy strategy = assertInstanceOf(TestStrategy.class, childTest.result);
        assertEquals(config, strategy.config); 
    }

    /**
     * Test {@link I18nContextProviderStrategyConfigurer#configure(Properties)}.
     */
    @Test
    void testConfigure_Missing()
    throws IOException {
        final Properties config = new Properties();
        config.setProperty(I18nConfiguration.STRATEGY, "MISSING");
        assertThrows(I18nConfigurationException.class, () -> {
            I18nContextProviderStrategyConfigurer.configure(config);
        });
    }

    public static class TestStrategy
    implements I18nContextProviderStrategy {

        /** The I18N context provider strategy type. */
        public static final String TYPE = "TEST";

        private final Properties config;

        public TestStrategy(Properties config) {
            super();
            this.config = config;
        }

        @Override
        public I18nContextProvider getDefaultContextProvider() {
            return null;
        }

        @Override
        public void setDefaultContextProvider(@NotNull I18nContextProvider provider) {
            // NOP
        }

        @Override
        public @NotNull I18nContextProvider getContextProvider() {
            return null;
        }

        @Override
        public void invalidate() {
            // NOP
        }
    }

    public static class TestConfigurer
    implements I18nContextProviderStrategyConfigurer {

        @Override
        public @NotNull String getType() {
            return TestStrategy.TYPE;
        }

        @Override
        public @NotNull I18nContextProviderStrategy create(
                @NotNull Properties config) {
            return new TestStrategy(config);
        }
    }

    private static class ConfigureTestRunnable
    implements Runnable {
        private I18nContextProviderStrategy result;
        @Override
        public void run() {
            this.result = I18nContextProviderStrategyConfigurer.configure();
        }
    }

    private static class ConfigurePropertiesTestRunnable
    implements Runnable {
        private final Properties config;
        private I18nContextProviderStrategy result;
        public ConfigurePropertiesTestRunnable(Properties config) {
            super();
            this.config = config;
        }
        @Override
        public void run() {
            this.result = I18nContextProviderStrategyConfigurer.configure(config);
        }
    }
}
