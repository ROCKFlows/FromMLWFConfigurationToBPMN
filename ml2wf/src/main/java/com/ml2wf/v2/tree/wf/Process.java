package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.ITreeManipulable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
 * @since 1.1.0
 */
@Getter
@Setter // TODO: listen on setters
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
@Log4j2
public class Process implements ITreeManipulable<WorkflowTask>, IInstantiable,
        IObserver<AbstractTreeEvent<WorkflowTask>>, IObservable<AbstractTreeEvent<Process>> {

    // TODO: create a normalizer

    private String id;
    private String name;
    private final List<WorkflowTask> tasks;
    private final List<SequenceFlow> sequenceFlows;
    private final Set<IObserver<AbstractTreeEvent<WorkflowTask>>> tasksObservers;
    private final Set<IObserver<AbstractTreeEvent<Process>>> processesObservers;
    private final InternalMemory internalMemory;

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
        this.id = id;
        this.name = name;
        this.tasks = new ArrayList<>(tasks);
        this.sequenceFlows = new ArrayList<>(sequenceFlows);
        this.tasksObservers = new HashSet<>();
        this.processesObservers = new HashSet<>();
        this.internalMemory = new InternalMemory();
        this.tasks.forEach(p -> p.subscribe(this));
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

    /**
     * This inner class is the {@link Process}'s internal memory.
     *
     * <p>
     *
     * This memory allows avoiding time-consuming tree search by keeping update a {@link Map} containing
     * all useful information for manipulating a {@link Process}.
     *
     * <p>
     *
     * It observes the current {@link Process} implementation to keep its
     * structure memory consistent.
     *
     * @see Process
     * @see IObserver
     *
     * @since 1.1.0
     */
    private final class InternalMemory implements IObserver<AbstractTreeEvent<WorkflowTask>> {

        // TODO: merge with abstract tree

        @Delegate private final Map<String, Pair<WorkflowTask, List<WorkflowTask>>> memory;

        private InternalMemory() {
            memory = new HashMap<>();
            tasks.forEach(t -> t.subscribe(this));
        }

        @Override
        public void update(@NonNull final AbstractTreeEvent<WorkflowTask> event) {
            log.debug("New Process event [{}].", event);
            var workflowTask = event.getNode();
            switch (event.getEventType()) {
                case ADDITION:
                    List<WorkflowTask> location = ((AdditionEvent<WorkflowTask>) event).getLocation();
                    memory.put(workflowTask.getName(), new Pair<>(workflowTask, location));
                    workflowTask.subscribe(this);
                    break;
                case REMOVAL:
                    memory.remove(workflowTask.getName());
                    workflowTask.unsubscribe(this);
                    break;
                case RENAMING:
                    String oldName = ((RenamingEvent<WorkflowTask>) event).getOldName();
                    memory.put(workflowTask.getName(), memory.remove(oldName));
                    break;
                case INSTANTIATION:
                    // TODO: should we do something ?
                    break;
                default:
                    throw new IllegalStateException("Unsupported event for Workflow's process internal memory.");
            }
        }
    }

    @Override
    public boolean hasChildren() {
        return !tasks.isEmpty();
    }

    @Override
    public WorkflowTask appendChild(WorkflowTask child) {
        tasks.add(child);
        update(new AdditionEvent<>(child, tasks));
        return child;
    }

    @Override
    public Optional<WorkflowTask> removeChild(WorkflowTask child) {
        if (!tasks.remove(child)) {
            return Optional.empty();
        }
        update(new RemovalEvent<>(child));
        sequenceFlows.removeIf(s -> s.getSourceRef().equals(child.getId()) || s.getTargetRef().equals(child.getId()));
        return Optional.ofNullable(child);
    }

    @Override
    public Optional<WorkflowTask> getChildWithName(String name) {
        return tasks.stream()
                .filter(t -> t.getName().equals(name))
                .findAny();
    }

    @Override
    public void normalize() {
        String oldName = name;
        name = name.trim().replace(" ", "_");
        if (!name.equals(oldName)) {
            notifyOnChange(new RenamingEvent<>(this, oldName));
        }
        tasks.forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        tasks.forEach(IInstantiable::instantiate);
    }

    @Override
    public void subscribe(@NonNull final IObserver<AbstractTreeEvent<Process>> observer) {
        processesObservers.add(observer);
        log.trace("Observer {} has subscribed to {}", observer.getClass().getSimpleName(), this);
    }

    @Override
    public void unsubscribe(@NonNull final IObserver<AbstractTreeEvent<Process>> observer) {
        processesObservers.remove(observer);
    }

    @Override
    public void notifyOnChange(@NonNull final AbstractTreeEvent<Process> event) {
        processesObservers.forEach(o -> o.update(event));
    }

    @Override
    public void update(@NonNull final AbstractTreeEvent<WorkflowTask> event) {
        tasksObservers.forEach(o -> o.update(event));
    }
}
