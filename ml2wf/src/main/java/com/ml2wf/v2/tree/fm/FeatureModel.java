package com.ml2wf.v2.tree.fm;

import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.events.AbstractTreeEvent;
import com.ml2wf.v2.util.observer.IObserver;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link FeatureModel} has a {@link FeatureModelStructure} containing its
 * {@link FeatureModelTask}s.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractTree} class but delegates its contract methods
 * to its {@link #structure}.
 *
 * @see AbstractTree
 * @see FeatureModelStructure
 * @see FeatureModelTask
 *
 * @since 1.1.0
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeatureModel extends AbstractTree<FeatureModelTask, String> implements Iterable<FeatureModelTask> {

    private FeatureModelStructure structure;
    @Getter @NonNull private final Set<FeatureModelRule> constraints = new LinkedHashSet<>();

    /**
     * {@code FeatureModel}'s empty private constructor.
     *
     * <p>
     *
     * <b>Note</b> that this class is used by Jackson for deserialization.
     */
    private FeatureModel() {
        // used by Jackson for deserialization
        super(new ArrayList<>());
    }

    /**
     * {@code FeatureModel}'s private constructor with a {@link FeatureModelStructure}.
     *
     * <p>
     *
     * Only used by the {@link FeatureModelFactory}.
     *
     * @param structure the {@link FeatureModelStructure}
     *
     * @see FeatureModelStructure
     * @see FeatureModelFactory
     */
    private FeatureModel(@NonNull FeatureModelStructure structure) {
        this();
        this.structure = structure;
    }

    /**
     * {@link FeatureModel}'s factory.
     *
     * @see FeatureModel
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FeatureModelFactory {
        public static FeatureModel createFeatureModel() {
            return new FeatureModel(new FeatureModelStructure());
        }
    }

    @Override
    public Iterator<FeatureModelTask> iterator() {
        // delegates to structure
        return structure.iterator();
    }

    @Override
    public @NonNull List<FeatureModelTask> getChildren() {
        // delegates to structure
        return structure.getChildren();
    }

    @Override
    public Collection<FeatureModelTask> getChildrenMatching(@NonNull Predicate<FeatureModelTask> predicate) {
        // delegates to structure
        return structure.getChildrenMatching(predicate);
    }

    @Override
    public boolean hasChildren() {
        // delegates to structure
        return structure.hasChildren();
    }

    @Override
    public Either<String, FeatureModelTask> appendDirectChild(FeatureModelTask child) {
        // delegates to structure
        return structure.appendDirectChild(child);
    }

    @Override
    public Optional<FeatureModelTask> removeDirectChild(FeatureModelTask child) {
        // delegates to structure
        return structure.removeDirectChild(child);
    }

    @Override
    public Optional<FeatureModelTask> getChildWithIdentity(@NonNull String identity) {
        // delegates to structure
        return structure.getChildWithIdentity(identity);
    }

    @Override
    public Optional<FeatureModelTask> getChildMatching(@NonNull Predicate<FeatureModelTask> predicate) {
        // delegates to structure
        return structure.getChildMatching(predicate);
    }

    @Override
    public boolean hasChildWithIdentity(@NonNull String identity) {
        // delegates to structure
        return structure.hasChildWithIdentity(identity);
    }

    @Override
    public void normalize() {
        // delegates to structure
        structure.normalize();
    }

    @Override
    public void subscribe(@NonNull IObserver<AbstractTreeEvent<FeatureModelTask>> observer) {
        // delegates to structure
        structure.subscribe(observer);
    }

    @Override
    public void unsubscribe(@NonNull IObserver<AbstractTreeEvent<FeatureModelTask>> observer) {
        // delegates to structure
        structure.unsubscribe(observer);
    }

    @Override
    public void notifyOnChange(@NonNull AbstractTreeEvent<FeatureModelTask> event) {
        // delegates to structure
        structure.notifyOnChange(event);
    }
}
