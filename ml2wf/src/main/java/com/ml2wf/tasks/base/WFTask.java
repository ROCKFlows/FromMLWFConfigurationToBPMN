package com.ml2wf.tasks.base;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.Spec;

import java.util.Objects;

/**
 * This class represents a workflow {@code Task}.
 *
 * <p>
 *
 * It is an extension of the {@code Task} class.
 *
 * @author Nicolas Lacroix
 *
 * @param <T>
 *
 * @see Task
 *
 * @since 1.0.0
 */
public abstract class WFTask<T extends Spec<?>> extends Task<T> {

    /**
     * The name of the referred meta-task.
     */
    @Getter @Setter protected String reference;

    /**
     * {@code WFTask}'s full constructor.
     *
     * <p>
     *
     * It initializes a {@code WFTask} specifying its {@code name} and
     * {@code reference}.
     *
     * @param name       name of the task
     * @param reference  reference of the task
     * @param node       node of the task
     * @param isAbstract whether the task is abstract or not
     *
     * @throws UnresolvedConflict
     */
    protected WFTask(String name, Node node, boolean isAbstract, String reference) throws UnresolvedConflict {
        super(name, node, isAbstract);
        this.reference = reference;
        // add the new task to the manager
        TasksManager.addTask(this);
    }

    @Override
    public WFTask<T> appendChild(Task<T> child) {
        child.setNode(this.node.appendChild(child.getNode()));
        return (WFTask<T>) child;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        WFTask<?> wfTask = (WFTask<?>) o;
        return Objects.equals(reference, wfTask.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reference);
    }
}
