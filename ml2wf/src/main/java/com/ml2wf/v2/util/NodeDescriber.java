package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NodeDescriber {

    private static final String FORMAT = "name=%s,tag=%s,document=%s,nb_children=%d";

    public static String getNodeDescription(Node node) {
        String name = NodeReader.hasAttribute(node, "name") ? NodeReader.getName(node) : "NO NAME";
        return String.format(FORMAT, name, node.getNodeName(),
                // nb_children=children / 2 because we don't count #text as children
                node.getOwnerDocument().hashCode(), node.getChildNodes().getLength() / 2);
    }
}
