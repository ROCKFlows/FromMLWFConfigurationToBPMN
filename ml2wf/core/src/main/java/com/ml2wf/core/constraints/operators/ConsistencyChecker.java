package com.ml2wf.core.constraints.operators;

import com.ml2wf.core.workflow.StandardWorkflow;

public interface ConsistencyChecker {
    boolean isWorkflowConsistent(StandardWorkflow workflow);
}
