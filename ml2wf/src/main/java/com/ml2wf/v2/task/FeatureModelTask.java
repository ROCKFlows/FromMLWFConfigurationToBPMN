package com.ml2wf.v2.task;

import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.v2.util.NodeEvaluator;
import com.ml2wf.v2.util.NodeWriter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Node;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
public class FeatureModelTask extends AbstractTask<FeatureModelTask> {

    // TODO: add isManaged setter

    @Getter @Setter(AccessLevel.PRIVATE)
    private boolean isUnmanaged;

    /**
     * {@code FeatureModelTask} constructor with a {@link Node} and a parent {@link FeatureModelTask}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract).
     *
     * @param node      the node
     * @param parent    the parent task
     */
    FeatureModelTask(Node node, FeatureModelTask parent) {
        super(node, parent);
        this.isUnmanaged = NodeEvaluator.isUnmanaged(node);
    }

    /**
     * {@code FeatureModelTask} constructor with a {@link Node}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract) with a
     * {@code null} {@link #getParent()}.
     *
     * @param node      the node
     */
    FeatureModelTask(Node node) {
        super(node);
        this.isUnmanaged = NodeEvaluator.isUnmanaged(node);
    }

    @Override
    public FeatureModelTask appendChild(FeatureModelTask childTask) {
        node.appendChild(childTask.node);
        NodeWriter.writeTag(node, FMNames.AND.getName());
        childTask.setParent(this);
        children.add(childTask);
        if (isUnmanaged) {
            childTask.setUnmanaged(true);
        }
        return childTask;
    }

    @Override
    public Optional<FeatureModelTask> removeChild(FeatureModelTask childTask) {
        if (!children.remove(childTask)) {
            // TODO: log
            return Optional.empty();
        }
        node.removeChild(childTask.node);
        if (!hasChildren()) {
            NodeWriter.writeTag(node, FMNames.FEATURE.getName());
        }
        childTask.setParent(null);
        if (isUnmanaged) {
            // TODO: by convention, we don't consider an orphan node as unmanaged ?
            childTask.setUnmanaged(false);
        }
        return Optional.of(childTask);
    }
}
