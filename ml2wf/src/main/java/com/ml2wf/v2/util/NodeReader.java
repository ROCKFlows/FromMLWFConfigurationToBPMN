package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NodeReader {

    // TODO: add logs
    // TODO: store string literals into constants

    public static String getAttribute(Node node, String attributeName) {
        // TODO: create custom exception
        if (!node.hasAttributes()) {
            throw new RuntimeException("Can't read node name (no attributes).");
        }
        Node nameNode = node.getAttributes().getNamedItem(attributeName);
        if (nameNode != null) {
            return nameNode.getNodeValue();
        }
        // TODO: create custom exception
        throw new RuntimeException("Can't read node name (no named item \"" + attributeName + "\").");
    }

    public static String getName(Node node) {
        return getAttribute(node, "name");
    }

    public static boolean isAbstract(Node node) {
        // TODO: manage invalid boolean
        return Boolean.parseBoolean(getAttribute(node, "abstract"));
    }
}
