package com.ml2wf.conflicts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

public class DifferentParentsSolver<T extends WFTask<?>> implements ConflictSolver<T> {

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
	public T solve(FMTask fmTask, T wfTask) throws InvalidTaskException, UnresolvedConflict {
		if (!this.areInConflict(fmTask, wfTask)) {
			return wfTask;
		}
		logger.warn("Conflict detected implying {} (in FM) and {} (in WF) : They have different parents.", fmTask,
				wfTask);
		throw new UnresolvedConflict(
				String.format("Can't find a solution for %s and %s having different parents.", fmTask, wfTask));
	}

	@Override
	public boolean areInConflict(FMTask fmTask, T wfTask) throws InvalidTaskException {
		this.checkRequirements(fmTask, wfTask);
		return !fmTask.getParent().getName().equals(wfTask.getReference());
	}

	private void checkRequirements(FMTask fmTask, T wfTask) throws InvalidTaskException {
		if (fmTask.getParent() == null) {
			throw new InvalidTaskException(
					String.format("Can't solve a conflict involving a FMTask (%s) without parent.", fmTask));
		}
		if (wfTask.getReference().isBlank()) {
			throw new InvalidTaskException(
					String.format("Can't solve a conflict involving a WFTask (%s) without reference.", wfTask));
		}
	}

}
