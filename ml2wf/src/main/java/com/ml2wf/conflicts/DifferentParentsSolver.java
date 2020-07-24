package com.ml2wf.conflicts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

public class DifferentParentsSolver<T extends WFTask<?>> implements ConflictSolver<T> {

	private static final String NO_REF_ERROR = "Can't resolve a conflict involving a WFTask (%s) without reference.";
	private static final String NO_SOLUTION_ERROR = "Can't find a solution for %s (parent=%s) and %s (parent=%s) having different parents.";

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(DifferentParentsSolver.class);

	public DifferentParentsSolver() {
		// empty constructor
	}

	@Override
	public T solve(T taskA, T taskB) throws InvalidTaskException, UnresolvedConflict {
		this.checkRequirements(taskA, taskB);
		logger.warn("Conflict detected implying {} (parent={}) and {} (parent={}) : They have different parents.",
				taskA, taskA.getReference(), taskB, taskB.getReference());
		// T1 + T2 + T1#T2 => T1#T2
		if (taskA.getReference().equals(WFMetaMerger.STEP_TASK)) {
			logger.warn("Keeping {} (parent={}).", taskB, taskB.getReference());
			return taskB;
		}
		if (taskB.getReference().equals(WFMetaMerger.STEP_TASK)) {
			logger.warn("Keeping {} (parent={}).", taskA, taskA.getReference());
			return taskA;
		}
		// ---
		throw new UnresolvedConflict(
				String.format(NO_SOLUTION_ERROR, taskA, taskA.getReference(), taskB, taskB.getReference()));
	}

	@Override
	public boolean areInConflict(T taskA, T taskB) throws InvalidTaskException {
		this.checkRequirements(taskA, taskB);
		return !taskA.getReference().equals(taskB.getReference());
	}

	private void checkRequirements(T taskA, T taskB) throws InvalidTaskException {
		String taskARef = taskA.getReference();
		if ((taskARef == null) || taskARef.isBlank()) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskA));
		}
		String taskBRef = taskB.getReference();
		if ((taskBRef == null) || taskBRef.isBlank()) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskB));
		}
	}

}
