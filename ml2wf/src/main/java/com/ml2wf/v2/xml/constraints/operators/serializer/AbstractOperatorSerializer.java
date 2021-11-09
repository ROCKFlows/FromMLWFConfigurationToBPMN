package com.ml2wf.v2.xml.constraints.operators.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.v2.constraints.operands.AbstractOperand;
import com.ml2wf.v2.constraints.operators.AbstractOperator;

import java.io.IOException;

public class AbstractOperatorSerializer extends StdSerializer<AbstractOperator> {

    public AbstractOperatorSerializer() {
        super(AbstractOperator.class);
    }

    @Override
    public void serialize(AbstractOperator operator, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (!(gen instanceof ToXmlGenerator)) {
            throw new IllegalStateException("Serialization only support XML format.");
        }
        ToXmlGenerator generator = (ToXmlGenerator) gen;
        generator.writeObjectFieldStart(AbstractOperator.Operators.getShortNameForClass(operator.getClass()));
        for (AbstractOperand operand : operator.getOperands())  {
            generator.writeObject(operand);
        }
        generator.writeEndObject();
    }
}
