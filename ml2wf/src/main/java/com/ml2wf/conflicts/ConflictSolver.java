package com.ml2wf.conflicts;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.exceptions.InvalidTaskException;

public interface ConflictSolver<T extends WFTask<?>> {

	public T solve(FMTask fmTask, T wfTask) throws InvalidTaskException, UnresolvedConflict;

	public boolean areInConflict(FMTask fmTask, T wfTask) throws InvalidTaskException;

}
