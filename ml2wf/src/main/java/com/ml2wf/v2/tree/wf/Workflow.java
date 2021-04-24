package com.ml2wf.v2.tree.wf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
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
 * A {@link Workflow} contains a list of {@link #processes}.
 *
 * <p>
 *
 * Each {@link Process} contains the workflow's tasks and links.
 *
 * @see Process
 *
 * @since 1.1.0
 */
@JacksonXmlRootElement(localName = "bpmn2:definitions")
@JsonIgnoreProperties({"observers", "internalMemory"})
@Data // TODO: check data
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class Workflow extends AbstractTree<Process> implements IInstantiable, IObservable<AbstractTreeEvent<Process>>,
        IObserver<AbstractTreeEvent<Process>> {

    protected final List<Process> processes;
    private final Set<IObserver<AbstractTreeEvent<Process>>> observers;
    private final InternalMemory internalMemory;

    @JsonCreator
    public Workflow(@JacksonXmlProperty(localName="bpmn2:process")
                    @JacksonXmlElementWrapper(useWrapping = false) List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.observers = new HashSet<>();
        this.internalMemory = new InternalMemory();
        processes.forEach(p -> p.subscribe(this));
    }

    protected final class InternalMemory implements IObserver<AbstractTreeEvent<Process>> {

        @Delegate
        private final Map<String, Pair<Process, List<Process>>> memory;

        private InternalMemory() {
            memory = new HashMap<>();
            subscribe(this);
        }

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
                    // TODO: should we do something ?
                    return;
                default:
                    throw new IllegalStateException("Unsupported event for Workflow internal memory.");
            }
        }
    }

    @Override
    public Process appendChild(final Process child) {
        processes.add(child);
        notifyOnChange(new AdditionEvent<>(child, processes));
        return child;
    }

    @Override
    public Optional<Process> removeChild(final Process child) {
        if (!processes.remove(child)) {
            return Optional.empty();
        }
        notifyOnChange(new RemovalEvent<>(child));
        return Optional.of(child);
    }

    @Override
    public Optional<Process> getChildWithName(final String name) {
        return processes.stream()
                .filter(p -> p.getChildWithName(name).isPresent())
                .findAny();
    }

    @Override
    public void normalize() {
        processes.forEach(INormalizable::normalize);
    }

    @Override
    public void instantiate() {
        processes.forEach(IInstantiable::instantiate);
    }

    @Override
    public void subscribe(@NonNull final IObserver<AbstractTreeEvent<Process>> observer) {
        observers.add(observer);
        log.trace("Observer {} has subscribed to workflow", observer.getClass().getSimpleName());
    }

    @Override
    public void unsubscribe(@NonNull final IObserver<AbstractTreeEvent<Process>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyOnChange(@NonNull final AbstractTreeEvent<Process> event) {
        observers.forEach(o -> o.update(event));
    }

    @Override
    public void update(@NonNull final AbstractTreeEvent<Process> event) {
        observers.forEach(o -> o.update(event));
    }
}
