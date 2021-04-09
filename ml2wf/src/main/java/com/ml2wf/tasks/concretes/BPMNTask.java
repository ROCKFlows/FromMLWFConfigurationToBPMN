package com.ml2wf.tasks.concretes;

import java.util.Optional;

import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.BPMNTaskSpecs;

/**
 * This class represents {@code Task} using the <a href="http://www.bpmn.org/">BPMN standard</a>.
 *
 * <p>
 *
 * It is an extension of the {@code WFTask} class.
 *
 * @author Nicolas Lacroix
 *
 * @see WFTask
 *
 * @since 1.0.0
 */
public final class BPMNTask extends WFTask<BPMNTaskSpecs> {

    /**
     * {@code BPMNTask}'s full constructor.
     *
     * <p>
     *
     * It initializes a {@code BPMNTask} specifying its {@code name} and
     * {@code reference}.
     *
     * @param name       name of the task
     * @param node       node of the task
     * @param isAbstract whether the task is abstract or not
     * @param reference  reference of the task
     *
     * @throws UnresolvedConflict
     */
    public BPMNTask(String name, Node node, boolean isAbstract, String reference) throws UnresolvedConflict {
        super(name, node, isAbstract, reference);
    }

    /**
     * Removes the given {@code oldChild} from the current {@code node}'s children
     * list.
     *
     * <p>
     *
     * More precisely, this method :
     *
     * <p>
     *
     * <ul>
     * <li>removes the given {@code oldChild}'s node to the current
     * {@link #node}'s children list,</li>
     * <li>sets the given {@code oldChild}'s parent as {@code null}.</li>
     * </ul>
     *
     * <p>
     *
     * <b>Note</b> that the given {@code oldChild} will be removed from the current
     * {@code node}'s children list only if its {@code parent} equals {@code this}.
     *
     * @param oldChild the child to remove
     * @return an {@code Optional} containing the removed {@code oldChild} if it was
     *         a current {@code node}'s child, an empty one else.
     *
     * @see Node
     */
    @Override
    public Optional<Task<BPMNTaskSpecs>> removeChild(Task<BPMNTaskSpecs> oldChild) {
        Node oldNode = oldChild.getNode();
        Optional<WFTask<?>> optTask = TasksManager.getWFTaskWithNode(oldNode);
        if (optTask.isPresent()) {
            node.removeChild(oldNode);
            return Optional.of((BPMNTask) optTask.get());
        }
        return Optional.empty();
    }

    @Override
    public void applySpecs() {
        BPMNTaskSpecs.values()[0].applyAll(this);
    }
}
