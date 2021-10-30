package com.ml2wf.v2.constraints.operands.xml.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v2.constraints.operands.xml.serializer.VariableOperandSerializer;

@JsonSerialize(using = VariableOperandSerializer.class)
public interface VariableOperandMixin {

}
