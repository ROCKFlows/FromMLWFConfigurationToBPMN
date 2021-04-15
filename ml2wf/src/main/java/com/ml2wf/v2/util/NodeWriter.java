package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NodeWriter {

    // TODO: add logs
    // TODO: store string literals into constants

    public static void writeAttribute(Node node, String attributeName, String attributeValue) {
        // TODO: create custom exception
        if (!node.hasAttributes()) {
            throw new RuntimeException("Can't write node attribute \"" + attributeName + "\" (no attributes).");
        }
        Node nameNode = node.getAttributes().getNamedItem(attributeName);
        if (nameNode != null) {
            nameNode.setNodeValue(attributeValue);
            return;
        }
        Attr newAttribute = node.getOwnerDocument().createAttribute(attributeName);
        newAttribute.setValue(attributeValue);
        node.getAttributes().setNamedItem(newAttribute);
    }

    public static void writeName(Node node, String value) {
        writeAttribute(node, "name", value);
    }

    public static void writeAbstractStatus(Node node, boolean isAbstract) {
        writeAttribute(node, "abstract", String.valueOf(isAbstract));
    }

    public static void writeTag(Node node, String newTag) {
        node.getOwnerDocument().renameNode(node, null, newTag);
    }
}
