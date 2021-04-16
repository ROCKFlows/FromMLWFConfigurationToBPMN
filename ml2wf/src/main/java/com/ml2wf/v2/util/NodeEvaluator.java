package com.ml2wf.v2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NodeEvaluator {

    public static boolean isUnmanaged(Node node) {
        Node parent;
        while ((parent = node.getParentNode()) != null) {
            if ("Unmanaged".equals(NodeReader.getName(parent))) {
                return true;
            }
            node = parent;
        }
        return false;
    }
}
