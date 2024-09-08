# :package: 0.1.0

01. :wrench: Added JPMS default module name `dev.orne.i18n`.
01. :wrench: Added `org.apache.commons:commons-lang3:3.12.0` dependency.
01. :wrench: Added `org.apache.commons:commons-text:1.9` dependency.
01. :gift: Added `dev.orne.i18n.I18nConfigurationException`
01. :gift: Added `dev.orne.i18n.I18nResources`
01. :gift: Added `dev.orne.i18n.I18N`
01. :gift: Added `dev.orne.i18n.I18nString`
    01. Added `dev.orne.i18n.I18nFixedString`
    01. Added `dev.orne.i18n.I18nResourcesString`
    01. Added `dev.orne.i18n.I18nStringMap`
01. :gift: Added `dev.orne.i18n.I18nStringEditor`
    01. Added `dev.orne.i18n.I18nStringMapEditor`
01. :gift: Added `dev.orne.i18n.I18nStringConverter`
01. :gift: Added I18N contexts system.
    01. Added `dev.orne.i18n.context.I18nContext`
    01. Added `dev.orne.i18n.context.DefaultI18nContext`
    01. Added `dev.orne.i18n.context.DummyI18nResources`
    01. Added `dev.orne.i18n.context.I18nBundleResources`
    01. Added `dev.orne.i18n.context.I18nConfiguration`
    01. Added `dev.orne.i18n.context.I18nContextProvider`
        01. Added `dev.orne.i18n.context.AbstractI18nContextProvider`
        01. Added `dev.orne.i18n.context.DefaultI18nContextProvider`
        01. Added `dev.orne.i18n.context.SharedI18nContextProvider`
    01. Added `dev.orne.i18n.context.I18nContextProviderFactory`
01. :gift: Added Spring framework integration support
    01. Added `dev.orne.i18n.spring.I18nSpringContext`
    01. Added `dev.orne.i18n.spring.I18nSpringResources`
    01. Added `dev.orne.i18n.spring.I18nSpringContextProvider`
    01. Added `dev.orne.i18n.spring.I18nSpringBaseConfiguration`
    01. Added `dev.orne.i18n.spring.I18nSpringWebContextClearer`
    01. Added `dev.orne.i18n.spring.I18nSpringWebConfiguration`
    01. Added `dev.orne.i18n.spring.I18nSpringConfiguration`
    01. Added `dev.orne.i18n.spring.EnableI18N`
01. :gift: Added Java Bean Validation 2.0 support
    01. Added `dev.orne.i18n.validation.AbstractValidatorForI18nString`
    01. Added `dev.orne.i18n.validation.NotEmptyValidatorForI18nString`
    01. Added `dev.orne.i18n.validation.NotBlankValidatorForI18nString`
    01. Added `dev.orne.i18n.validation.SizeValidatorForI18nString`
    01. Added `dev.orne.i18n.validation.PatternValidatorForI18nString`
01. :gift: Added Jackson 2.11 support
    01. Added `dev.orne.i18n.I18nStringJacksonSerializer`
    01. Added `dev.orne.i18n.I18nStringJacksonDeserializer`
01. :gift: Added `dev.orne.i18n.I18nXmlSchema`
01. :gift: Added Java XML Binding (JAXB) 2.2.8 support
    01. Added `dev.orne.i18n.jaxb.XmlI18nString`
    01. Added `dev.orne.i18n.jaxb.XmlI18nStringTranslation`
    01. Added `dev.orne.i18n.jaxb.ObjectFactory`
    01. Added `dev.orne.i18n.jaxb.JaxbUtils`
    01. Added `dev.orne.i18n.jaxb.I18nStringAdapter`
    01. Added `dev.orne.i18n.jaxb.I18nStringAdapter.Full`
    01. Added `dev.orne.i18n.jaxb.I18nFixedStringAdapter`
    01. Added `dev.orne.i18n.jaxb.I18nResourcesStringAdapter`
    01. Added `dev.orne.i18n.jaxb.I18nResourcesStringAdapter.Full`
    01. Added `dev.orne.i18n.jaxb.I18nStringAdapter`
    01. Added `dev.orne.i18n.jaxb.I18nStringAdapter.Full`
01. :gift: Added Orne Test Generators 0.2.0 support
    01. :gift: Added `dev.orne.i18n.I18nStringGenerator`
    01. :gift: Added `dev.orne.i18n.I18nFixedStringGenerator`
    01. :gift: Added `dev.orne.i18n.I18nStringMapGenerator`
    01. :gift: Added `dev.orne.i18n.I18nResourcesStringGenerator`
