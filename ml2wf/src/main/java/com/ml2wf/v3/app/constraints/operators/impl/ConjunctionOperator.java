package com.ml2wf.v3.app.constraints.operators.impl;

import com.ml2wf.v3.app.workflow.StandardWorkflow;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import com.ml2wf.v3.app.constraints.operators.AbstractOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConjunctionOperator extends AbstractOperator {

    public ConjunctionOperator(final List<AbstractOperand> operands) {
        super(operands);
    }

    @Override
    public boolean isWorkflowConsistent(StandardWorkflow workflow) {
        return operands.stream().allMatch(o -> o.isWorkflowConsistent(workflow));
    }

    @Override
    public String toString() {
        return String.format("(%s)", operands.stream().map(Object::toString).collect(Collectors.joining(" ∧ ")));
    }
}