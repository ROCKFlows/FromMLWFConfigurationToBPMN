package com.ml2wf.app.components;

import com.ml2wf.contract.business.IStandardKnowledgeComponent;
import com.ml2wf.core.tree.converter.FeatureModelConverter;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<FeatureModel> getFeatureModel(String versionName) {
        return standardKnowledgeComponent.getStandardKnowledgeTree(versionName)
                .map(featureModelConverter::fromStandardKnowledgeTree);
    }

    public Mono<FeatureModel> getFeatureModelTaskWithName(String taskName, String versionName) {
        return standardKnowledgeComponent.getStandardKnowledgeTaskWithName(taskName, versionName)
                .map(featureModelConverter::fromStandardKnowledgeTree);
    }


    public Mono<Boolean> importFeatureModel(String versionName, FeatureModel featureModel) {
        var standardKnowledgeTree = featureModelConverter.toStandardKnowledgeTree(featureModel);
        return standardKnowledgeComponent.importStandardKnowledgeTree(versionName, standardKnowledgeTree);
    }
}
