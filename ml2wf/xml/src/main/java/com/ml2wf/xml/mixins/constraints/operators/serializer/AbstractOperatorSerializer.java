package com.ml2wf.xml.mixins.constraints.operators.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.core.constraints.operands.AbstractOperand;
import com.ml2wf.core.constraints.operators.AbstractOperator;

import java.io.IOException;

import static java.util.stream.Collectors.groupingBy;

public class AbstractOperatorSerializer extends StdSerializer<AbstractOperator> {

    public AbstractOperatorSerializer() {
        super(AbstractOperator.class);
    }

    public void xmlSerialize(AbstractOperator operator, JsonGenerator generator) throws IOException {
        generator.writeObjectFieldStart(AbstractOperator.Operators.getShortNameForClass(operator.getClass()));
        for (AbstractOperand operand : operator.getOperands())  {
            generator.writeObject(operand);
        }
        generator.writeEndObject();
    }

    public void jsonSerialize(AbstractOperator operator, JsonGenerator generator) throws IOException {
        generator.writeObjectFieldStart(AbstractOperator.Operators.getShortNameForClass(operator.getClass()));
        operator.getOperands().stream().collect(
                groupingBy(o -> (o instanceof AbstractOperator) ?
                        AbstractOperator.Operators.getShortNameForClass((Class<? extends AbstractOperator>) o.getClass()) : // TODO: to improve
                        AbstractOperand.Operands.getShortNameForClass(o.getClass())
                )
        ).forEach((n, o) -> {
            try {
                generator.writeStartArray();
                for (AbstractOperand operand : o) {
                    generator.writeObject(operand);
                }
                generator.writeEndArray();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        generator.writeEndObject();
    }

    @Override
    public void serialize(AbstractOperator operator, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (gen instanceof ToXmlGenerator) {
            xmlSerialize(operator, gen);
        } else {
            jsonSerialize(operator, gen);
        }
    }
}
