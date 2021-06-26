module dev.orne.i18n {
    exports dev.orne.i18n;
    exports dev.orne.i18n.spring;
    requires org.slf4j;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires static java.desktop;
    requires static transitive java.validation;
    requires static transitive jakarta.validation;
    requires static jaxb.api;
    requires static java.xml.bind;
    requires static transitive com.fasterxml.jackson.databind;
    requires static spring.beans;
    requires static spring.core;
    requires static transitive spring.context;
    requires static org.apiguardian.api;
}
