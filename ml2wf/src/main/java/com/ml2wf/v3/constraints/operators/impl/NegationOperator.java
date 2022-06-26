package com.ml2wf.v3.constraints.operators.impl;

import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.constraints.operators.AbstractUnaryOperator;
import com.ml2wf.v3.workflow.StandardWorkflow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NegationOperator extends AbstractUnaryOperator {

    public NegationOperator(final AbstractOperand operand) {
        super(operand);
    }

    public NegationOperator(final List<AbstractOperand> singleOperandList) {
        super(singleOperandList);
        // this constructor only accept a single valued list
    }

    @Override
    public boolean isWorkflowConsistent(StandardWorkflow workflow) {
        return !operands.get(0).isWorkflowConsistent(workflow);
    }

    @Override
    public String toString() {
        return String.format("!(%s)", operands.get(0).toString());
    }
}
