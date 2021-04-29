package com.ml2wf.v2.tree.wf.xml.mixins;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "bpmn2:definitions")
@JsonIgnoreProperties({"observers", "internalMemory"})
public interface WFMixin {
}
