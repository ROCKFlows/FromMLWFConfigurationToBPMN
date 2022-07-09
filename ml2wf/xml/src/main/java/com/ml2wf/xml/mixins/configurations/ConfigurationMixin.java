package com.ml2wf.xml.mixins.configurations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.core.configurations.ConfigurationFeature;

import java.util.List;

@JacksonXmlRootElement(localName = "configuration")
public interface ConfigurationMixin {


    @JacksonXmlProperty(localName = "feature")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<ConfigurationFeature> getFeatures();
}
