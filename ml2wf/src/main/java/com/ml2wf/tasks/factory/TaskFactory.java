package com.ml2wf.tasks.factory;

import java.util.Set;

import org.w3c.dom.Node;

import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;

/**
 * This interface provides a method for the <b>creation of {@code Task}'s
 * instances</b>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Task
 */
public interface TaskFactory {

	/**
	 * Creates and returns a {@code Set<Task>} containing all created tasks
	 * corresponding to the given {@code node}.
	 *
	 * @param node {@code Node} to convert to {@code Task}
	 * @return the {@code Set<Task>} containing all created {@code Task}
	 *
	 * @since 1.0
	 * @see Task
	 * @see Node
	 */
	public Set<Task> createTasks(Node node);

	/**
	 * Converts a {@code WFTask} to a {@code FMTask}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method uses the {@link FMTask#FMTask(WFTask)}
	 * constructor for the convertion.
	 *
	 * @param task task to convert
	 * @return the converted task
	 */
	public FMTask convertWFtoFMTask(WFTask task);
}