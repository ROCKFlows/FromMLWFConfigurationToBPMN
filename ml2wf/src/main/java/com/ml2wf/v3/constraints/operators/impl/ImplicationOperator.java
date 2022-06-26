package com.ml2wf.v3.constraints.operators.impl;

import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.constraints.operators.AbstractOperator;
import com.ml2wf.v3.workflow.StandardWorkflow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImplicationOperator extends AbstractOperator {

    public ImplicationOperator(final List<AbstractOperand> operands) {
        super(operands);
    }

    @Override
    public boolean isWorkflowConsistent(StandardWorkflow workflow) {
        if (operands.get(0).isWorkflowConsistent(workflow)) {
            return operands.subList(1, operands.size()).stream().allMatch(o -> o.isWorkflowConsistent(workflow));
        }
        return true;
    }
}
