package com.ml2wf.v2.xml.fm.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import com.ml2wf.v2.xml.fm.serializers.FeatureModelTaskSerializer;

import java.util.List;

@JsonSerialize(using = FeatureModelTaskSerializer.class)
@JsonIgnoreProperties({"parent"})
public interface FMTaskMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getName();

    @JacksonXmlProperty(localName = "abstract", isAttribute = true)
    boolean isAbstract();

    @JacksonXmlProperty(localName = "mandatory", isAttribute = true)
    boolean isMandatory();

    @JacksonXmlProperty(localName = "description")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<FeatureModelTask.Description> getDescriptions();
}
