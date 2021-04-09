package com.ml2wf.tasks.concretes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.FMTaskSpecs;
import com.ml2wf.util.XMLManager;

/**
 * This class represents {@code Task} using the
 * <a href="https://featureide.github.io/">FeatureIDE framework</a>.
 *
 * <p>
 *
 * It is an extension of the {@code Task} class.
 *
 * @author Nicolas Lacroix
 *
 * @see Task
 *
 * @since 1.0.0
 */
public final class FMTask extends Task<FMTaskSpecs> {

    /**
     * Task's parent.
     */
    @Getter @Setter private FMTask parent;

    /**
     * {@code FMTask}'s full constructor.
     *
     * <p>
     *
     * It initializes a {@code FMTask} specifying its {@code name}, {@code parent},
     * {@code node} and {@code isAbstract} status.
     *
     * @param name       name of the task
     * @param parent     parent of the task
     * @param node       node of the task
     * @param isAbstract whether the task is abstract or not
     *
     * @throws UnresolvedConflict
     */
    public FMTask(String name, FMTask parent, Node node, boolean isAbstract) throws UnresolvedConflict {
        super(name, node);
        this.parent = parent;
        setAbstract(isAbstract);
        // add the new task to the manager
        // TODO: possible duplicate registration with super constructor
        TasksManager.addTask(this);
    }

    /**
     * {@code FMTask}'s partial constructor.
     *
     * <p>
     *
     * It initializes a {@code FMTask} specifying its {@code name}, {@code node} and
     * {@code isAbstract} status.
     *
     * @param name       name of the task
     * @param node       node of the task
     * @param isAbstract whether the task is abstract or not
     *
     * @throws UnresolvedConflict
     */
    public FMTask(String name, Node node, boolean isAbstract) throws UnresolvedConflict {
        super(name, node);
        setAbstract(isAbstract);
        // TODO: possible duplicate registration with super constructor
        TasksManager.addTask(this); // add the new task to the manager
    }

    /**
     * {@code FMTask}'s constructor based on an existing {@code WFTask} instance.
     *
     * <p>
     *
     * It initializes a {@code FMTask} using the given {@code task} specs and
     * consider the given {@code task}'s parent if {@code saveParent} is
     * {@code true}.
     *
     * <p>
     *
     * It is equivalent to a {@code Task} convertion from {@code WFTask} to
     * {@code FMTask}.
     *
     * @param task       {@code WFTask} instance to be converted
     * @param saveParent whether the result task should save the given
     *                   {@code task}'s parent as its own parent or not.
     *
     * @throws UnresolvedConflict
     *
     * @see WFTask
     */
    public FMTask(WFTask<?> task, boolean saveParent) throws UnresolvedConflict {
        super(task.getName(), task.getNode());
        setAbstract(task.isAbstract());
        if (saveParent) {
            Optional<FMTask> optReferredTask = TasksManager.getFMTaskWithName(task.getReference());
            optReferredTask.ifPresent(fmTask -> parent = fmTask);
        }
        // add the new task to the manager
        // TODO: possible duplicate registration with super constructor
        TasksManager.addTask(this);

    }

    @Override
    public void setAbstract(boolean isAbstract) {
        super.setAbstract(isAbstract);
        ((Element) node).setAttribute(FMAttributes.ABSTRACT.getName(), String.valueOf(isAbstract));
    }

    /**
     * Appends the given {@code child} to the current task.
     *
     * <p>
     *
     * More precisely, this method :
     *
     * <p>
     *
     * <ul>
     * <li>appends the given {@code child}'s node to the current {@link #node},</li>
     * <li>sets the given {@code child}'s parent as {@code this}.</li>
     * </ul>
     *
     * <p>
     *
     * <b>Note</b> that a {@code Task} can only have a unique {@code parent} but
     * multiple children.
     *
     * @param child task to append as child
     *
     * @return the appended child
     *
     * @see Node
     */
    @Override
    public FMTask appendChild(Task<FMTaskSpecs> child) {
        XMLManager.getDocument().renameNode(node, null, FMNames.AND.getName());
        child.setNode(node.appendChild(child.getNode()));
        ((FMTask) child).setParent(this);
        return (FMTask) child;
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
     *
     * @return an {@code Optional} containing the removed {@code oldChild} if it was
     *         a current {@code node}'s child, an empty one else.
     *
     * @see Node
     */
    @Override
    public Optional<Task<FMTaskSpecs>> removeChild(Task<FMTaskSpecs> oldChild) {
        if (!(oldChild instanceof FMTask)) {
            return Optional.empty();
        }
        // TODO: improve considering oldChild.getParent().equals(this) result
        for (Node child : XMLManager.nodeListAsList(node.getChildNodes())) {
            Node oldNode = oldChild.getNode();
            if (child.equals(oldNode)) {
                // if the oldChild is a child of this
                node.removeChild(oldNode);
                ((FMTask) oldChild).setParent(null);
                if (!node.hasChildNodes()) {
                    XMLManager.getDocument().renameNode(node, null, FMNames.FEATURE.getName());
                }
                return Optional.of(oldChild);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an {@code Optional} containing the given {@code parent}'s child with
     * the given {@code name} or an empty one if no suitable child was found.
     *
     * <p>
     *
     * <b>Note</b> that this method is {@code static}.
     *
     * @param parent    parent containing children
     * @param childName name of child to retrieve
     *
     * @return an {@code Optional} containing the given {@code parent}'s child with
     *         the given {@code name} or an empty one if no suitable child was found
     */
    private static Optional<FMTask> getChildWithName(Task<FMTaskSpecs> parent, String childName) {
        List<FMTask> childrenTasks = TasksManager.getFMTaskWithParent(parent);
        Optional<FMTask> optChild;
        for (FMTask child : childrenTasks) {
            if (child.getName().equals(childName)) {
                return Optional.of(child);
            } else {
                optChild = getChildWithName(child, childName);
                if (optChild.isPresent()) {
                    return optChild;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns an {@code Optional} containing the child with
     * the given {@code name} or an empty one if no suitable child was found.
     *
     * <p>
     *
     * <b>Note</b> that this method uses the {@link #getChildWithName(Task, String)}
     * method passing {@code this} as parent argument.
     *
     * @param childName name of child to retrieve
     *
     * @return an {@code Optional} containing the child with the given {@code name}
     *         or an empty one if no suitable child was found
     */
    public Optional<FMTask> getChildWithName(String childName) {
        return getChildWithName(this, childName);
    }

    /**
     * Returns the Lowest Common Ancestor (LCA) of the given {@code tasks}.
     *
     * @param tasks tasks to retrieve the LCA
     *
     * @return Returns the LCA of the given {@code tasks}
     */
    public static FMTask getLCA(Iterable<FMTask> tasks) {
        FMTask parent;
        List<FMTask> commonParents = new ArrayList<>();
        List<FMTask> nodeParents = new ArrayList<>();
        for (FMTask task : tasks) {
            // for each node
            parent = task;
            while ((parent = parent.getParent()) != null) {
                // get all parents
                nodeParents.add(parent);
            }
            if (commonParents.isEmpty()) {
                commonParents.addAll(nodeParents);
            }
            // retaining common parents
            commonParents.retainAll(nodeParents);
        }
        return commonParents.get(0);
    }

    @Override
    public void applySpecs() {
        FMTaskSpecs.values()[0].applyAll(this);
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
        FMTask fmTask = (FMTask) o;
        return Objects.equals(parent, fmTask.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parent);
    }
}
