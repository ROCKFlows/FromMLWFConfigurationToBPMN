package com.ml2wf.core.constraints.operators;

import com.ml2wf.core.constraints.operands.AbstractOperand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractUnaryOperator extends AbstractOperator {

    protected AbstractUnaryOperator(final AbstractOperand operand, String operandName) {
        super(Collections.singletonList(operand), operandName);
    }

    protected AbstractUnaryOperator(final List<AbstractOperand> singleOperandList, String operandName) {
        super(Collections.singletonList(singleOperandList.get(0)), operandName);
    }
}
