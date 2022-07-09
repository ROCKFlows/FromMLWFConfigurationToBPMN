package com.ml2wf.xml.mixins.workflow.custom.bpmn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public interface BPMNWFDocumentationMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlText
    String getContent();
}
