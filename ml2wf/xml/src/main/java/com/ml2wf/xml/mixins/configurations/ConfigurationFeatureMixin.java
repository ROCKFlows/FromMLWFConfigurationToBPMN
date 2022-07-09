package com.ml2wf.xml.mixins.configurations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.core.configurations.ConfigurationFeature;

public interface ConfigurationFeatureMixin {

    @JacksonXmlProperty(isAttribute = true)
    ConfigurationFeature.Status getAutomatic();
    @JacksonXmlProperty(isAttribute = true)
    ConfigurationFeature.Status getManual();
    @JacksonXmlProperty(isAttribute = true)
    String getName();
}
