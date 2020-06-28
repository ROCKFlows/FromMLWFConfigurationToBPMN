package com.ml2wf.tasks;

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
	private String name;
	/**
	 * Task's parent {@code Node}.
	 */
	private Node parent;

	/**
	 * {@code Task}'s full constructor.
	 *
	 * @param name   the task's name
	 * @param parent the task's parent
	 *
	 * @see Node
	 */
	public Task(String name, Node parent) {
		this.name = name;
		this.parent = parent;
	}

	/**
	 * {@code Task}'s default constructor.
	 *
	 * @param name   the task's name
	 * @param parent the task's parent
	 *
	 * @see Node
	 */
	public Task(String name) {
		this.name = name;
		this.parent = null;
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
	 * Returns the current task's {@code parent}.
	 *
	 * @return the current task's {@code parent}
	 */
	public Node getParent() {
		return this.parent;
	}

	/**
	 * Sets the current task's {@code parent}.
	 *
	 * @param parent the new task's {@code parent}
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
