package com.ml2wf.v2.xml.constraints.operators.mixins;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v2.xml.constraints.operators.serializer.AbstractOperatorSerializer;

@JsonSerialize(using = AbstractOperatorSerializer.class)
public interface AbstractOperatorMixin {

}
