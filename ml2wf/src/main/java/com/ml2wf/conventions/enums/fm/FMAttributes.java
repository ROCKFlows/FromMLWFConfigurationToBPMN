package com.ml2wf.conventions.enums.fm;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This {@code enum} contains handled attributes' names according to the
 * <a href="https://featureide.github.io/">FeatureIDE
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FMAttributes {

    NAME("name"), ABSTRACT("abstract"), MANDATORY("mandatory"), KEY("key"), VALUE("value"), TYPE("type");

    /**
     * Tag name.
     */
    private final String name;
}
