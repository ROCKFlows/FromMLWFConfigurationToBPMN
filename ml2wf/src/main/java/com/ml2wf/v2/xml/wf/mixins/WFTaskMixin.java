package com.ml2wf.v2.xml.wf.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.wf.WorkflowTask;

@JsonIgnoreProperties({"ID_COUNTER", "observers", "identity"})
public interface WFTaskMixin {

    @JacksonXmlProperty(isAttribute = true)
    String getId();

    @JacksonXmlProperty(isAttribute = true)
    String getName();

    @JacksonXmlProperty(localName = "bpmn2:documentation")
    @JacksonXmlElementWrapper(useWrapping = false)
    WorkflowTask.Documentation getDocumentation();

    @JsonIgnore
    boolean isAbstract();

    @JsonIgnore
    boolean isOptional();
}