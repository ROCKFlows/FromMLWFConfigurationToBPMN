package com.ml2wf.v2.constraints.operators.xml.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v2.constraints.operators.xml.serializer.AbstractOperatorSerializer;

@JsonSerialize(using = AbstractOperatorSerializer.class)
public interface AbstractOperatorMixin {

}
