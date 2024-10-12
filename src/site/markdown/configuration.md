# Configuration

When the library is used without any additional configuration a
configuration suitable for standalone applications is applied by default.

## File based configuration

As a preferred alternative, configuration file based method is provided: Add a
`dev.orne.i18n.config.properties` file in the class path resources
with the configuration options.

```
# If context should be thread exclusive or shared among threads. Allows SHARED, THREAD. Defaults to THREAD.
dev.orne.i18n.context.provider=THREAD
# If context is inherited by child threads in per-thread context providers. Defaults to true.
dev.orne.i18n.context.inherited=true
# The default application language. Defaults to Locale.getDefault().
dev.orne.i18n.language.default=en
# The languages supported by the application. Optional, not used internally. Defaults to Locale.getAvailableLocales().
dev.orne.i18n.language.available=bn,en,es,hi,zh
# The default I18N resources bundle. Defaults to "messages".
dev.orne.i18n.resources=messages
# Additional alternative named I18N resources bundles. Optional. In this example exposed as "errors".
dev.orne.i18n.resources.named.errors=error-messages
```

The configuration file takes effect for the class loader that provides it and
all child class loaders that don't provide a new configuration file.
This is specially usefull for J2EE evironments, where different WARS packed in
a single EAR can provide different configurations while other threads using the
EAR's class loader (executors threads, for example) use the shared EAR-level
configuration.

Multiple configuration files in a single class loader are not allowed and
throw an exception.

Support for custom context providers can be added registering implementations
of `dev.orne.i18n.context.I18nContextProviderFactory` through Java SPI.

## Programmatic configuration

To change the I18N configuration or context provider programmatically a class
implementing the interface `I18nContextProvider.Configurer` can call
`setI18nConfiguration()` to override the configuration or
`setI18nContextProvider()` to set a fully configured context provider instance:

```java
class I18nConfigurer implements I18nContextProvider.Configurer {

    void configureI18n() {
        // Set configuration (do not use 'dev.orne.i18n.config.properties' file)
        setI18nConfiguration(config);

        // and/or

        // Configure context provider
        I18nContextProvider provider = ...;
        // Register for this thread and child threads
        setI18nContextProvider(provider);
    }
}
```

## Spring configuration

In Spring based applications just add `EnableI18N` annotation to Spring
context configuration. This anotation will link de I18N context to Spring
`LocaleContextHolder`, use application's default `MessageSource` as default
I18N resources source and, if `spring-web` is in the classpath, register a
listener of Spring's `RequestHandledEvent` events to clear I18N context
between HTTP requests.

```java
@Configuration
@EnableI18N
class SpringConfig {
    // Application beans configuration
}
```

On applications with Spring contexts hierarchies property `classLoader` can
be set to provide a class of the class loader being configured. This allows
I18N configuration EAR wide,  

```java
@Configuration
@EnableI18N(classLoader = EarSpringConfig.class)
class EarSpringConfig {
    // Configures shared I18N provider for all WARs and EJBs
}

@Configuration
@EnableI18N
class War1SpringConfig {
    // Configures custom I18N configuration for this WAR
}

@Configuration
class War2SpringConfig {
    // Uses shared I18N provider
}
```

Additional configuration can be applied implementing the `I18nSpringConfigurer`
interface.
For example, to extract context configuation from application provided
properties:

```java
@Configuration
@EnableI18N
class SpringConfig implements I18nSpringConfigurer {

    @Bean
    public Properties applicationConfiguration() {
        ...
    }

    @Override
    public void configureI18nContextProvider(
            @NotNull I18nSpringContextProvider.Builder builder) {
        // Default implementation
        builder.configure(applicationConfiguration());
    }
}
```
