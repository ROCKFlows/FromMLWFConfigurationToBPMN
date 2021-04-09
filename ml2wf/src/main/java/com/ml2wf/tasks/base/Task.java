package com.ml2wf.tasks.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Node;

import com.ml2wf.tasks.exceptions.InvalidTaskException;
import com.ml2wf.tasks.specs.Spec;

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
 * @param <T> Any {@code class} implementing the {@code Spec interface}
 *
 * @see Node
 *
 * @since 1.0.0
 */
public abstract class Task<T extends Spec<?>> {

    /**
     * Task's name.
     */
    @Getter protected String name;
    /**
     * The task's {@code Node} instance.
     */
    @Getter @Setter protected Node node;
    /**
     * Abstract status.
     */
    @Getter @Setter protected boolean isAbstract;
    /**
     * {@code Map} containing the current task specifications.
     */
    @Getter protected Map<String, String> specs;

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
    protected Task(String name, Node node, boolean isAbstract) {
        if (name == null) {
            throw new InvalidTaskException("Can't create a task with a nullable name");
        }
        this.name = name;
        this.node = node;
        this.isAbstract = isAbstract;
        this.specs = new HashMap<>();
    }

    /**
     * {@code Task}'s partial constructor.
     *
     * <p>
     *
     * It initializes a {@code Task} specifying its {@code name}, {@code parent} and
     * {@code node}.
     *
     * @param name name of the task
     * @param node node of the task
     */
    protected Task(String name, Node node) {
        if (name == null) {
            throw new InvalidTaskException("Can't create a task with a nullable name");
        }
        this.name = name;
        this.node = node;
        this.isAbstract = false;
        this.specs = new HashMap<>();
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
     *
     * @return the current task's specifications {@code Map}
     *
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
     * @see Spec
     */
    public void addAllSpecs(Map<String, String> specs) {
        this.specs.putAll(specs);
    }

    /**
     * Applies the current specifications to the current {@link #node}.
     */
    public abstract void applySpecs();

    /**
     * Appends the given {@code child} to the current task.
     *
     * @param child task to append as child
     *
     * @return the appended child
     *
     * @see Task
     */
    public abstract Task<T> appendChild(Task<T> child);

    /**
     * Removes the given {@code oldChild} from the current {@code node}'s children
     * list.
     *
     * @param oldChild  the child to remove
     *
     * @return an {@code Optional} containing the removed {@code oldChild} if it was
     *         a current {@code node}'s child, an empty one else.
     *
     * @see Node
     */
    public abstract Optional<Task<T>> removeChild(Task<T> oldChild);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task<?> task = (Task<?>) o;
        return isAbstract == task.isAbstract &&
                Objects.equals(name, task.name) &&
                Objects.equals(node, task.node) &&
                Objects.equals(specs, task.specs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, node, isAbstract, specs);
    }

    @Override
    public String toString() {
        return String.format("%s : %s", getClass().getSimpleName(), name);
    }
}
