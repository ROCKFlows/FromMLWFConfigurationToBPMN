package com.ml2wf.v2.tree.fm;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.MovedEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
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
 * a {@link FeatureModel}. TODO: to update
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
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class FeatureModelStructure extends AbstractTree<FeatureModelTask>
        implements IObservable<AbstractTreeEvent<FeatureModelTask>> {

    // TODO: check if we can change FeatureModelTask to generic T
    // TODO: manage null parameters

    @Builder.Default
    @Getter
    private final List<FeatureModelTask> children = new ArrayList<>();
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
                    break;
                case REMOVAL:
                    memory.remove(featureModelTask.getName());
                    featureModelTask.unsubscribe(this);
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
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override
    public FeatureModelTask appendChild(final FeatureModelTask child) {
        children.add(child);
        notifyOnChange(new AdditionEvent<>(child, children));
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
        return Optional.of(child);
    }

    @Override
    public Optional<FeatureModelTask> getChildWithName(final String name) {
        Pair<FeatureModelTask, List<FeatureModelTask>> childPair = internalMemory.get(name);
        return Optional.ofNullable((childPair != null && childPair.isPresent()) ? childPair.getKey() : null);
    }

    @Override
    public void normalize() {
        children.forEach(FeatureModelTask::normalize);
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
