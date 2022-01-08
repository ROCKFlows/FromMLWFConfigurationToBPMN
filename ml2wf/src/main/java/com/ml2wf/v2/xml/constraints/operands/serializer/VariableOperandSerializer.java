package com.ml2wf.v2.xml.constraints.operands.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.v2.constraints.operands.impl.VariableOperand;

import java.io.IOException;

public class VariableOperandSerializer extends StdSerializer<VariableOperand> {

    public VariableOperandSerializer() {
        super(VariableOperand.class);
    }

    public void xmlSerialize(VariableOperand operand, JsonGenerator generator) throws IOException {
        generator.writeStringField("var", operand.getValue());
    }

    public void jsonSerialize(VariableOperand operand, JsonGenerator generator) throws IOException {
        generator.writeString(operand.getValue());
    }

    @Override
    public void serialize(VariableOperand operand, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (gen instanceof ToXmlGenerator) {
            xmlSerialize(operand, gen);
        } else {
            jsonSerialize(operand, gen);
        }
    }
}
