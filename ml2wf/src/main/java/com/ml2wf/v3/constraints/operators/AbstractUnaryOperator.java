package com.ml2wf.v3.constraints.operators;

import com.ml2wf.v3.constraints.operands.AbstractOperand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractUnaryOperator extends AbstractOperator {

    protected AbstractUnaryOperator(final AbstractOperand operand) {
        super(Collections.singletonList(operand));
    }

    protected AbstractUnaryOperator(final List<AbstractOperand> singleOperandList) {
        super(Collections.singletonList(singleOperandList.get(0)));
    }
}
