package com.ml2wf.v3.app.xml.mixins.workflow.custom.bpmn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNWorkflowTask;

public interface BPMNWFTaskMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlProperty(isAttribute = true)
    String getName();

    @JacksonXmlProperty(localName = "bpmn2:documentation")
    @JacksonXmlElementWrapper(useWrapping = false)
    BPMNWorkflowTask.Documentation getDocumentation();
}
