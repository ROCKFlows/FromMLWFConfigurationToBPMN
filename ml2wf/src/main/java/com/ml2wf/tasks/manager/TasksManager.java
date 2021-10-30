package com.ml2wf.tasks.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Node;

import com.ml2wf.conflicts.ConflictSolver;
import com.ml2wf.conflicts.ConflictsManager;
import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.XMLManager;

/**
 * This class manages all created tasks.
 *
 * <p>
 *
 * <b>Note</b> that it uses {@code Set} to store created tasks and thus to avoid
 * tasks duplication.
 *
 * @author Nicolas Lacroix
 *
 * @see Task
 * @see FMTask
 * @see WFTask
 *
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public final class TasksManager {

    /**
     * {@code Set} containing all {@code FMTask}.
     *
     * <p>
     *
     * <b>Note</b> that this is a {@code LinkedHashSet} that allows to keep the
     * insertion order which is needed for workflows (which are sequantial).
     */
    private static final Set<FMTask> fmTasks;
    /**
     * {@code Set} containing all {@code WFTask}.
     */
    private static final Set<WFTask<?>> wfTasks;
    /**
     * The manager dedicated to the conflict resolution.
     */
    private static final ConflictSolver conflictManager;

    static {
        fmTasks = new LinkedHashSet<>();
        wfTasks = new HashSet<>();
        conflictManager = new ConflictsManager();
    }

    // getters

    /**
     * Returns all stored {@code Task}.
     *
     * @param <T> Any {@code class} extending the {@code Task class}
     *
     * @return all stored {@code Task}
     *
     * @see Task
     */
    @SuppressWarnings("unchecked")
    public static <T extends Task<?>> Set<T> getTasks() {
        return (Set<T>) Stream.concat(fmTasks.stream(), wfTasks.stream()).collect(Collectors.toSet());
    }

    /**
     * Returns all stored {@code FMTask}.
     *
     * @return all stored {@code FMTask}
     *
     * @see FMTask
     */
    public static Set<FMTask> getFMTasks() {
        return fmTasks;
    }

    /**
     * Returns an {@code Optional} containing the {@code FMTask} with the given
     * {@code name}.
     *
     * <p>
     *
     * If there is no {@code FMTask} with the given {@code name}, then the
     * returned {@code Optional} is empty.
     *
     * @param name name of wanted task
     *
     * @return an {@code Optional} containing the {@code FMTask} with the given
     *         {@code name}
     *
     * @see FMTask
     */
    public static Optional<FMTask> getFMTaskWithName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return fmTasks.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst();
    }

    /**
     * Returns an {@code Optional} containing the {@code FMTask} with the given
     * {@code parent}.
     *
     * <p>
     *
     * If there is no {@code FMTask} with the given {@code parent}, then the
     * returned {@code Optional} is empty.
     *
     * @param parent parent of wanted task
     *
     * @return an {@code Optional} containing the {@code FMTask} with the given
     *         {@code parent}
     *
     * @see FMTask
     */
    public static List<FMTask> getFMTaskWithParent(Task<?> parent) {
        if (parent == null) {
            return new ArrayList<>();
        }
        return fmTasks.stream()
                .filter(t -> (t.getParent() != null) && t.getParent().equals(parent))
                .collect(Collectors.toList());
    }

    /**
     * Returns an {@code Optional} containing the {@code FMTask} with the given
     * {@code node}.
     *
     * <p>
     *
     * If there is no {@code FMTask} with the given {@code node}, then the returned
     * {@code Optional} is empty.
     *
     * @param node node of wanted task
     *
     * @return an {@code Optional} containing the {@code FMTask} with the given
     *         {@code node}

     * @see FMTask
     */
    public static Optional<FMTask> getFMTaskWithNode(Node node) {
        if (node == null) {
            return Optional.empty();
        }
        return fmTasks.stream()
                .filter(t -> t.getNode().equals(node))
                .findFirst();
    }

    /**
     * Returns all stored {@code WFTask}.
     *
     * @return all stored {@code WFTask}
     *
     * @see WFTask
     */
    public static Set<WFTask<?>> getWFTasks() {
        return wfTasks;
    }

    /**
     * Returns an {@code Optional} containing the {@code WFTask} with the given
     * {@code name}.
     *
     * <p>
     *
     * If there is no {@code WFTask} with the given {@code name}, then the
     * returned {@code Optional} is empty.
     *
     * @param name name of wanted task
     *
     * @return an {@code Optional} containing the {@code WFTask} with the given
     *         {@code name}
     *
     * @see WFTask
     */
    public static Optional<WFTask<?>> getWFTaskWithName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return wfTasks.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst();
    }

    /**
     * Returns an {@code Optional} containing the {@code WFTask} with the given
     * {@code node}.
     *
     * <p>
     *
     * If there is no {@code WFTask} with the given {@code node}, then the returned
     * {@code Optional} is empty.
     *
     * @param node node of wanted task
     *
     * @return an {@code Optional} containing the {@code WFTask} with the given
     *         {@code node}
     *
     * @see WFTask
     */
    public static Optional<WFTask<?>> getWFTaskWithNode(Node node) {
        if (node == null) {
            return Optional.empty();
        }
        return wfTasks.stream()
                .filter(t -> t.getNode().equals(node))
                .findFirst();
    }

    // adder

    /**
     * Adds the given {@code task} to the right {@code Set} according to its nature.
     *
     * @param <T>  Any {@code class} extending the {@code Task class}
     * @param task task to add
     *
     * @return if the {@code TasksManager} did not already contain the given
     *         {@code task}
     *
     * @throws UnresolvedConflict
     *
     * @see Task
     */
    public static <T extends Task<?>> boolean addTask(T task) throws UnresolvedConflict {
        if (task == null) {
            return false;
        }
        if (task instanceof FMTask) {
            removeIfNullParent((FMTask) task);
            return fmTasks.add((FMTask) task);
        }
        WFTask<?> wfTask = (WFTask<?>) task;
        String taskName = wfTask.getName();
        // applying filters
        if (!removeIfEmptyRef(wfTask) && removeIfPrioritizeConcrete(wfTask)) {
            // we have to update the corresponding FMTask's abstract status
            //TODO : never used...
            Optional<FMTask> optFmTask = getFMTaskWithName(taskName);
            if (optFmTask.isPresent()) {
                FMTask fmTask = optFmTask.get();
                log.info("Changing {}'s abstract status from true to false", fmTask);
                fmTask.setAbstract(false);
            }
        }
        // detecting and resolving conflicts
        Optional<WFTask<?>> optExistingWFTask = getWFTaskWithName(taskName);
        if (optExistingWFTask.isPresent()) {
            wfTask = processConflicts(optExistingWFTask.get(), wfTask);
        }
        /*-Optional<FMTask> optExistingFMTask = getFMTaskWithName(taskName);
        if (optExistingFMTask.isPresent()) {
            wfTask = processConflicts(optExistingFMTask.get(), wfTask);
        }*/
        return wfTasks.add(wfTask);
    }

    // conflicts

    /**
     * Detects and solves possible conflicts using the {@link #conflictManager}.
     *
     * @param taskA existing task
     * @param taskB new task
     *
     * @return the rectified new task
     *
     * @throws UnresolvedConflict
     *
     * @see ConflictsManager
     */
    private static <T extends Task<?>> T processConflicts(T taskA, T taskB) throws UnresolvedConflict {
        if (conflictManager.areInConflict(taskA, taskB)) {
            taskB = conflictManager.solve(taskA, taskB);
            if (taskA instanceof FMTask) {
                fmTasks.remove(taskA);
            } else {
                wfTasks.remove(taskA);
                getFMTaskWithName(taskB.getName()).ifPresent(TasksManager::removeTask);
            }
        }
        return taskB;
    }

    // filter

    /**
     * Removes any task from the {@link #fmTasks} collection that :
     *
     * <p>
     *
     * <ul>
     * <li>is equals to the given {@code WFTask},</li>
     * <li>has a {@code null} parent</b>.</li>
     * </ul>
     *
     * @param task task to compare
     *
     * @return if any elements were removed
     *
     * @see WFTask
     */
    public static boolean removeIfNullParent(FMTask task) {
        return fmTasks.removeIf(t -> t.equals(task) && (t.getParent() == null));
    }

    /**
     * Removes any task from the {@link #wfTasks} collection that :
     *
     * <p>
     *
     * <ul>
     * <li>is equals to the given {@code WFTask},</li>
     * <li>contains a <b>blank reference</b>.</li>
     * </ul>
     *
     * @param task task to compare
     *
     * @return if any elements were removed
     *
     * @see WFTask
     */
    public static boolean removeIfEmptyRef(WFTask<?> task) {
        return wfTasks.removeIf(t -> t.equals(task) && (t.getReference().isBlank()));
    }

    /**
     * Removes any task from the {@link #wfTasks} collection that :
     *
     * <p>
     *
     * <ul>
     * <li>is equals to the given {@code WFTask},</li>
     * <li>matches the <b>"concrete priority" rule</b>.</li>
     * </ul>
     *
     * <p>
     *
     * <b>Note</b> that the "concrete priority" rule is defined by the issue
     * <a href=
     * "https://github.com/MireilleBF/FromMLWFConfigurationToBPMN/issues/148">#148</a>.
     *
     * @param task task to compare
     *
     * @return if any elements were removed
     *
     * @see WFTask
     */
    public static boolean removeIfPrioritizeConcrete(WFTask<?> task) {
        return wfTasks.removeIf(t -> t.equals(task) && (t.isAbstract() && !task.isAbstract()));
    }

    // remover

    /**
     * Removes the given {@code task} from the right {@code Set} according to its
     * nature.
     *
     * @param <T>  Any {@code class} extending the {@code Task class}
     * @param task task to remove
     *
     * @return whether the remove operation succeed or not
     *
     * @see Task
     */
    public static <T extends Task<?>> boolean removeTask(T task) {
        if (task == null) {
            return false;
        }
        if (!(task instanceof FMTask)) {
            return wfTasks.remove(task);
        }
        FMTask fmTask = (FMTask) task;
        // removing from its parent
        FMTask fmParent = fmTask.getParent();
        if (fmParent != null) {
            fmParent.removeChild(fmTask);
        }
        // removing from the FMTask collection
        return fmTasks.remove(fmTask);
    }

    //

    /**
     * Returns whether a task with the given {@code name} exists or not.
     *
     * @param name name of the task to verify its existence
     *
     * @return whether a task with the given {@code name} exists or not
     *
     * @see FMTask
     */
    public static boolean existsInFM(String name) {
        if (name == null) {
            return false;
        }
        return fmTasks.stream()
                .map(Task::getName)
                .anyMatch(name::equals);
    }

    /**
     * Updates the given {@code tasks}' parents.
     *
     * <p>
     *
     * This can be useful after a batch treatment where the order treatment is
     * random and thus where parents can be treated after children.
     *
     * @param tasks tasks to update
     *
     * @see FMTask
     */
    public static void updateFMParents(Collection<FMTask> tasks) {
        tasks.stream()
                .filter(t -> t.getParent() == null)
                .forEach((FMTask t) -> {
                    Optional<FMTask> opt = getFMTaskWithName(XMLManager.getNodeName(t.getNode().getParentNode()));
                    if (opt.isPresent()) {
                        t.setParent(opt.get());
                        log.debug( "{} has now a parent {}",t.getName(), t.getParent());
                    }
        });


    }

    //ADDED To MANAGED Feature that becomes concrete
    public static void updateStatus(Iterable<WFTask<?>> tasks) {
        tasks.forEach((WFTask<?> t) -> {
            Optional<FMTask> opt = getFMTaskWithName(XMLManager.getNodeName(t.getNode()));
            opt.ifPresent(fmTask -> fmTask.setAbstract(t.isAbstract()));
        });
    }

    // cleaners

    /**
     * Clears all {@code Set}s by calling the {@link #clearFMTasks()} and
     * {@link #clearWFTasks()} methods.
     */
    public static void clear() {
        clearFMTasks();
        clearWFTasks();
    }

    /**
     * Clears the {@link #fmTasks}.
     */
    public static void clearFMTasks() {
        fmTasks.clear();
    }

    /**
     * Clears the {@link #wfTasks}.
     */
    public static void clearWFTasks() {
        wfTasks.clear();
    }
}
