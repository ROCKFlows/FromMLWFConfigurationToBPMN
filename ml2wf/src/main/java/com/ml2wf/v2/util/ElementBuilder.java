package com.ml2wf.v2.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class ElementBuilder {

    private final Element element;

    private ElementBuilder(Document document, String tagName) {
        this.element = document.createElement(tagName);
    }

    public static ElementBuilder builder(Document document, String tagName) {
        return new ElementBuilder(document, tagName);
    }

    public ElementBuilder setAttribute(String name, String value) {
        element.setAttribute(name, value);
        return this;
    }

    public ElementBuilder setIdAttribute(String name, boolean isId) {
        element.setIdAttribute(name, isId);
        return this;
    }

    public ElementBuilder setTextContent(String textContent) {
        element.setTextContent(textContent);
        return this;
    }

    public Element build() {
        // TODO: close this builder
        return element;
    }
}
