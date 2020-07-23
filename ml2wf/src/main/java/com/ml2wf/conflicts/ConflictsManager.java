package com.ml2wf.conflicts;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
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
	public T solve(FMTask fmTask, T wfTask) throws InvalidTaskException, UnresolvedConflict {
		for (ConflictSolver<T> conflictSolver : this.solvers) {
			if (conflictSolver.areInConflict(fmTask, wfTask)) {
				wfTask = conflictSolver.solve(fmTask, wfTask);
			}
		}
		return wfTask;
	}

	@Override
	public boolean areInConflict(FMTask fmTask, T wfTask) throws InvalidTaskException {
		for (ConflictSolver<T> conflictSolver : this.solvers) {
			if (conflictSolver.areInConflict(fmTask, wfTask)) {
				return true;
			}
		}
		return false;
	}

}
