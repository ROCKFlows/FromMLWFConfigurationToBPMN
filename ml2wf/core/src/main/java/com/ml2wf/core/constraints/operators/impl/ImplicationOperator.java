package com.ml2wf.core.constraints.operators.impl;

import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.constraints.operands.AbstractOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        return String.format("(%s)", operands.stream().map(Object::toString).collect(Collectors.joining(" => ")));
    }
}
