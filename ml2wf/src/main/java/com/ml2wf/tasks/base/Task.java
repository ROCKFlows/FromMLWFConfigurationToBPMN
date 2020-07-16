package com.ml2wf.tasks.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.w3c.dom.Node;

import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.Spec;

/**
 * This class represents a {@code Task}.
 *
 * <p>
 *
 * More precisely, it only contains uselful {@code Node}'s data and is more easy
 * to use than a classic {@code Node}.
 *
 *
 * @author Nicolas Lacroix
 *
 * @param <T> Any {@code class} implementing the {@code Spec interface}
 *
 * @version 1.0
 *
 * @see Node
 *
 */
public abstract class Task<T extends Spec<?>> {

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
	 * {@code Map} containing the current task specifications.
	 */
	protected Map<String, String> specs;

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
		this.specs = new HashMap<>();
		TasksManager.addTask(this); // add the new task to the manager
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
	 * Sets whether the current task's is abstract or not.
	 *
	 * @param isAbstract the new task's abstract status
	 */
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
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
	 * @param node the new task's {@code node}
	 */
	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Returns the current task's specifications {@code Map}.
	 *
	 * @return the current task's specifications {@code Map}
	 *
	 * @since 1.0
	 * @see Spec
	 */
	public Map<String, String> getSpecs() {
		return this.specs;
	}

	/**
	 * Returns the given {@code spec} value contained in the current task's
	 * specifications {@code Map} or {@code null} if it is not found.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method behaves like the {@link Map#get(Object)}
	 * method.
	 *
	 * @param spec specification to retrieve
	 * @return the current task's specifications {@code Map}
	 *
	 * @since 1.0
	 * @see Spec
	 */
	public String getSpecValue(String spec) {
		return this.specs.get(spec);
	}

	/**
	 * Adds the given {@code spec} associated to its given {@code value} to the
	 * current task's specifications {@code Map}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method behaves like the {@link Map#put(Object, Object)}
	 * method.
	 *
	 * @param spec  specification to add
	 * @param value the {@code spec} value
	 *
	 * @since 1.0
	 * @see Spec
	 */
	public void addSpec(String spec, String value) {
		this.specs.put(spec, value);
	}

	/**
	 * Adds the given {@code specs} to the current task's specifications
	 * {@code Map}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method behaves like the {@link Map#putAll(Map)}
	 * method.
	 *
	 * @param specs specifications to add
	 *
	 * @since 1.0
	 * @see Spec
	 */
	public void addAllSpecs(Map<String, String> specs) {
		this.specs.putAll(specs);
	}

	/**
	 * Applies the current specifications to the current {@link node}.
	 */
	public abstract void applySpecs();

	/**
	 * Appends the given {@code child} to the current task.
	 *
	 * @param child task to append as child
	 * @return the appended child
	 *
	 * @since 1.0
	 * @see Nod
	 */
	public abstract Task<T> appendChild(Task<T> child);

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
	public abstract Optional<Task<T>> removeChild(Task<T> oldChild);

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
		@SuppressWarnings("unchecked")
		Task<T> other = (Task<T>) obj;
		if (this.name == null) {
			return this.name == other.name;
		} else {
			return this.name.equals(other.name);
		}
	}

	@Override
	public String toString() {
		return "Task : " + this.getName();
	}

}
