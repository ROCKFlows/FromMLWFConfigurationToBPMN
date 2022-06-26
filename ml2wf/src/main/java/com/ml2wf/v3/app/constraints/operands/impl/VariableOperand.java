package com.ml2wf.v3.app.constraints.operands.impl;

import com.ml2wf.v3.app.workflow.StandardWorkflow;
import com.ml2wf.v3.app.workflow.StandardWorkflowTask;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableOperand implements AbstractOperand {

    protected String value;

    private boolean isConsistent(StandardWorkflowTask task) {
        return task.getName().equals(value);
    }

    @Override
    public boolean isWorkflowConsistent(StandardWorkflow workflow) {
        return workflow.getTasks().stream().anyMatch(this::isConsistent);
    }

    @Override
    public String toString() {
        return value;
    }
}
