package com.ml2wf.tasks.base;

import java.util.Optional;

import org.w3c.dom.Node;

/**
 * This class represents a {@code Task}.
 *
 * <p>
 *
 * More precisely, it only contains uselful {@code Node}'s data and is more easy
 * to use than a classic {@code Node}.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Node
 *
 */
public abstract class Task {

	/**
	 * Task's name.
	 */
	protected String name;
	/**
	 * The task's {@code Node} instance.
	 */
	protected Node node;
	/**
	 * Abstract status.
	 */
	protected boolean isAbstract;

	/**
	 * {@code Task}'s full constructor.
	 *
	 * <p>
	 *
	 * It initializes a {@code Task} specifying its {@code name}, {@code parent},
	 * {@code node} and {@code isAbstract} status.
	 *
	 * @param name       name of the task
	 * @param node       node of the task
	 * @param isAbstract whether the task is abstract or not
	 */
	public Task(String name, Node node, boolean isAbstract) {
		this.name = name;
		this.node = node;
		this.isAbstract = isAbstract;
	}

	/**
	 * Returns the current task's {@code name}.
	 *
	 * @return the current task's {@code name}
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns whether the current task {@link #isAbstract} or not.
	 *
	 * @return whether the current task is abstract or not
	 */
	public boolean isAbstract() {
		return this.isAbstract;
	}

	/**
	 * Returns the current task's {@link #node}.
	 *
	 * @return the current task's {@code node}
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * Sets the current task's {@link #node}.
	 *
	 * @param the new task's {@code node}
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Appends the given {@code child} to the current task.
	 *
	 * @param child task to append as child
	 * @return the appended child
	 *
	 * @since 1.0
	 * @see Node
	 */
	public Task appendChild(Task child) {
		child.setNode(this.node.appendChild(child.getNode()));
		return child;
	}

	/**
	 * Removes the given {@code oldChild} from the current {@code node}'s children
	 * list.
	 *
	 * @param oldChild the child to remove
	 * @return an {@code Optional} containing the removed {@code oldChild} if it was
	 *         a current {@code node}'s child, an empty one else.
	 *
	 * @since 1.0
	 * @see Node
	 */
	public abstract Optional<Task> removeChild(Task oldChild);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (this.isAbstract ? 1231 : 1237);
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
		result = (prime * result) + ((this.node == null) ? 0 : this.node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Task other = (Task) obj;
		if (this.name == null) {
			return this.name == other.name;
		} else {
			return this.name.equals(other.name);
		}
	}

}
