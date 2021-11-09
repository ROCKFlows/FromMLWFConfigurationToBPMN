package com.ml2wf.v2.xml.wf.mixins;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public interface ProcessSequenceFlowMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlProperty(isAttribute = true)
    String getSourceRef();

    @JacksonXmlProperty(isAttribute = true)
    String getTargetRef();
}
