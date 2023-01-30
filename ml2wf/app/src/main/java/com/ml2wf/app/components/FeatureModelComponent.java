package com.ml2wf.app.components;

import com.ml2wf.contract.business.IStandardKnowledgeComponent;
import com.ml2wf.core.tree.converter.FeatureModelConverter;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeatureModelComponent {

    // TODO: intercept this method return and convert automatically standard knowledge tree to feature model (Aspect4J ?)

    private final IStandardKnowledgeComponent standardKnowledgeComponent;
    private final FeatureModelConverter featureModelConverter;

    public FeatureModelComponent(@Autowired IStandardKnowledgeComponent standardKnowledgeComponent,
                                 @Autowired FeatureModelConverter featureModelConverter) {
        this.standardKnowledgeComponent = standardKnowledgeComponent;
        this.featureModelConverter = featureModelConverter;
    }

    public FeatureModel getFeatureModel(String versionName) {
        var standardKnowledgeTree = standardKnowledgeComponent.getStandardKnowledgeTree(versionName);
        return featureModelConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
    }

    public FeatureModelTask getFeatureModelTaskWithName(String taskName, String versionName) {
        var standardKnowledgeTask = standardKnowledgeComponent.getStandardKnowledgeTaskWithName(taskName, versionName);
        return featureModelConverter.fromStandardKnowledgeTask(standardKnowledgeTask);
    }


    public boolean importFeatureModel(String versionName, FeatureModel featureModel) {
        var standardKnowledgeTree = featureModelConverter.toStandardKnowledgeTree(featureModel);
        return standardKnowledgeComponent.importStandardKnowledgeTree(versionName, standardKnowledgeTree);
    }
}
