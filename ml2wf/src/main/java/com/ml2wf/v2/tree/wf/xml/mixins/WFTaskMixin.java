package com.ml2wf.v2.tree.wf.xml.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.wf.WorkflowTask;

@JsonIgnoreProperties({"observers"})
public interface WFTaskMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlProperty(isAttribute = true)
    String getName();

    @JacksonXmlProperty(localName = "bpmn2:documentation")
    WorkflowTask.Documentation getDocumentation();
}
