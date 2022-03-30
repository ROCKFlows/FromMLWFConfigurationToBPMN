package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.ITreeManipulable;
import com.ml2wf.v2.tree.Identifiable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
 * It also can instantiate itself by instantiating its {@link #getChildren()} implementing the
 * {@link Instantiable} interface.
 *
 * @see WorkflowTask
 * @see SequenceFlow
 * @see ITreeManipulable
 * @see Instantiable
 *
 * @since 1.1.0
 */
@Getter
@Setter // TODO: listen on setters
@EqualsAndHashCode(of = {"id", "name"}, callSuper = true)
@ToString(of = {"id", "name"})
@Log4j2
public class Process extends AbstractTree<WorkflowTask, String> implements Instantiable, Identifiable<String> {

    // TODO: create a normalizer

    private String id;
    private String name;
    private final List<SequenceFlow> sequenceFlows;

    // TODO: find why we have to specify localName for id and name

    /**
     * {@code Process}'s constructor with and id, a name, some {@link WorkflowTask}s and their
     * associated {@link SequenceFlow}s.
     *
     * <p>
     *
     * <b>Note</b> that this constructor is also a {@link JsonCreator}.
     *
     * @param id            the new process's id
     * @param name          the new process's name
     * @param tasks         the new process's tasks
     * @param sequenceFlows the new process's sequence flows
     *
     * @see WorkflowTask
     * @see SequenceFlow
     * @see JsonCreator
     */
    @JsonCreator
    public Process(@JacksonXmlProperty(localName = "id", isAttribute = true) String id,
                   @JacksonXmlProperty(localName = "name", isAttribute = true) String name,
                   @JacksonXmlProperty(localName="bpmn2:task")
                   @JacksonXmlElementWrapper(useWrapping = false) List<WorkflowTask> tasks,
                   @JacksonXmlProperty(localName = "bpmn2:sequenceFlow")
                   @JacksonXmlElementWrapper(useWrapping = false) List<SequenceFlow> sequenceFlows) {
        super(tasks);
        this.id = id;
        this.name = name;
        this.sequenceFlows = new ArrayList<>(sequenceFlows);
    }

    /**
     * A {@link SequenceFlow} links two tasks (a source and a target) by referencing
     * their {@link WorkflowTask#getId()}.
     *
     * @see WorkflowTask
     *
     * @since 1.1.0
     */
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    public static class SequenceFlow {

        private String id;
        private String sourceRef;
        private String targetRef;
    }

    @Override
    public @NonNull String getIdentity() {
        return name;
    }

    @Override
    public Optional<WorkflowTask> removeDirectChild(final WorkflowTask child) {
        Optional<WorkflowTask> optRemovedChild = super.removeDirectChild(child);
        if (optRemovedChild.isPresent()) {
            sequenceFlows.removeIf(s -> s.getSourceRef().equals(child.getId()) ||
                    s.getTargetRef().equals(child.getId())); // TODO: check id or name ?
        }
        return optRemovedChild;
    }

    @Override
    public void normalize() {
        String oldName = name;
        name = name.trim().replace(" ", "_");
        if (!name.equals(oldName)) {
            // TODO: notifyOnChange(new RenamingEvent<>(this, oldName));
        }
        getChildren().forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        getChildren().forEach(Instantiable::instantiate);
    }
}
