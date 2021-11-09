package com.ml2wf.v2.xml.constraints.serializer;

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

    @Override
    public void serialize(VariableOperand operand, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (!(gen instanceof ToXmlGenerator)) {
            throw new IllegalStateException("Serialization only support XML format.");
        }
        ToXmlGenerator generator = (ToXmlGenerator) gen;
        generator.writeStringField("var", operand.getValue());
    }
}
