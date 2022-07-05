package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.components.neo4j.Neo4JStandardKnowledgeComponent;
import com.ml2wf.v3.app.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.v3.app.tree.converter.FeatureModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureModelComponent {

    // TODO: intercept this method return and convert automatically standard knowledge tree to feature model (Aspect4J ?)

    private final IStandardKnowledgeComponent standardKnowledgeComponent;
    private final FeatureModelConverter featureModelConverter; // TODO: make autowirable

    public FeatureModelComponent(@Autowired Neo4JStandardKnowledgeComponent standardKnowledgeComponent) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        featureModelConverter = new FeatureModelConverter();
    }

    public FeatureModel getFeatureModel(String versionName) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        return featureModelConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
    }

    public FeatureModel getFeatureModelTaskWithName(String taskName, String versionName) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTaskWithName(taskName, versionName);
        return featureModelConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
    }


    public boolean importFeatureModel(String versionName, FeatureModel featureModel) {
        var standardKnowledgeTree = featureModelConverter.toStandardKnowledgeTree(featureModel);
        return standardKnowledgeComponent.importStandardKnowledgeTree(versionName, standardKnowledgeTree);
    }
}
