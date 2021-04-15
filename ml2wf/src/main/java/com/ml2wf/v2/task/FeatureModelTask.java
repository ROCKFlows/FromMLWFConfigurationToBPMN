package com.ml2wf.v2.task;

import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.v2.util.NodeWriter;
import org.w3c.dom.Node;

import java.util.Optional;

public class FeatureModelTask extends AbstractTask<FeatureModelTask> {

    /**
     * {@code FeatureModelTask} constructor with a {@link Node}, a parent {@link FeatureModelTask} and
     * an abstract status.
     *
     * @param node          the node
     * @param parent        the parent task
     * @param isAbstract    whether the task is abstract or not
     */
    protected FeatureModelTask(Node node, FeatureModelTask parent, boolean isAbstract) {
        super(node, parent, isAbstract);
    }

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
    protected FeatureModelTask(Node node, FeatureModelTask parent) {
        super(node, parent);
    }

    /**
     * {@code FeatureModelTask} constructor with a {@link Node} and an abstract status.
     *
     * <p>
     *
     * <b>Note</b> that the task is set with a {@code null} {@link #getParent()}.
     *
     * @param node          the node
     * @param isAbstract    whether the task is abstract or not
     */
    protected FeatureModelTask(Node node, boolean isAbstract) {
        super(node, isAbstract);
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
    protected FeatureModelTask(Node node) {
        super(node);
    }

    @Override
    public FeatureModelTask appendChild(FeatureModelTask childTask) {
        node.appendChild(childTask.node);
        NodeWriter.writeTag(node, FMNames.AND.getName());
        childTask.setParent(this);
        children.add(childTask);
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
        return Optional.of(childTask);
    }
}
