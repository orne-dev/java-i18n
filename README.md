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
    - [ ] Apache Commons BeanUtils converters
    - [x] Support for [random generation for tests][orne generators site]
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

## Further information

For further information refer to the [Javadoc][javadoc], [project Wiki][wiki]
and [Maven Site][site].

[javadoc]: https://javadoc.io/doc/dev.orne/i18n
[wiki]: https://github.com/orne-dev/java-i18n/wiki
[site]: https://orne-dev.github.io/java-i18n/
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
