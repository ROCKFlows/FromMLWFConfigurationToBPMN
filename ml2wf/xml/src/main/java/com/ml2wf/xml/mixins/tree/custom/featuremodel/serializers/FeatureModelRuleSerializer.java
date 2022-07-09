package com.ml2wf.xml.mixins.tree.custom.featuremodel.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ml2wf.core.constraints.operators.AbstractOperator;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelRule;

import java.io.IOException;

public class FeatureModelRuleSerializer extends StdSerializer<FeatureModelRule> {

    public FeatureModelRuleSerializer() {
        super(FeatureModelRule.class);
    }

    @Override
    public void serialize(FeatureModelRule rule, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        if (rule.getDescription() != null && !rule.getDescription().getContent().isBlank()) {
            gen.writeObjectField("description", rule.getDescription());
        }
        AbstractOperator operator = rule.getConstraint().getOperator();
        gen.writeObject(operator);
        gen.writeEndObject();
    }
}
