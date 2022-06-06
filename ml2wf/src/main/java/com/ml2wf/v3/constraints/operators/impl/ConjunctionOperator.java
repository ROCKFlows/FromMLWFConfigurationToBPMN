package com.ml2wf.v3.constraints.operators.impl;

import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.constraints.operators.AbstractOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConjunctionOperator extends AbstractOperator {

    public ConjunctionOperator(final List<AbstractOperand> operands) {
        super(operands);
    }
}
