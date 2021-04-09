package com.ml2wf.tasks.factory;

import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;

/**
 * This interface provides a method for the <b>creation of {@code Task}'s
 * instances</b>.
 *
 * @author Nicolas Lacroix
 *
 * @see Task
 *
 * @since 1.0.0
 */
public interface TaskFactory {

    /**
     * Creates and returns the created task corresponding to the given {@code node}.
     *
     * @param <T>  Any {@code class} implementing the {@code Spec interface}
     * @param node {@code Node} to convert to {@code Task}
     *
     * @return the created task corresponding to the given {@code node}
     *
     * @throws UnresolvedConflict
     *
     * @see Node
     */
    <T extends Task<?>> T createTask(Node node) throws UnresolvedConflict;

    /**
     * Converts a {@link WFTask} to a {@code FMTask}.
     *
     * @param task  the {@link WFTask} to convert
     *
     * @return the converted task
     *
     * @throws UnresolvedConflict
     *
     * @see WFTask
     * @see FMTask
     */
    FMTask convertWFtoFMTask(WFTask<?> task) throws UnresolvedConflict;
}
