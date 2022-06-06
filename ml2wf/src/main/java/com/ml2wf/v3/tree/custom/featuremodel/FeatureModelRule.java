package com.ml2wf.v3.tree.custom.featuremodel;

import com.ml2wf.v3.constraints.ConstraintTree;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureModelRule {

    private ConstraintTree constraint;
    private Description description;

    protected FeatureModelRule(Map<String, Object> ruleMapping) {
        var optDescription = Optional.ofNullable((String) ruleMapping.remove("description"));
        description = new Description(optDescription.orElse(""));
        constraint = ConstraintTree.fromMap(ruleMapping);
    }

    /**
     * A {@link Description} has a {@link #content} providing additional
     * information about a {@link FeatureModelRule}.
     *
     * @see FeatureModelRule
     *
     * @since 1.1.0
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static final class Description {

        @NonNull private String content;
    }
}
