package com.ml2wf.v3.app.xml.mixins.tree.custom.featuremodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ml2wf.v3.app.xml.mixins.tree.custom.featuremodel.serializers.FeatureModelRuleSerializer;

import java.util.Map;

@JsonSerialize(using = FeatureModelRuleSerializer.class)
public abstract class FMRuleMixin {

    @JsonCreator
    protected FMRuleMixin(Map<String, Object> props) {}
}
