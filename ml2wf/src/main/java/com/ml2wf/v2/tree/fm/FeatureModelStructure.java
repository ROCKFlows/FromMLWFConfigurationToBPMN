package com.ml2wf.v2.tree.fm;

import com.google.common.collect.Iterables;
import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.util.observer.IObservable;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A {@link FeatureModelStructure} is an {@link AbstractTree} extension containing the
 * {@link FeatureModelTask} instances of a {@link FeatureModel}.
 *
 * @see FeatureModel
 * @see FeatureModelTask
 * @see IObservable
 *
 * @since 1.1.0
 */
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class FeatureModelStructure extends AbstractTree<FeatureModelTask, String> implements Iterable<FeatureModelTask> {

    /**
     * {@code FeatureModelStructure}'s constructor with some children {@link FeatureModelTask}s.
     *
     * @param children  the new structure's children tasks
     */
    public FeatureModelStructure(List<FeatureModelTask> children) {
        super(children);
    }

    /**
     * {@code FeatureModelStructure}'s empty constructor used by jackson for deserialization.
     */
    @SuppressWarnings("unused")
    protected FeatureModelStructure() {
        // used by Jackson for deserialization
        this(new ArrayList<>());
    }

    @Override
    public void normalize() {
        getChildren().forEach(INormalizable::normalize);
    }

    /**
     * A recursive iterator for the {@link #getChildren()}.
     */
    @Override
    public Iterator<FeatureModelTask> iterator() {
        return Iterables.concat(getChildren()).iterator();
    }
}
