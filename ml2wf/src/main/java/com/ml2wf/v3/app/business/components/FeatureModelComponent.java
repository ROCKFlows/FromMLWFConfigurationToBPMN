package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.v3.tree.converter.FeatureModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureModelComponent {

    // TODO: intercept this method return and convert automatically standard knowledge tree to feature model (Aspect4J ?)

    private final StandardKnowledgeComponent standardKnowledgeComponent;
    private final FeatureModelConverter featureModelConverter; // TODO: make autowirable

    public FeatureModelComponent(@Autowired StandardKnowledgeComponent standardKnowledgeComponent) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        featureModelConverter = new FeatureModelConverter();
    }

    public FeatureModel getFeatureModelWithName(String name) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTreeWithName(name);
        return featureModelConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
    }

    public boolean importFeatureModel(FeatureModel featureModel) {
        var standardKnowledgeTree = featureModelConverter.toStandardKnowledgeTree(featureModel);
        return standardKnowledgeComponent.importStandardKnowledgeTree(standardKnowledgeTree);
    }
}
