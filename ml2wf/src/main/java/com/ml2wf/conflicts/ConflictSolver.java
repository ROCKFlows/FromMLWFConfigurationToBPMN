package com.ml2wf.conflicts;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

public interface ConflictSolver<T extends Task<?>> {

	public T solve(T taskA, T taskB) throws InvalidTaskException, UnresolvedConflict;

	public boolean areInConflict(T taskA, T taskB) throws InvalidTaskException;

}
