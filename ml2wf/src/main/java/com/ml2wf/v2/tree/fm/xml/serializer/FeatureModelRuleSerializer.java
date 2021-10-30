package com.ml2wf.v2.tree.fm.xml.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.v2.constraints.operators.AbstractOperator;
import com.ml2wf.v2.tree.fm.FeatureModelRule;

import java.io.IOException;

public class FeatureModelRuleSerializer extends StdSerializer<FeatureModelRule> {

    public FeatureModelRuleSerializer() {
        super(FeatureModelRule.class);
    }

    @Override
    public void serialize(FeatureModelRule rule, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (!(gen instanceof ToXmlGenerator)) {
            throw new IllegalStateException("Serialization only support XML format.");
        }
        ToXmlGenerator generator = (ToXmlGenerator) gen;
        generator.writeStartObject();
        if (rule.getDescription() != null && !rule.getDescription().getContent().isBlank()) {
            generator.writeObjectField("description", rule.getDescription());
        }
        AbstractOperator operator = rule.getConstraint().getOperator();
        generator.writeObject(operator);
        generator.writeEndObject();
    }
}
