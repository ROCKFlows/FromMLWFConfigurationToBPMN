package com.ml2wf.v3.xml.mixins.workflow.custom.bpmn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public interface BPMNProcessSequenceFlowMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlProperty(isAttribute = true)
    String getSourceRef();

    @JacksonXmlProperty(isAttribute = true)
    String getTargetRef();
}
