package com.ml2wf.v3.app.tree.custom.featuremodel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@Log4j2
public class FeatureModelStructure {

    private final List<FeatureModelTask> children;

    public FeatureModelStructure(List<FeatureModelTask> children) {
        this.children = children;
    }

    @SuppressWarnings("unused")
    protected FeatureModelStructure() {
        // used by Jackson for deserialization
        this(new ArrayList<>());
    }
}
