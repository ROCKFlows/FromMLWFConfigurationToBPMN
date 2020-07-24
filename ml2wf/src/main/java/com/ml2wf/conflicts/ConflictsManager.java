package com.ml2wf.conflicts;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

public class ConflictsManager<T extends WFTask<?>> implements ConflictSolver<T> {

	// TODO: check composite DP

	private List<ConflictSolver<T>> solvers = new ArrayList<>();

	public ConflictsManager() {
		this.initSolvers();
	}

	private void initSolvers() {
		this.solvers.add(new DifferentParentsSolver<>());
	}

	@Override
	public T solve(T taskA, T taskB) throws InvalidTaskException, UnresolvedConflict {
		for (ConflictSolver<T> conflictSolver : this.solvers) {
			if (conflictSolver.areInConflict(taskA, taskB)) {
				taskB = conflictSolver.solve(taskA, taskB);
			}
		}
		return taskB;
	}

	@Override
	public boolean areInConflict(T taskA, T taskB) throws InvalidTaskException {
		for (ConflictSolver<T> conflictSolver : this.solvers) {
			if (conflictSolver.areInConflict(taskA, taskB)) {
				return true;
			}
		}
		return false;
	}

}
