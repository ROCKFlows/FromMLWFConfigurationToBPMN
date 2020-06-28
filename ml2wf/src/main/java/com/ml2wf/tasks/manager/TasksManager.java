package com.ml2wf.tasks.manager;

import java.util.HashSet;
import java.util.Set;

import com.ml2wf.tasks.BPMNTask;
import com.ml2wf.tasks.FMTask;
import com.ml2wf.tasks.Task;

/**
 * This class manages all created tasks.
 *
 * <p>
 *
 * <b>Note</b> that it uses {@code Set} to store created tasks.
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

	// TODO: use FMTask or Task for generic type

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
