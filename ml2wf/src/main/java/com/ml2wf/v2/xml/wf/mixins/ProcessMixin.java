package com.ml2wf.v2.xml.wf.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.WorkflowTask;

import java.util.List;

@JacksonXmlRootElement(localName = "bpmn2:process")
@JsonIgnoreProperties({"internalMemory", "identity"})
public interface ProcessMixin {

    @JacksonXmlProperty(localName = "bpmn2:task")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<WorkflowTask> getChildren();

    @JacksonXmlProperty(localName = "bpmn2:sequenceFlow")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Process.SequenceFlow> getSequenceFlows();
}
