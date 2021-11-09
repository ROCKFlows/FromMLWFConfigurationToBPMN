package com.ml2wf.v2.xml.constraints.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v2.xml.constraints.serializer.VariableOperandSerializer;

@JsonSerialize(using = VariableOperandSerializer.class)
public interface VariableOperandMixin {

}
