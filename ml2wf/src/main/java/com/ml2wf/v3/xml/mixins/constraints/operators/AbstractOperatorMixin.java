package com.ml2wf.v3.xml.mixins.constraints.operators;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v3.xml.mixins.constraints.operators.serializer.AbstractOperatorSerializer;

@JsonSerialize(using = AbstractOperatorSerializer.class)
public interface AbstractOperatorMixin {

}
