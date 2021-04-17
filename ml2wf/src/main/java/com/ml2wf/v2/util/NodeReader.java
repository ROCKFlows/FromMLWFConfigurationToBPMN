package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Node;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public final class NodeReader {

    // TODO: add logs
    // TODO: store strings into constants

    public static Optional<String> getAttribute(Node node, String attributeName) {
        // TODO: create custom exception
        if (!node.hasAttributes()) {
            log.warn("Can't read node attribute (no attributes) for node [{}].",
                    NodeDescriber.getNodeDescription(node));
            return Optional.empty();
        }
        Node nameNode = node.getAttributes().getNamedItem(attributeName);
        if (nameNode != null) {
            return Optional.of(nameNode.getNodeValue());
        }
        log.warn("Can't read node attribute (no named item \"{}\") for node [{}].",
                attributeName, NodeDescriber.getNodeDescription(node));
        return Optional.empty();
    }

    public static boolean hasAttribute(Node node, String attributeName) {
        return node.hasAttributes() && node.getAttributes().getNamedItem(attributeName) != null;
    }

    public static String getName(Node node) {
        return getAttribute(node, "name")
                .orElseThrow(() -> new RuntimeException("Unable to retrieve node name for node : " +
                        NodeDescriber.getNodeDescription(node)));
    }

    public static boolean isAbstract(Node node) {
        // TODO: manage invalid boolean
        return Boolean.parseBoolean(getAttribute(node, "abstract")
                .orElse("false"));
    }
}
