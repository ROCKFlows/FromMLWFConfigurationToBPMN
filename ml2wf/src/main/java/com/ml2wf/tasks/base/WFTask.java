package com.ml2wf.tasks.base;

import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.Spec;

/**
 * This class represents a workflow {@code Task}.
 *
 * <p>
 *
 * It is an extension of the {@code Task} class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 * @param <T>
 *
 * @see Task
 *
 */
public abstract class WFTask<T extends Spec<?>> extends Task<T> {

	/**
	 * The name of the referred meta-task.
	 */
	protected String reference;

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
	 * @throws UnresolvedConflict
	 */
	public WFTask(String name, Node node, boolean isAbstract, String reference)
			throws UnresolvedConflict {
		super(name, node, isAbstract);
		this.reference = reference;
		TasksManager.addTask(this); // add the new task to the manager
	}

	/**
	 * Returns the current {@link #reference}.
	 *
	 * @return the current {@code reference}
	 */
	public String getReference() {
		return this.reference;
	}

	/**
	 * Sets the current {@link #reference}.
	 *
	 * @param reference the new task's reference
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public WFTask<T> appendChild(Task<T> child) {
		child.setNode(this.node.appendChild(child.getNode()));
		return (WFTask<T>) child;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.reference == null) ? 0 : this.reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof WFTask) && super.equals(obj);
	}

	@Override
	public String toString() {
		return "WFTask : " + this.getName();
	}

}
