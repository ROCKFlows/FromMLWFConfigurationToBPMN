package com.ml2wf.v2.task;

import org.w3c.dom.Node;

import java.util.Optional;

public class WorkflowTask extends AbstractTask<WorkflowTask> {

    /**
     * {@code WorkflowTask} constructor with a {@link Node}, a parent {@link AbstractTask} and
     * an abstract status.
     *
     * @param node          the node
     * @param parent        the parent task
     * @param isAbstract    whether the task is abstract or not
     */
    protected WorkflowTask(Node node, WorkflowTask parent, boolean isAbstract) {
        super(node, parent, isAbstract);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node} and a parent {@link AbstractTask}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract).
     *
     * @param node      the node
     * @param parent    the parent task
     */
    protected WorkflowTask(Node node, WorkflowTask parent) {
        super(node, parent);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node} and an abstract status.
     *
     * <p>
     *
     * <b>Note</b> that the task is set with a {@code null} {@link #getParent()}.
     *
     * @param node          the node
     * @param isAbstract    whether the task is abstract or not
     */
    protected WorkflowTask(Node node, boolean isAbstract) {
        super(node, isAbstract);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract) with a
     * {@code null} {@link #getParent()}.
     *
     * @param node      the node
     */
    protected WorkflowTask(Node node) {
        super(node);
    }

    @Override
    public WorkflowTask appendChild(WorkflowTask childTask) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<WorkflowTask> removeChild(WorkflowTask childTask) {
        throw new UnsupportedOperationException("TODO");
    }
}
