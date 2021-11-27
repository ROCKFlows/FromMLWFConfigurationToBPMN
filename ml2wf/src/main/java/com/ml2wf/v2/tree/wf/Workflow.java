package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.util.observer.IObserver;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.List;

/**
 * A {@link Workflow} is an {@link AbstractTree} extension containing a list of {@link Process}es.
 *
 * <p>
 *
 * Each {@link Process} contains the workflow's tasks and links.
 *
 * @see AbstractTree
 * @see Process
 *
 * @since 1.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class Workflow extends AbstractTree<Process, String> implements IInstantiable,
        IObserver<AbstractTreeEvent<Process>> {

    /**
     * {@code Workflow}'s constructor with a list of {@link Process}es.
     *
     * <p>
     *
     * <b>Note</b> that this constructor is also a {@link JsonCreator}.
     *
     * @param processes the processes
     *
     * @see Process
     * @see JsonCreator
     */
    @JsonCreator
    public Workflow(@NonNull
                    @JacksonXmlProperty(localName="bpmn2:process")
                    @JacksonXmlElementWrapper(useWrapping = false) List<Process> processes) {
        getChildren().addAll(processes);
        for (Process process : processes) {
            if (appendChild(process).isLeft()) {
                log.error("Can't add task {} for workflow.", process.getIdentity());
            }
        }
        // TODO: getChildren().forEach(p -> p.subscribe(this));
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *
     * This method does not allow adding duplicated process in a workflow.
     *
     * @param child the child
     *
     * @return {@link Either#left(Object)} if any children has the same id or name as the given child
     */
    @Override
    public Either<String, Process> appendChild(Process child) {
        if (hasChildWithIdentity(child.getIdentity())) {
            return Either.left("Can't add duplicated process in a workflow.");
        }
        return super.appendChild(child);
    }

    @Override
    public void normalize() {
        getChildren().forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        getChildren().forEach(IInstantiable::instantiate);
    }

    @Override
    public void update(@NonNull final AbstractTreeEvent<Process> event) {
        observers.forEach(o -> o.update(event));
    }
}
