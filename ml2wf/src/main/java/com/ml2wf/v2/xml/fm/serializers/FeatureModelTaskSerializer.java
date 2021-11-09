package com.ml2wf.v2.tree.fm.xml.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.v2.tree.fm.FeatureModelTask;

import java.io.IOException;

public class FeatureModelTaskSerializer extends StdSerializer<FeatureModelTask> {

    public FeatureModelTaskSerializer() {
        super(FeatureModelTask.class);
    }

    @Override
    public void serialize(FeatureModelTask task, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // TODO: fix multiple documentation issues
        // TODO: add alt support
        if (!(gen instanceof ToXmlGenerator)) {
            throw new IllegalStateException("Serialization only support XML format.");
        }
        ToXmlGenerator generator = (ToXmlGenerator) gen;
        generator.writeStartObject();
        generator.setNextIsAttribute(true);
        generator.writeStringField("name", task.getName());
        generator.setNextIsAttribute(true);
        generator.writeBooleanField("abstract", task.isAbstract());
        generator.setNextIsAttribute(true);
        generator.writeBooleanField("mandatory", task.isMandatory());
        generator.setNextIsUnwrapped(true);
        for (FeatureModelTask.Description description : task.getDescriptions()) {
            generator.writeObjectField("description", description);
        }
        for (FeatureModelTask child : task.getChildren()) {
            generator.writeObjectField((child.hasChildren()) ? "and" : "feature", child);
        }
        generator.writeEndObject();
    }
}
