package com.ml2wf.v2.constraints.operands.impl;

import com.ml2wf.v2.constraints.operands.AbstractOperand;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableOperand implements AbstractOperand {

    protected String value;
}
