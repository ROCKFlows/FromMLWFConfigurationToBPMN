package com.ml2wf.conflicts;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;

public interface ConflictSolver {

	<T extends Task<?>> T solve(T taskA, T taskB) throws UnresolvedConflict;

	<T extends Task<?>> boolean areInConflict(T taskA, T taskB);
}
