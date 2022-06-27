package com.ml2wf.v3.app.xml.mixins.configurations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v3.app.configurations.ConfigurationFeature;

public interface ConfigurationFeatureMixin {

    @JacksonXmlProperty(isAttribute = true)
    ConfigurationFeature.Status getAutomatic();
    @JacksonXmlProperty(isAttribute = true)
    ConfigurationFeature.Status getManual();
    @JacksonXmlProperty(isAttribute = true)
    String getName();
}
