package com.ml2wf.v2.task;

import com.ml2wf.v2.util.NodeReader;
import com.ml2wf.v2.util.NodeWriter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractTask<T extends AbstractTask<T>> {

    // TODO: set default abstract+name according to the given node
    // TODO: add nonnull annotations

    @Getter protected String name;
    @Getter @Setter(AccessLevel.PROTECTED)
    protected T parent;
    @Getter protected Set<T> children;
    @Getter protected final Node node;
    @Getter protected boolean isAbstract;

    /**
     * {@code AbstractTask} constructor with a {@link Node}, a parent {@link AbstractTask} and
     * an abstract status.
     *
     * @param node          the node
     * @param parent        the parent task
     * @param isAbstract    whether the task is abstract or not
     */
    protected AbstractTask(Node node, T parent, boolean isAbstract) {
        this.parent = parent;
        children = new HashSet<>();
        this.node = node;
        this.isAbstract = isAbstract;
        this.name = NodeReader.getName(this.node);
    }

    /**
     * {@code AbstractTask} constructor with a {@link Node} and a parent {@link AbstractTask}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract).
     *
     * @param node      the node
     * @param parent    the parent task
     */
    protected AbstractTask(Node node, T parent) {
        this(node, parent, NodeReader.isAbstract(node));
    }

    /**
     * {@code AbstractTask} constructor with a {@link Node} and an abstract status.
     *
     * <p>
     *
     * <b>Note</b> that the task is set with a {@code null} {@link #parent}.
     *
     * @param node          the node
     * @param isAbstract    whether the task is abstract or not
     */
    protected AbstractTask(Node node, boolean isAbstract) {
        this(node, null, isAbstract);
    }

    /**
     * {@code AbstractTask} constructor with a {@link Node}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract) with a
     * {@code null} {@link #parent}.
     *
     * @param node      the node
     */
    protected AbstractTask(Node node) {
        this(node, NodeReader.isAbstract(node));
    }

    /**
     * Appends the given child task to the current task.
     *
     * @param childTask the child task
     *
     * @return the appended child
     */
    public abstract T appendChild(T childTask);

    /**
     * Removes the given child task from the current task if it is present in its
     * {@link #children} collection.
     *
     * @param childTask the child task
     *
     * @return an {@link Optional} containing the removed child
     */
    public abstract Optional<T> removeChild(T childTask);

    /**
     * Returns the {@link #node}'s owner {@link Document}.
     *
     * @return the {@link #node}'s owner {@link Document}
     *
     * @see Document
     */
    public Document getDocument() {
        return node.getOwnerDocument();
    }

    /**
     * Sets the current task's name and updates its internal {@link #node}.
     *
     * @param name  the new task's name
     */
    public void setName(String name) {
        NodeWriter.writeName(node, name);
        this.name = name;
    }

    /**
     * Sets the current task's abstract status and updates its internal {@link #node}.
     *
     * @param isAbstract    the new task abstract status
     */
    public void setAbstract(boolean isAbstract) {
        NodeWriter.writeAbstractStatus(node, isAbstract);
        this.isAbstract = isAbstract;
    }

    /**
     * Returns whether the current task has children or not.
     *
     * @return whether the current task has children or not
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Normalizes the current task by trimming its name and replacing whitespaces by underscores.
     */
    public void normalize() {
        setName(name.trim().replace(" ", "_"));
    }
}
