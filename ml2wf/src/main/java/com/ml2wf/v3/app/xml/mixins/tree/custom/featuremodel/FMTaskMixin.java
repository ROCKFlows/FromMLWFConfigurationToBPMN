package com.ml2wf.v3.app.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v3.app.xml.mixins.tree.custom.featuremodel.serializers.FeatureModelTaskSerializer;
import com.ml2wf.v3.app.tree.custom.featuremodel.FeatureModelTask;

import java.util.List;

@JsonSerialize(using = FeatureModelTaskSerializer.class)
@JsonIgnoreProperties({"parent", "internalMemory"})
public interface FMTaskMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getName();

    @JacksonXmlProperty(localName = "abstract", isAttribute = true)
    boolean isAbstract();

    @JacksonXmlProperty(localName = "mandatory", isAttribute = true)
    boolean isMandatory();

    @JacksonXmlProperty(localName = "version", isAttribute = true)
    boolean getVersion();

    @JacksonXmlProperty(localName = "description")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<FeatureModelTask.Description> getDescriptions();

    @JacksonXmlProperty(localName = "and")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonAlias({ "and", "feature", "alt" })
    List<FeatureModelTask> getChildren();
}
