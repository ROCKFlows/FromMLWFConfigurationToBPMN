package com.ml2wf.xml.mixins.workflow.custom.bpmn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.core.workflow.custom.bpmn.BPMNProcess;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflowTask;

import java.util.List;

@JacksonXmlRootElement(localName = "bpmn2:process")
public interface BPMNProcessMixin {

    @JacksonXmlProperty(localName = "bpmn2:task")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<BPMNWorkflowTask> getTasks();

    @JacksonXmlProperty(localName = "bpmn2:sequenceFlow")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<BPMNProcess.SequenceFlow> getSequenceFlows();
}
