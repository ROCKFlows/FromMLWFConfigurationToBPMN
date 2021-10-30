package com.ml2wf.conflicts;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.tasks.base.Task;

public class ConflictsManager implements ConflictSolver {

    // TODO: check composite DP

    private final List<ConflictSolver> solvers = new ArrayList<>();

    public ConflictsManager() {
        this.initSolvers();
    }

    private void initSolvers() {
        this.solvers.add(new DifferentParentsSolver());
    }

    @Override
    public <T extends Task<?>> T solve(T taskA, T taskB) throws UnresolvedConflict {
        for (ConflictSolver conflictSolver : this.solvers) {
            if (conflictSolver.areInConflict(taskA, taskB)) {
                taskB = conflictSolver.solve(taskA, taskB);
            }
        }
        return taskB;
    }

    @Override
    public <T extends Task<?>> boolean areInConflict(T taskA, T taskB) {
        for (ConflictSolver conflictSolver : this.solvers) {
            if (conflictSolver.areInConflict(taskA, taskB)) {
                return true;
            }
        }
        return false;
    }
}
