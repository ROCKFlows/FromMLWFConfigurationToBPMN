package com.ml2wf.tasks.manager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.w3c.dom.Node;

import com.ml2wf.tasks.BPMNTask;
import com.ml2wf.tasks.FMTask;
import com.ml2wf.tasks.Task;
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
 * @version 1.0
 *
 * @see Task
 * @see FMTask
 * @see BPMNTask
 *
 */
public final class TasksManager {

	// TODO: use FMTask or Task for generic type ?

	/**
	 * {@code Set} containing all {@code FMTask}.
	 */
	private static Set<FMTask> fmTasks = new HashSet<>();
	/**
	 * {@code Set} containing all {@code BPMNTask}.
	 */
	private static Set<BPMNTask> bpmnTasks = new HashSet<>();

	/**
	 * {@code TasksManager}'s default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is private due to the static nature of this
	 * class's methods.
	 */
	private TasksManager() {

	}

	// getters

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

	public static Optional<FMTask> getFMTaskWithName(String name) {
		return fmTasks.stream().filter(t -> t.getName().equals(name)).findFirst();
	}

	public static List<FMTask> getFMTaskWithParent(Task parent) {
		return fmTasks.stream().filter(t -> (t.getParent() != null) && t.getParent().equals(parent))
				.collect(Collectors.toList());
	}

	public static Optional<FMTask> getFMTaskWithNode(Node node) {
		return fmTasks.stream().filter(t -> t.getNode().equals(node)).findFirst();
	}

	/**
	 * Returns all stored {@code BPMNTask}.
	 *
	 * @return all stored {@code BPMNTask}
	 *
	 * @see BPMNTask
	 *
	 * @since 1.0
	 */
	public static Set<BPMNTask> getBPMNTasks() {
		return bpmnTasks;
	}

	public static Optional<BPMNTask> getBPMNTaskWithName(String name) {
		return bpmnTasks.stream().filter(t -> t.getName().equals(name)).findFirst();
	}

	// adder

	/**
	 * Adds the given {@code task} to the right {@code Set} according to its nature
	 * and returns it.
	 *
	 * @param task task to add
	 *
	 * @return the added {@code task}
	 *
	 * @since 1.0
	 * @see Task
	 */
	public static Task addTask(Task task) {
		if (task != null) {
			if (task instanceof FMTask) {
				fmTasks.add((FMTask) task);
			} else {
				// removing if a task with the same name and a blank reference is already
				// contained to keep more precise tasks
				bpmnTasks.removeIf(
						t -> (t.getName() != null) && t.getName().equals(task.getName()) && t.getReference().isBlank());
				bpmnTasks.add((BPMNTask) task);
			}
		}
		return task;
	}

	// remover

	/**
	 * Removes the given {@code task} from the right {@code Set} according to its
	 * nature.
	 *
	 * @param task task to remove
	 * @return whether the remove operation succeed or not
	 *
	 * @since 1.0
	 * @see Task
	 */
	public static boolean removeTask(Task task) {
		if (task == null) {
			return false;
		}
		if (task instanceof FMTask) {
			return fmTasks.remove(task);
		} else {
			return bpmnTasks.remove(task);
		}
	}

	//

	/**
	 * Returns whether a task with the given {@code name} exists or not.
	 *
	 * @param name name of the task to verify its existence
	 * @return whether a task with the given {@code name} exists or not
	 *
	 * @since 1.0
	 * @see FMTask
	 */
	public static boolean existsinFM(String name) {
		return fmTasks.stream().map(Task::getName).anyMatch(n -> n.equals(name));
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
	 * @since 1.0
	 * @see FMTask
	 */
	public static void updateFMParents(Set<FMTask> tasks) {
		tasks.stream().filter(t -> t.getParent() == null).forEach(t -> {
			Optional<FMTask> opt = TasksManager.getFMTaskWithName(XMLManager.getNodeName(t.getNode().getParentNode()));
			if (opt.isPresent()) {
				t.setParent(opt.get());
			}
		});
	}

	// clearers

	/**
	 * Clears all {@code Set}s by calling the {@link #clearFMTasks()} and
	 * {@link #clearBPMNTasks()} methods.
	 *
	 * @since 1.0
	 */
	public static void clear() {
		clearFMTasks();
		clearBPMNTasks();
	}

	/**
	 * Clears the {@link #fmTasks}.
	 *
	 * @since 1.0
	 */
	public static void clearFMTasks() {
		fmTasks.clear();
	}

	/**
	 * Clears the {@link #bpmnTasks}.
	 *
	 * @since 1.0
	 */
	public static void clearBPMNTasks() {
		bpmnTasks.clear();
	}

}
