package com.ml2wf.v2.constraints.operators.impl;

import com.ml2wf.v2.constraints.operands.AbstractOperand;
import com.ml2wf.v2.constraints.operators.AbstractOperator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImplicationOperator extends AbstractOperator {

    public ImplicationOperator(final List<AbstractOperand> operands) {
        super(operands);
    }
}
