package com.ml2wf.v2.tree.fm.xml.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.ml2wf.v2.tree.fm.FeatureModelTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FeatureModelTaskDeserializer extends StdDeserializer<FeatureModelTask> {

    public FeatureModelTaskDeserializer() {
        super(FeatureModelTask.class);
    }

    protected FeatureModelTaskDeserializer(Class<?> vc) {
        super(vc);
    }

    // TODO: check default value for abstract/mandatory

    @Override
    public FeatureModelTask deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        // TODO: add alt support
        System.out.println("deserialize");
        JsonNode node = Objects.requireNonNull(jsonParser.getCodec().readTree(jsonParser));
        return FeatureModelTask.builder()
                .name(getName(node))
                .isAbstract(isAbstract(node))
                .isMandatory(isMandatory(node))
                .descriptions(getDescriptions(jsonParser, node))
                .children(getChildren(jsonParser, node))
                .build();
    }

    private static String getName(JsonNode node) {
        return Objects.requireNonNull(node.get("name")).asText();
    }

    private static boolean isAbstract(JsonNode node) {
        JsonNode abstractNode = node.get("abstract");
        return abstractNode != null && abstractNode.asBoolean(false);
    }

    private static boolean isMandatory(JsonNode node) {
        JsonNode mandatoryNode = node.get("mandatory");
        return mandatoryNode != null && mandatoryNode.asBoolean(false);
    }

    private static List<FeatureModelTask.Description> getDescriptions(JsonParser jsonParser, JsonNode node)
            throws JsonProcessingException {
        List<FeatureModelTask.Description> descriptions = new ArrayList<>();
        JsonNode descriptionNodes = node.get("description");
        if (descriptionNodes == null || descriptionNodes.size() == 0) {
            return descriptions;
        }
        for (JsonNode descriptionNode : descriptionNodes) {
            descriptions.add(jsonParser.getCodec().treeToValue(descriptionNode, FeatureModelTask.Description.class));
        }
        return descriptions;
    }

    private static void mergeChildren(ArrayNode childrenNodes, JsonNode newChildren) {
        if (newChildren == null) {
            return;
        }
        if (newChildren.isArray()) {
            for (JsonNode newChild : newChildren) {
                assert !newChild.isArray(); // TODO: change this assert once all fixed
                childrenNodes.insert(childrenNodes.size(), newChild);
            }
        } else {
            childrenNodes.insert(childrenNodes.size(), newChildren);
        }
    }

    private static ArrayNode groupAllChildren(JsonNode node) {
        var childrenNodes = new ArrayNode(JsonNodeFactory.instance);
        mergeChildren(childrenNodes, node.get("and"));
        mergeChildren(childrenNodes, node.get("feature"));
        mergeChildren(childrenNodes, node.get("alt"));
        return childrenNodes;
    }

    private static List<FeatureModelTask> getChildren(JsonParser jsonParser, JsonNode node)
            throws JsonProcessingException {
        List<FeatureModelTask> childrenTasks = new ArrayList<>();
        ArrayNode childrenNodes = groupAllChildren(node);
        if (childrenNodes.size() == 0) {
            return childrenTasks;
        }
        for (JsonNode childrenNode : childrenNodes) {
            childrenTasks.add(jsonParser.getCodec().treeToValue(childrenNode, FeatureModelTask.class));
        }
        return childrenTasks;
    }
}
