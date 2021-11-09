package com.ml2wf.v2.xml.fm.builders;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.ml2wf.v2.tree.fm.FeatureModelStructure;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonPOJOBuilder(withPrefix = "")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FMStructureBuilder {

    protected List<FeatureModelTask> children = new ArrayList<>();

    public FMStructureBuilder children(List<FeatureModelTask> children) {
        this.children = children;
        System.out.println("set");
        return this;
    }

    public FeatureModelStructure build() {
        System.out.println("build struct !");
        return new FeatureModelStructure(children);
    }
}
