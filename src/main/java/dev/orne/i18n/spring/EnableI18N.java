package dev.orne.i18n.spring;

/*-
 * #%L
 * Orne I18N
 * %%
 * Copyright (C) 2021 - 2024 Orne Developments
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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import dev.orne.i18n.context.I18nConfiguration;

/**
 * Annotation for I18N context provider configuration on Spring contexts Java
 * configurations.
 * <p>
 * To be used together with @{@link Configuration Configuration} classes as
 * follows, configuring current thread context class loader's I18N context
 * provider using the provided I18N configuration file, if any
 * (see {@link I18nConfiguration}):
 *
 * <pre class="code">
 * {@literal @}Configuration
 * {@literal @}EnableI18N
 * public class AppConfig {
 *
 * }</pre>
 * 
 * <p>
 * To provide a custom configuration (ignoring the any configuration file if
 * desired) implement {@code I18nSpringConfigurer} in the
 * {@literal @}{@link Configuration} class:
 * 
 * <pre class="code">
 * {@literal @}Configuration
 * {@literal @}EnableI18N
 * public class AppConfig implements I18nSpringConfigurer {
 *
 *     {@literal @}Override
 *     public void configureI18nContextProvider(
 *             {@literal @}NotNull I18nSpringContextProvider.Builder builder,
 *             {@literal @}NotNull ApplicationContext context) {
 *         // Custom provider configuration
 *     }
 *
 * }</pre>
 * 
 * <p>
 * This allows using custom {@code I18nSpringContextProvider} extensions:
 * 
 * <pre class="code">
 * {@literal @}Configuration
 * {@literal @}EnableI18N
 * public class AppConfig implements I18nSpringConfigurer {
 *
 *     {@literal @}Override
 *     public {@literal @}NotNull I18nSpringContextProvider.Builder getI18nContextProviderBuilder() {
 *         return MyCustomSpringContextProvider.builder();
 *     }
 *
 *     {@literal @}Override
 *     public void configureI18nContextProvider(
 *             {@literal @}NotNull I18nSpringContextProvider.Builder builder,
 *             {@literal @}NotNull ApplicationContext context) {
 *         MyCustomSpringContextProvider.Builder myBuilder = (MyCustomSpringContextProvider.Builder) builder;
 *         // Custom provider configuration
 *     }
 *
 * }</pre>
 * 
 * <p>
 * By default current current thread context class loader's I18N context
 * provider is configured.
 * In J2EE contexts loading Spring context through
 * {@code ContextLoaderListener} this means the WAR's class loader.
 * To configure an EAR class loader's I18N context provider (for example
 * in shared parent contexts loaded through
 * {@code ContextLoaderListener.loadParentContext(ServletContext)}),
 * set a class in the EAR's libraries (the {@literal @}{@link Configuration}
 * class, for example):
 * 
 * <pre class="code">
 * {@literal @}Configuration
 * {@literal @}EnableI18N(classLoader=SharedAppConfig.class)
 * public class SharedAppConfig {
 *
 * }</pre>
 * 
 * <p>
 * This allows separate I18N configurations for EAR level thread like
 * asynchronous task executors or JMS listeners threads and WAR and EJB modules
 * if desired.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2021-01
 * @see I18nSpringConfiguration
 * @since 0.1
 */
@API(status=Status.STABLE, since="0.1")
@Documented
@Retention(RUNTIME)
@Target({ TYPE, ANNOTATION_TYPE })
@Import(I18nSpringConfiguration.class)
public @interface EnableI18N {

    /**
     * Returns the class whom {@code ClassLoader} to apply the configuration to.
     * <p>
     * Default value {@code Void.class} configures the current thread context
     * class loader's I18N context provider.
     * <p>
     * See {@link EnableI18N} javadoc for additional examples.
     * 
     * @return The class whom {@code ClassLoader} to apply the configuration to.
     */
    Class<?> classLoader() default Void.class;
}
