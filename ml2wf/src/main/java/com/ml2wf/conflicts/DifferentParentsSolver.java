package com.ml2wf.conflicts;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

@NoArgsConstructor
@Log4j2
public class DifferentParentsSolver implements ConflictSolver {

	private static final String NO_REF_ERROR = "Can't resolve a conflict involving a WFTask (%s) without reference.";
	private static final String NO_SOLUTION_ERROR = "Can't find a solution for %s (parent=%s) and %s (parent=%s) having different parents.";

	@Override
	public <T extends Task<?>> T solve(T taskA, T taskB) throws UnresolvedConflict {
		checkRequirements(taskA, taskB);
		String refA = getRefName(taskA);
		String refB = getRefName(taskB);
		log.warn("Conflict detected implying {} (parent={}) and {} (parent={}) : They have different parents.",
				taskA, refA, taskB, refB);
		// T1 + T2 + T1#T2 => T1#T2
		if (refA.equals(WFMetaMerger.STEP_TASK)) {
			log.warn("Keeping {} (parent={}).", taskB, refB);
			return taskB;
		}
		if (refB.equals(WFMetaMerger.STEP_TASK)) {
			log.warn("Keeping {} (parent={}).", taskA, refA);
			return taskA;
		}
		// ---
		throw new UnresolvedConflict(
				String.format(NO_SOLUTION_ERROR, taskA, refA, taskB, refB));
	}

	@Override
	public <T extends Task<?>> boolean areInConflict(T taskA, T taskB) {
		checkRequirements(taskA, taskB);
		return !getRefName(taskA).equals(getRefName(taskB));
	}

	private static <T extends Task<?>> void checkRequirements(T taskA, T taskB) {
		if (taskA == null) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskA));
		}
		if (taskB == null) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskB));
		}
		String ref = getRefName(taskA);
		if ((ref == null) || ref.isBlank()) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskA));
		}
		ref = getRefName(taskB);
		if ((ref == null) || ref.isBlank()) {
			throw new InvalidTaskException(
					String.format(NO_REF_ERROR, taskB));
		}
	}

	private static <T extends Task<?>> String getRefName(T task) {
		if (task instanceof FMTask) {
			FMTask fmTask = (FMTask) task;
			if (fmTask.getParent() == null) {
				return "";
			}
			return fmTask.getParent().getName();
		}
		return ((WFTask<?>) task).getReference();
	}
}
