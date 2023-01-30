package com.ml2wf.core.tree.custom.featuremodel;

import com.ml2wf.core.tree.custom.CustomKnowledgeTree;
import lombok.*;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class FeatureModel extends CustomKnowledgeTree {

    private FeatureModelStructure structure;
    @Getter @NonNull private final Set<FeatureModelRule> constraints = new LinkedHashSet<>();

    public FeatureModel(@NonNull FeatureModelStructure structure, Collection<FeatureModelRule> constraints) {
        this(structure);
        this.constraints.addAll(constraints);
    }

    protected FeatureModel(@NonNull FeatureModelStructure structure) {
        this();
        this.structure = structure;
    }
}
