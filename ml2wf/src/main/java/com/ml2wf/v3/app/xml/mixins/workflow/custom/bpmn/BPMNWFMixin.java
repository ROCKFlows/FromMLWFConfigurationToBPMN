package com.ml2wf.v3.app.xml.mixins.workflow.custom.bpmn;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNProcess;

import java.util.List;

@JacksonXmlRootElement(localName = "bpmn2:definitions")
public interface BPMNWFMixin {

    @JacksonXmlProperty(localName = "bpmn2:process")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<BPMNProcess> getProcesses();
}
