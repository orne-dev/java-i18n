# Orne Java I18N utilities

Provides utilities for internationalization of Java applications.

## Status

[![License][status.license.badge]][status.license]
[![Latest version][status.maven.badge]][status.maven]
[![Javadoc][status.javadoc.badge]][javadoc]
[![Maven site][status.site.badge]][site]

| Latest Release | Develop |
| :------------: | :-------------: |
| [![Build Status][status.latest.ci.badge]][status.latest.ci] | [![Build Status][status.dev.ci.badge]][status.dev.ci] |
| [![Coverage][status.latest.cov.badge]][status.latest.cov] | [![Coverage][status.dev.cov.badge]][status.dev.cov] |

## Features

The library provides the following features (unchecked features are planned and
unimplemented):

- [x] I18N strings
    - [x] Jakarta EE validation support (javax)
    - [ ] Jakarta EE validation support (jakarta)
    - [x] Jakarta XML Bind (JAXB) support (javax)
    - [ ] Jakarta XML Bind (JAXB) support (jakarta)
    - [x] Jackson support
    - [ ] Apache Commons BeanUtils support
    - [ ] Spring MVC data binding support
    - [x] Testing [random generation][orne generators site] support
- [x] I18N resources
- [x] I18N context
    - [x] Spring integration
- [ ] JavaFX integration

## Usage

The binaries can be obtained from [Maven Central][status.maven] with the
`dev.orne:i18n` coordinates:

```xml
<dependency>
  <groupId>dev.orne</groupId>
  <artifactId>i18n</artifactId>
  <version>0.1.0</version>
</dependency>
```

### Internationalization context

The internationalization context represents the user locale and is
linked to the executing thread.
The locale of current context can be accessed through the methods of the
main `I18N` class:

```java
I18N.getLocale();
I18N.setLocale(newLocale);
```

### Internationalized resources

Internationalized resources can be obtained from the I18N context, that will
return message translations when available:

```java
I18nResources resources = I18N.getResources();
resources.getMessage("Error processing request", "app.errors.request");
resources.getMessage("Error processing request for folder {0}", "app.errors.request.folder", folderId);
```

Additional named I18N resources are supported:

```java
I18nResources resources = I18N.getResources("errors");
resources.getMessage("Error processing request", "app.errors.request");
resources.getMessage("Error processing request for folder {0}", "app.errors.request.folder", folderId);
```

### Internationalized strings

Internationalized strings provide an abstraction for texts that can have
translations based on user language:

```java
class MyBean {
    I18nString description;
}
MyBean bean = ...;
bean.getDesription().get();
bean.getDesription().get(altLocale);
```

Support for JAXB and Jackson based JSON de/serialization is provided out of the
box.

### Configuration

The default I18N configuration can be customized through a
`dev.orne.i18n.config.properties` file in the classpath.

```properties
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

See the [Maven Site configuration page][site config] for further
information.

#### Spring integration

Integration with Spring's `LocaleContextHolder` is provided out of the box:

```java
@EnableI18N
@Configuration
class MySpringConfiguration {
    ...
}
```

The base configuration links the locale of the Spring's `LocaleContextHolder`
with the `I18nContext` locale and application's default `MessageSource` as
default `I18nResources`.

For J2EE environments the library detects the presence of `spring-web` and
listens `RequestHandledEvent` events to clear the I18N context after
each HTTP request. 

See the [Maven Site Spring configuration page][site config spring] for
further information.

## Further information

For further information refer to the [Maven Site][site] and [Javadoc][javadoc].

[site]: https://orne-dev.github.io/java-i18n/
[site config]: https://orne-dev.github.io/java-i18n/configuration.html
[site config spring]: https://orne-dev.github.io/java-i18n/configuration.html#spring-configuration
[javadoc]: https://javadoc.io/doc/dev.orne/i18n
[status.license]: http://www.gnu.org/licenses/gpl-3.0.txt
[status.license.badge]: https://img.shields.io/github/license/orne-dev/java-i18n
[status.maven]: https://search.maven.org/artifact/dev.orne/i18n
[status.maven.badge]: https://img.shields.io/maven-central/v/dev.orne/i18n.svg?label=Maven%20Central
[status.javadoc.badge]: https://javadoc.io/badge2/dev.orne/i18n/javadoc.svg
[status.site.badge]: https://img.shields.io/website?url=https%3A%2F%2Forne-dev.github.io%2Fjava-i18n%2F
[status.latest.ci]: https://github.com/orne-dev/java-i18n/actions/workflows/release.yml
[status.latest.ci.badge]: https://github.com/orne-dev/java-i18n/actions/workflows/release.yml/badge.svg?branch=master
[status.latest.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-i18n
[status.latest.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-i18n&metric=coverage
[status.dev.ci]: https://github.com/orne-dev/java-i18n/actions/workflows/build.yml
[status.dev.ci.badge]: https://github.com/orne-dev/java-i18n/actions/workflows/build.yml/badge.svg?branch=develop
[status.dev.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-i18n&branch=develop
[status.dev.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-i18n&metric=coverage&branch=develop
[orne generators site]: https://orne-dev.github.io/java-generators/
