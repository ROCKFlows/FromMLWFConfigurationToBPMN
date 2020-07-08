package com.ml2wf.tasks.concretes;

import java.util.Optional;

import org.w3c.dom.Node;

import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.manager.TasksManager;

/**
 * This class represents {@code Task} using the
 * <a href="http://www.bpmn.org/">BPMN standard</a>.
 *
 * <p>
 *
 * It is an extension of the {@code WFTask} class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see WFTask
 *
 */
public final class BPMNTask extends WFTask {

	/**
	 * {@code BPMNTask}'s full constructor.
	 *
	 * <p>
	 *
	 * It initializes a {@code BPMNTask} specifying its {@code name} and
	 * {@code reference}.
	 *
	 * @param name       name of the task
	 * @param reference  reference of the task
	 * @param node       node of the task
	 * @param isAbstract whether the task is abstract or not
	 */
	public BPMNTask(String name, Node node, boolean isAbstract, String reference) {
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
	 * @since 1.0
	 * @see Node
	 */
	@Override
	public Optional<Task> removeChild(Task oldChild) {
		Node oldNode = oldChild.getNode();
		Optional<BPMNTask> optTask = TasksManager.getBPMNTaskWithNode(oldNode);
		if (optTask.isPresent()) {
			this.node.removeChild(oldNode);
			return Optional.of((Task) optTask.get());
		}
		return Optional.empty();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof BPMNTask) && super.equals(obj);
	}
}