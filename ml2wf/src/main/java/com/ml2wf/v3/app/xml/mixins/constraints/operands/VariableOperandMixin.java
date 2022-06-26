package com.ml2wf.v3.app.xml.mixins.constraints.operands;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v3.app.xml.mixins.constraints.operands.serializer.VariableOperandSerializer;

@JsonSerialize(using = VariableOperandSerializer.class)
public interface VariableOperandMixin {

}
