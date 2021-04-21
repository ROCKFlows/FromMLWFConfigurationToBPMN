package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.v2.tree.ITreeManipulable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * A process contains some {@link WorkflowTask}s and their associated {@link SequenceFlow}s.
 *
 * <p>
 *
 * It is identified by an {@link #id} and has a {@link #name}.
 *
 * <p>
 *
 * It is an implementation of the {@link ITreeManipulable} which means that it can add
 * and remove some {@link WorkflowTask}.
 *
 * <p>
 *
 * It also can instantiated itself by instantiating its {@link #tasks} implementing the
 * {@link IInstantiable} interface.
 *
 * @see WorkflowTask
 * @see SequenceFlow
 * @see ITreeManipulable
 * @see IInstantiable
 *
 * @since 1.1
 */
@JacksonXmlRootElement(localName = "bpmn2:process")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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

    /**
     * A {@link SequenceFlow} links two tasks (a source and a target) by referencing
     * their {@link WorkflowTask#getId()}.
     *
     * @see WorkflowTask
     *
     * @since 1.1
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
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
        if (!tasks.remove(child)) {
            return null;
        }
        sequenceFlows.removeIf(s -> s.getSourceRef().equals(child.getId()) || s.getTargetRef().equals(child.getId()));
        return child;
    }

    @Override
    public void instantiate() {
        tasks.forEach(IInstantiable::instantiate);
    }
}
