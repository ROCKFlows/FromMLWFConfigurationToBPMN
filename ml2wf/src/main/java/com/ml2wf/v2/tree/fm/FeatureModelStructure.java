package com.ml2wf.v2.tree.fm;

import com.ml2wf.v2.tree.AbstractTree;
import com.ml2wf.v2.tree.INormalizable;
import com.ml2wf.v2.util.observer.IObservable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Log4j2
public class FeatureModelStructure extends AbstractTree<FeatureModelTask, String> {

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
        getChildren().addAll(children);
    }

    @Override
    public void normalize() {
        getChildren().forEach(INormalizable::normalize);
    }
}
