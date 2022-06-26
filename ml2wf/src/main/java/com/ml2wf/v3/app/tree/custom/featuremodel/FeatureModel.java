package com.ml2wf.v3.app.tree.custom.featuremodel;

import com.ml2wf.v3.app.tree.custom.CustomKnowledgeTree;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FeatureModel extends CustomKnowledgeTree {

    private FeatureModelStructure structure;
    @Getter @NonNull private final Set<FeatureModelRule> constraints = new LinkedHashSet<>();

    private FeatureModel() {
        // used by Jackson for deserialization
    }

    public FeatureModel(@NonNull FeatureModelStructure structure, Collection<FeatureModelRule> constraints) {
        this(structure);
        this.constraints.addAll(constraints);
    }

    protected FeatureModel(@NonNull FeatureModelStructure structure) {
        this();
        this.structure = structure;
    }
}
