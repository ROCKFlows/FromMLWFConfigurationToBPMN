package com.ml2wf.v3.app.constraints.operators;

import com.ml2wf.v3.app.workflow.StandardWorkflow;

public interface ConsistencyChecker {
    boolean isWorkflowConsistent(StandardWorkflow workflow);
}
