package com.ml2wf.v2.tree.fm;

import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.tree.events.AdditionEvent;
import com.ml2wf.v2.tree.events.MovedEvent;
import com.ml2wf.v2.tree.events.RemovalEvent;
import com.ml2wf.v2.tree.events.RenamingEvent;
import com.ml2wf.v2.util.observer.IObservable;
import com.ml2wf.v2.util.observer.IObserver;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link FeatureModelStructure} is an {@link AbstractTree} extension containing the
 * {@link FeatureModelTask} instances of a {@link FeatureModel}.
 *
 * <p>
 *
 * <b>Note</b> that this class is observed by its {@link FeatureModelInternalMemory} that allows fast
 * manipulation.
 *
 * @see FeatureModel
 * @see FeatureModelTask
 * @see FeatureModelInternalMemory
 * @see IObservable
 *
 * @since 1.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class FeatureModelStructure extends AbstractTree<FeatureModelTask> {

    /**
     * {@code FeatureModelStructure}'s default constructor.
     *
     * <p>
     *
     * <b>Note</b> that this constructor initializes the structure's {@link #internalMemory}.
     */
    protected FeatureModelStructure() {
        this.internalMemory = new FeatureModelInternalMemory();
    }

    /**
     * {@code FeatureModelStructure}'s constructor with some children {@link FeatureModelTask}s.
     *
     * <p>
     *
     * <b>Note</b> that this constructor calls the default {@link #FeatureModelStructure()} constructor
     * that initializes the structure's {@link #internalMemory}.
     *
     * @param children  the new structure's children tasks
     */
    public FeatureModelStructure(@NonNull List<FeatureModelTask> children) {
        this();
        this.children.addAll(children);
    }

    /**
     * This inner class is the {@link FeatureModelStructure}'s internal memory.
     *
     * <p>
     *
     * This memory allows avoiding time-consuming tree search by keeping update a {@link Map} containing
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
    protected final class FeatureModelInternalMemory extends AbstractInternalMemory {

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
    public Optional<FeatureModelTask> removeChild(@NonNull final FeatureModelTask child) {
        if (!internalMemory.containsKey(child.getName())) {
            return Optional.empty();
        }
        Pair<FeatureModelTask, List<FeatureModelTask>> childPair = internalMemory.get(child.getName());
        childPair.getValue().remove(child);
        notifyOnChange(new RemovalEvent<>(child));
        return Optional.of(child);
    }

    @Override
    public void normalize() {
        children.forEach(INormalizable::normalize);
    }
}
