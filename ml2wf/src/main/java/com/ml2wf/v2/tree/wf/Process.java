package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JacksonXmlRootElement(localName = "bpmn2:process")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Process implements ITreeManipulable<WorkflowTask>, IInstantiable {

    @JacksonXmlProperty(isAttribute = true)
    private String id;
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlProperty(localName="bpmn2:task")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<WorkflowTask> tasks;
    @JacksonXmlProperty(localName = "bpmn2:sequenceFlow")
    @JacksonXmlElementWrapper(useWrapping = false)
    @Setter(AccessLevel.PRIVATE)
    private List<SequenceFlow> sequenceFlows;

    @NoArgsConstructor
    @ToString
    @Getter
    @Setter
    static class SequenceFlow {

        @JacksonXmlProperty(isAttribute = true)
        private String id;
        @JacksonXmlProperty(isAttribute = true)
        private String sourceRef;
        @JacksonXmlProperty(isAttribute = true)
        private String targetRef;
    }

    @Override
    public WorkflowTask appendChild(WorkflowTask child) {
        tasks.add(child);
        return child;
    }

    @Override
    public WorkflowTask removeChild(WorkflowTask child) {
        return (tasks.remove(child)) ? child : null;
    }

    @Override
    public void instantiate() {
        tasks.forEach(IInstantiable::instantiate);
    }
}
