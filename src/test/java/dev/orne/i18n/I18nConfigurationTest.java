package dev.orne.i18n;

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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.validation.constraints.NotNull;

/**
 * Unit tests for {@code I18N} runtime configuration.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @since 0.1
 * @see I18N
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
        I18N.reconfigure();
    }

    private ClassLoader createConfigClassLoader(
            final @NotNull Properties config)
    throws IOException {
        this.tmpFolder = Files.createTempDirectory("testTmp");
        this.cfgFile = tmpFolder.resolve(I18N.CONFIG_FILE);
        try (final OutputStream out = Files.newOutputStream(cfgFile)) {
            config.store(out, null);
        }
        return new URLClassLoader(
                new URL[] { this.tmpFolder.toUri().toURL() },
                Thread.currentThread().getContextClassLoader());
        
    }

    /**
     * Test {@link I18N#loadConfiguration(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration()
    throws IOException {
        final Properties expected = new Properties();
        expected.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        expected.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(expected);
        final Properties result = I18N.loadConfiguration(cl);
        assertEquals(expected, result);
    }

    /**
     * Test {@link I18N#loadConfiguration(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration_NotFound()
    throws Exception {
        final Properties result = I18N.loadConfiguration(Thread.currentThread().getContextClassLoader());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test {@link I18N#loadConfiguration(ClassLoader)}.
     */
    @Test
    void testLoadConfiguration_IOException()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        config.setProperty("mock.prop", "mock.value");
        final ClassLoader cl = createConfigClassLoader(config);
        final FileChannel channel = FileChannel.open(this.cfgFile, StandardOpenOption.WRITE);
        channel.lock();
        try {
            final Properties result = I18N.loadConfiguration(cl);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } finally {
            channel.close();
        }
    }

    /**
     * Test {@link I18N#getCustomStrategyClass(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategyClass()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final Class<? extends I18nContextProviderStrategy> result = I18N.getCustomStrategyClass(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertSame(CustomI18nContextProviderStrategy.class, result);
    }

    /**
     * Test {@link I18N#getCustomStrategyClass(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategyClass_NotFound()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, "dev.orne.i18n.I18nConfigurationTest.MissingClass");
        final Class<? extends I18nContextProviderStrategy> result = I18N.getCustomStrategyClass(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertNull(result);
    }

    /**
     * Test {@link I18N#getCustomStrategyClass(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategyClass_NotValid()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, I18nConfigurationTest.class.getName());
        final Class<? extends I18nContextProviderStrategy> result = I18N.getCustomStrategyClass(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertNull(result);
    }

    /**
     * Test {@link I18N#getCustomStrategy(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategy()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final I18nContextProviderStrategy result = I18N.getCustomStrategy(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertTrue(result instanceof CustomI18nContextProviderStrategy);
    }

    /**
     * Test {@link I18N#getCustomStrategy(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategy_PropertiesConstructor()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomConfigurableI18nContextProviderStrategy.class.getName());
        final I18nContextProviderStrategy result = I18N.getCustomStrategy(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertTrue(result instanceof CustomConfigurableI18nContextProviderStrategy);
        assertSame(config, ((CustomConfigurableI18nContextProviderStrategy) result).config);
    }

    /**
     * Test {@link I18N#getCustomStrategy(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategy_NonAccesibleConstructor()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomNonAccesibleI18nContextProviderStrategy.class.getName());
        final I18nContextProviderStrategy result = I18N.getCustomStrategy(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertNull(result);
    }

    /**
     * Test {@link I18N#getCustomStrategy(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategy_ConstructorError()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomFlawedI18nContextProviderStrategy.class.getName());
        final I18nContextProviderStrategy result = I18N.getCustomStrategy(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertNull(result);
    }

    /**
     * Test {@link I18N#getCustomStrategy(ClassLoader, Properties)}.
     */
    @Test
    void testGetCustomStrategy_PropertiesConstructorError()
    throws Exception {
        final Properties config = new Properties();
        config.setProperty(I18N.STRATEGY_PROP, CustomFlawedConfigurableI18nContextProviderStrategy.class.getName());
        final I18nContextProviderStrategy result = I18N.getCustomStrategy(
                Thread.currentThread().getContextClassLoader(),
                config);
        assertNull(result);
    }

    /**
     * Test {@link I18N#createStrategy(ClassLoader)}.
     */
    @Test
    void testCreateStrategy_ClassLoader()
    throws IOException {
        final Properties expected = new Properties();
        expected.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final ClassLoader cl = createConfigClassLoader(expected);
        final I18nContextProviderStrategy result = I18N.createStrategy(cl);
        assertTrue(result instanceof CustomI18nContextProviderStrategy);
    }

    /**
     * Test {@link I18N#createStrategy()}.
     */
    @Test
    void testCreateStrategy()
    throws IOException, InterruptedException {
        final Properties expected = new Properties();
        expected.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final ClassLoader cl = createConfigClassLoader(expected);
        final CreateStrategyTestRunnable childTest = new CreateStrategyTestRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertTrue(childTest.result instanceof CustomI18nContextProviderStrategy);
    }

    /**
     * Test {@link I18N#getContextProviderStrategy()}.
     */
    @Test
    void testGetContextProviderStrategy()
    throws IOException, InterruptedException {
        final Properties expected = new Properties();
        expected.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final ClassLoader cl = createConfigClassLoader(expected);
        final GetContextProviderStrategyRunnable childTest = new GetContextProviderStrategyRunnable();
        final Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertTrue(childTest.result instanceof CustomI18nContextProviderStrategy);
        assertSame(childTest.result, I18N.getContextProviderStrategy());
    }

    /**
     * Test {@link I18N#reconfigure()}.
     */
    @Test
    void testReconfigure()
    throws IOException, InterruptedException {
        final I18nContextProviderStrategy defaultStrategy = I18N.getContextProviderStrategy();
        assertTrue(defaultStrategy instanceof DefaultI18nContextProviderStrategy);
        final Properties expected = new Properties();
        expected.setProperty(I18N.STRATEGY_PROP, CustomI18nContextProviderStrategy.class.getName());
        final ClassLoader cl = createConfigClassLoader(expected);
        final GetContextProviderStrategyRunnable childTest = new GetContextProviderStrategyRunnable();
        Thread child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertSame(defaultStrategy, childTest.result);
        I18N.reconfigure();
        child = new Thread(childTest);
        child.setContextClassLoader(cl);
        child.start();
        child.join();
        assertTrue(childTest.result instanceof CustomI18nContextProviderStrategy);
        assertSame(childTest.result, I18N.getContextProviderStrategy());
        I18N.reconfigure();
        child = new Thread(childTest);
        child.start();
        child.join();
        assertTrue(defaultStrategy instanceof DefaultI18nContextProviderStrategy);
    }

    public static class CustomI18nContextProviderStrategy
    implements I18nContextProviderStrategy {

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

    public static class CustomConfigurableI18nContextProviderStrategy
    implements I18nContextProviderStrategy {

        private final Properties config;

        public CustomConfigurableI18nContextProviderStrategy(Properties config) {
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

    public static class CustomNonAccesibleI18nContextProviderStrategy
    implements I18nContextProviderStrategy {

        private CustomNonAccesibleI18nContextProviderStrategy() {
            super();
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

    public static class CustomFlawedI18nContextProviderStrategy
    implements I18nContextProviderStrategy {

        private CustomFlawedI18nContextProviderStrategy() {
            super();
            throw new RuntimeException();
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

    public static class CustomFlawedConfigurableI18nContextProviderStrategy
    implements I18nContextProviderStrategy {

        public CustomFlawedConfigurableI18nContextProviderStrategy(
                Properties config) {
            super();
            throw new RuntimeException();
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

    private static class CreateStrategyTestRunnable
    implements Runnable {
        private I18nContextProviderStrategy result;
        @Override
        public void run() {
            this.result = I18N.createStrategy();
        }
    }

    private static class GetContextProviderStrategyRunnable
    implements Runnable {
        private I18nContextProviderStrategy result;
        @Override
        public void run() {
            this.result = I18N.getContextProviderStrategy();
        }
    }
}
