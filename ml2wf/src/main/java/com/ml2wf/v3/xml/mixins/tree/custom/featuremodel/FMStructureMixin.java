package com.ml2wf.v3.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModelTask;

import java.util.List;

@JsonIgnoreProperties({"observers", "internalMemory"})
public interface FMStructureMixin {

    @JacksonXmlProperty(localName = "and")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonAlias({ "and", "feature", "alt" })
    List<FeatureModelTask> getChildren();
}
