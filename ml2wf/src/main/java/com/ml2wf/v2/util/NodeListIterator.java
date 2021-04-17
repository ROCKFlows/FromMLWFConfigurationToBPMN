package com.ml2wf.v2.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class NodeListIterator implements Iterator<Node> {

    private final NodeList nodeList;
    private int index;

    public NodeListIterator(final NodeList nodeList) {
        this.nodeList = nodeList;
    }

    public NodeListIterator(Document document, String tag) {
        this(document.getElementsByTagName(tag));
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
