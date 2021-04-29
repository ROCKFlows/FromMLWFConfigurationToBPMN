package com.ml2wf.v2.tree.fm.xml.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.fm.FeatureModelStructure;

@JacksonXmlRootElement(localName = "extendedFeatureModel") // TODO: differentiate extended
public interface FMMixin {

    @JacksonXmlProperty(localName = "struct")
    FeatureModelStructure getStructure();
}
