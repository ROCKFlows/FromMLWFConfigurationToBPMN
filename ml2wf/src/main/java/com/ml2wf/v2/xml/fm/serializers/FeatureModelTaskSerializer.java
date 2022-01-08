package com.ml2wf.v2.xml.fm.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.ml2wf.v2.tree.fm.FeatureModelTask;

import java.io.IOException;

import static java.util.stream.Collectors.groupingBy;

public class FeatureModelTaskSerializer extends StdSerializer<FeatureModelTask> {

    public FeatureModelTaskSerializer() {
        super(FeatureModelTask.class);
    }

    public void xmlSerialize(FeatureModelTask task, ToXmlGenerator generator) throws IOException {
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

    public void jsonSerialize(FeatureModelTask task, JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("_name", task.getName());
        generator.writeBooleanField("_abstract", task.isAbstract());
        generator.writeBooleanField("_mandatory", task.isMandatory());
        for (FeatureModelTask.Description description : task.getDescriptions()) {
            generator.writeObjectField("description", description);
        }
        task.getChildren().stream().collect(
                groupingBy(c -> c.hasChildren() ? "ands" : "features") // TODO , mapping(c -> c, toList()))
        ).forEach((n, c) -> {
            try {
                generator.writeArrayFieldStart(n);
                for (FeatureModelTask child : c) {
                    generator.writeObject(child);
                }
                generator.writeEndArray();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
        generator.writeEndObject();
    }

    @Override
    public void serialize(FeatureModelTask task, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // TODO: fix multiple documentation issues
        // TODO: add alt support
        if (gen instanceof ToXmlGenerator) {
            xmlSerialize(task, (ToXmlGenerator) gen);
        } else {
            jsonSerialize(task, gen);
        }
    }
}
