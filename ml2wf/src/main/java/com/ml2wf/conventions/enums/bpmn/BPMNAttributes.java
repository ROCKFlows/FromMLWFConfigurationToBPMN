package com.ml2wf.conventions.enums.bpmn;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This {@code enum} contains handled attributes' names according to the
 * <a href="http://www.bpmn.org/">BPMN standard</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BPMNAttributes {

    BACKGROUND("ext:shapeBackground"), ID("id"), NAME("name"),
    // positional attributes
    ELEMENT("bpmnElement"), HEIGHT("height"), WIDTH("width"), X("x"), Y("y");

    /**
     * Tag name.
     */
    private final String name;
}
