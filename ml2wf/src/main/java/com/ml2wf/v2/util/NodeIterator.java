package com.ml2wf.v2.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NodeIterator implements Iterator<Node> {

    private final NodeList nodeList;
    private int index;

    public NodeIterator(Document document, String tag) {
        this.nodeList = document.getElementsByTagName(tag);
    }

    @Override
    public boolean hasNext() {
        return index < nodeList.getLength();
    }

    @Override
    public Node next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return nodeList.item(index++);
    }
}
