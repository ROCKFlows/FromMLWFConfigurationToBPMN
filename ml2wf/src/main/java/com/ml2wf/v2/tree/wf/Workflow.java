package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
public class Workflow extends AbstractTree<Process> implements IInstantiable, IObserver<AbstractTreeEvent<Process>> {

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
        this.children.addAll(processes);
        this.internalMemory = new WorkflowInternalMemory();
        this.children.forEach(p -> p.subscribe(this));
    }

    /**
     * This inner class is the {@link Workflow}'s internal memory.
     *
     * <p>
     *
     * This memory allows avoiding time-consuming tree search by keeping update a {@link Map} containing
     * all useful information for manipulating a {@link Workflow}.
     *
     * <p>
     *
     * It observes the current {@link Workflow} implementation to keep its
     * structure memory consistent.
     *
     * @see Workflow
     * @see IObserver
     *
     * @since 1.1.0
     */
    protected final class WorkflowInternalMemory extends AbstractInternalMemory {

        @Override
        public void update(@NonNull final AbstractTreeEvent<Process> event) {
            log.debug("New Workflow event [{}].", event);
            var process = event.getNode();
            switch (event.getEventType()) {
                case ADDITION:
                    List<Process> location = ((AdditionEvent<Process>) event).getLocation();
                    memory.put(process.getName(), new Pair<>(process, location));
                    process.subscribe(this);
                    break;
                case REMOVAL:
                    memory.remove(process.getName());
                    process.unsubscribe(this);
                    break;
                case RENAMING:
                    String oldName = ((RenamingEvent<Process>) event).getOldName();
                    memory.put(process.getName(), memory.remove(oldName));
                    break;
                case INSTANTIATION:
                    // TODO: should we do something else ?
                    log.trace("{} has been instantiated", process.getName());
                    return;
                default:
                    throw new IllegalStateException("Unsupported event for Workflow internal memory.");
            }
        }
    }

    @Override
    public Optional<Process> removeChild(@NonNull final Process child) {
        if (!internalMemory.containsKey(child.getName())) {
            return Optional.empty();
        }
        Pair<Process, List<Process>> childPair = internalMemory.get(child.getName());
        childPair.getValue().remove(child);
        notifyOnChange(new RemovalEvent<>(child));
        return Optional.of(child);
    }

    @Override
    public void normalize() {
        children.forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        children.forEach(IInstantiable::instantiate);
    }

    @Override
    public void update(@NonNull final AbstractTreeEvent<Process> event) {
        observers.forEach(o -> o.update(event));
    }
}
