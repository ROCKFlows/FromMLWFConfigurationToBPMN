package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NodeDescriber {

    private static final String FORMAT = "name=%s,tag=%s,document=%s,nb_children=%d";

    public static String getNodeDescription(Node node) {
        // TODO: change "TODO" for name
        return String.format(FORMAT, "TODO", node.getNodeName(),
                node.getOwnerDocument(), node.getChildNodes().getLength());
    }
}
