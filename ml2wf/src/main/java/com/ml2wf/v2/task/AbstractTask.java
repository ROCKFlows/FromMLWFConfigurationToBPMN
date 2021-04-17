package com.ml2wf.v2.task;

import com.ml2wf.v2.util.NodeDescriber;
import com.ml2wf.v2.util.NodeReader;
import com.ml2wf.v2.util.NodeWriter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode
public abstract class AbstractTask<T extends AbstractTask<T>> {

    // TODO: add nonnull annotations

    @Getter private String name;
    @Getter @Setter(AccessLevel.PROTECTED)
    protected T parent;
    @Getter protected Set<T> children;
    @Getter protected final Node node;
    @Getter private boolean isAbstract;

    /**
     * {@code AbstractTask} constructor with a {@link Node} and a parent {@link AbstractTask}.
     *
     * @param node          the node
     * @param parent        the parent task
     */
    AbstractTask(Node node, T parent) {
        this.node = node;
        this.parent = parent;
        this.children = new HashSet<>(); // TODO: to fill
        this.isAbstract = NodeReader.isAbstract(this.node);
        this.name = NodeReader.getName(this.node);
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
    AbstractTask(Node node) {
        this(node, null);
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

    @Override
    public String toString() {
        return String.format("%s (%s) {parent=%s, isAbstract=%s, node=[%s]",
                getClass().getSimpleName(),
                name,
                (parent != null) ? parent.getName() : null,
                isAbstract,
                NodeDescriber.getNodeDescription(node)
        );
    }
}
