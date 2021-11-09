package com.ml2wf.v2.xml.constraints.operands.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v2.xml.constraints.operands.serializer.VariableOperandSerializer;

@JsonSerialize(using = VariableOperandSerializer.class)
public interface VariableOperandMixin {

}
