package com.ml2wf.v2.tree.wf.xml.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.wf.Process;

import java.util.List;

@JacksonXmlRootElement(localName = "bpmn2:definitions")
@JsonIgnoreProperties({"observers", "internalMemory"})
public interface WFMixin {

    @JacksonXmlProperty(localName = "bpmn2:process")
    @JacksonXmlElementWrapper(useWrapping = false)
    List<Process> getChildren();
}
