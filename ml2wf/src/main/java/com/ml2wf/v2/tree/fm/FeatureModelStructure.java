package com.ml2wf.v2.tree.fm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.MovedEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
 * A {@link FeatureModelStructure} contains the {@link FeatureModelTask} instances of
 * a {@link FeatureModel}.
 *
 * <p>
 *
 * As specified by the {@link FeatureModel} class, three kinds of tasks are differentiated.
 * These tasks are stored into three distinct collections :
 *
 * <ul>
 *     <li>the {@link #children} contains the <b>and</b> tasks</li>
 *     <li>the {@link #childrenLeaves} contains the <b>features</b></li>
 *     <li>the {@link #alternativeChildren} contains the <b>alternative</b> tasks</li>
 * </ul>
 *
 * <p>
 *
 * <b>Note</b> that this class is observed by its {@link InternalMemory} that allows fast
 * manipulation.
 *
 * @see FeatureModel
 * @see FeatureModelTask
 * @see InternalMemory
 * @see IObservable
 *
 * @since 1.1.0
 */
@JsonIgnoreProperties({"observers", "internalMemory"})
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class FeatureModelStructure extends AbstractTree<FeatureModelTask>
        implements IObservable<AbstractTreeEvent<FeatureModelTask>> {

    // TODO: check if we can change FeatureModelTask to generic T
    // TODO: manage null parameters

    @JacksonXmlProperty(localName="and")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<FeatureModelTask> children = new ArrayList<>();
    @JacksonXmlProperty(localName="feature")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<FeatureModelTask> childrenLeaves = new ArrayList<>();
    @JacksonXmlProperty(localName="alt")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<FeatureModelTask> alternativeChildren = new ArrayList<>();
    protected final Set<IObserver<AbstractTreeEvent<FeatureModelTask>>> observers = new HashSet<>();
    protected final InternalMemory internalMemory = new InternalMemory();

    /**
     * This inner class is the {@link FeatureModelStructure}'s internal memory.
     *
     * <p>
     *
     * This memory allows avoiding tree search by keeping update a {@link Map} containing
     * all useful information for manipulating a {@link FeatureModelStructure}.
     *
     * <p>
     *
     * It observes the current {@link FeatureModelStructure} implementation to keep its
     * structure memory consistent.
     *
     * @see FeatureModelStructure
     * @see IObserver
     *
     * @since 1.1.0
     */
    protected final class InternalMemory implements IObserver<AbstractTreeEvent<FeatureModelTask>> {

        @Delegate
        private final Map<String, Pair<FeatureModelTask, List<FeatureModelTask>>> memory;

        private InternalMemory() {
            memory = new HashMap<>();
            subscribe(this);
        }

        @Override
        public void update(@NonNull final AbstractTreeEvent<FeatureModelTask> event) {
            log.debug("New FeatureModel event [{}].", event);
            var featureModelTask = event.getNode();
            switch (event.getEventType()) {
                case ADDITION:
                    List<FeatureModelTask> location = ((AdditionEvent<FeatureModelTask>) event).getLocation();
                    memory.put(featureModelTask.getName(), new Pair<>(featureModelTask, location));
                    featureModelTask.subscribe(this);
                    updateParentLocation(memory.get(featureModelTask.getParent().getName()));
                    break;
                case REMOVAL:
                    memory.remove(featureModelTask.getName());
                    featureModelTask.unsubscribe(this);
                    updateParentLocation(memory.get(featureModelTask.getParent().getName()));
                    break;
                case RENAMING:
                    String oldName = ((RenamingEvent<FeatureModelTask>) event).getOldName();
                    memory.put(featureModelTask.getName(), memory.remove(oldName));
                    break;
                case MOVED:
                    List<FeatureModelTask> newLocation = ((MovedEvent<FeatureModelTask>) event).getNewLocation();
                    memory.get(featureModelTask.getName()).setValue(newLocation);
                    break;
                default:
                    throw new IllegalStateException("Unsupported event for FeatureModelStructure internal memory.");
            }
        }

        /**
         * Updates the parent location according to its children.
         *
         * <p>
         *
         * If it has no children, then it is moved to the {@link #childrenLeaves} collection.
         * If it has any children, then it is moved to the {@link #children} collection.
         *
         * @param parentPair    the {@link Pair} containing the parent task information
         */
        private void updateParentLocation(final Pair<FeatureModelTask, List<FeatureModelTask>> parentPair) {
            // TODO: manage alt case
            FeatureModelTask parent = parentPair.getKey();
            List<FeatureModelTask> oldLocation = parentPair.getValue();
            List<FeatureModelTask> newLocation;
            if (parent.hasChildren() && oldLocation == childrenLeaves) {
                newLocation = children;
                parentPair.setValue(children);
            } else if (!parent.hasChildren() && oldLocation != childrenLeaves) {
                newLocation = childrenLeaves;
            } else {
                return;
            }
            oldLocation.remove(parent);
            newLocation.add(parent);
            notifyOnChange(new MovedEvent<>(parent, oldLocation, newLocation));
        }
    }

    @Override
    public boolean hasChildren() {
        return !(children.isEmpty() && childrenLeaves.isEmpty() && alternativeChildren.isEmpty());
    }

    @Override
    public FeatureModelTask appendChild(final FeatureModelTask child) {
        if (childrenLeaves.isEmpty()) {
            childrenLeaves.add(child);
            notifyOnChange(new AdditionEvent<>(child, childrenLeaves));
        } else {
            children.add(child);
            notifyOnChange(new AdditionEvent<>(child, children));
        }
        // TODO: allow capability to add alternative child
        return child;
    }

    @Override
    public Optional<FeatureModelTask> removeChild(final FeatureModelTask child) {
        if (!internalMemory.containsKey(child.getName())) {
            return Optional.empty();
        }
        Pair<FeatureModelTask, List<FeatureModelTask>> childPair = internalMemory.get(child.getName());
        childPair.getValue().remove(child);
        notifyOnChange(new RemovalEvent<>(child));
        if (childPair.getValue().isEmpty()) {
            //moveChild(childPair.getKey(), childrenLeaves);
        }
        return Optional.of(child);
    }

    @Override
    public Optional<FeatureModelTask> getChildWithName(final String name) {
        Pair<FeatureModelTask, List<FeatureModelTask>> childPair = internalMemory.get(name);
        return Optional.ofNullable((childPair.isPresent()) ? childPair.getKey() : null);
    }

    @Override
    public void normalize() {
        children.forEach(FeatureModelTask::normalize);
        childrenLeaves.forEach(FeatureModelTask::normalize);
        alternativeChildren.forEach(FeatureModelTask::normalize);
    }

    @Override
    public void subscribe(@NonNull final IObserver<AbstractTreeEvent<FeatureModelTask>> observer) {
        observers.add(observer);
        log.trace("Observer {} has subscribed to {}", observer, this);
    }

    @Override
    public void unsubscribe(@NonNull final IObserver<AbstractTreeEvent<FeatureModelTask>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyOnChange(@NonNull final AbstractTreeEvent<FeatureModelTask> event) {
        observers.forEach(o -> o.update(event));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
