package com.ml2wf.v2.xml.fm.builders;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@JsonPOJOBuilder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FMTaskBuilder extends FMStructureBuilder {

    private FeatureModelTask parent;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private List<FeatureModelTask.Description> descriptions = new ArrayList<>();

    public FMTaskBuilder withParent(FeatureModelTask parent) {
        this.parent = parent;
        return this;
    }

    public FMTaskBuilder withName(String name) {
        System.out.println("set name");
        this.name = name;
        return this;
    }

    public FMTaskBuilder withAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
        return this;
    }

    public FMTaskBuilder withMandatory(boolean mandatory) {
        isMandatory = mandatory;
        return this;
    }

    public FMTaskBuilder withDescriptions(List<FeatureModelTask.Description> descriptions) {
        this.descriptions = new ArrayList<>(descriptions);
        return this;
    }

    @Override
    public FeatureModelTask build() {
        System.out.printf("build task %s !%n", name);
        return new FeatureModelTask(parent, name, isAbstract, isMandatory, descriptions, children);
    }
}
