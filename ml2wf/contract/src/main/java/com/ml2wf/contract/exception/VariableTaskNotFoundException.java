package com.ml2wf.contract.exception;

import com.ml2wf.core.constraints.operands.impl.VariableOperand;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class VariableTaskNotFoundException extends RuntimeException {

    VariableOperand operand;
}
