package com.ml2wf.v3.app.constraints.operators.impl;

import com.ml2wf.v3.app.workflow.StandardWorkflow;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import com.ml2wf.v3.app.constraints.operators.AbstractUnaryOperator;
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
