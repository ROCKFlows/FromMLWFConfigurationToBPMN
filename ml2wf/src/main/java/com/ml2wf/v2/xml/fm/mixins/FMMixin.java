package com.ml2wf.v2.xml.fm.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.fm.FeatureModelRule;
import com.ml2wf.v2.tree.fm.FeatureModelStructure;

import java.util.Set;

@JacksonXmlRootElement(localName = "extendedFeatureModel") // TODO: differentiate extended
@JsonIgnoreProperties({"children"})
public interface FMMixin {

    @JacksonXmlProperty(localName = "struct")
    FeatureModelStructure getStructure();

    @JacksonXmlProperty(localName = "rule")
    @JacksonXmlElementWrapper(localName = "constraints")
    Set<FeatureModelRule> getConstraints();
}
