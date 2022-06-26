package com.ml2wf.v3.constraints.operators;

import com.ml2wf.v3.workflow.StandardWorkflow;

public interface ConsistencyChecker {
    boolean isWorkflowConsistent(StandardWorkflow workflow);
}
