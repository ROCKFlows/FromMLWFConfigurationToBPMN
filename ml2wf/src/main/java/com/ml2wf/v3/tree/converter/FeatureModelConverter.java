package com.ml2wf.v3.tree.converter;

import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModelRule;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModelStructure;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModelTask;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

public class FeatureModelConverter implements IKnowledgeTreeConverter<FeatureModel> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class FeatureModelTaskConverter {
        private static StandardKnowledgeTask toStandardKnowledgeTask(FeatureModelTask featureModelTask) {
            String description = featureModelTask.getDescriptions().isEmpty() ?
                    "" : featureModelTask.getDescriptions().get(0).getContent();
            var subTasks = featureModelTask.getChildren().stream()
                    .map(FeatureModelTaskConverter::toStandardKnowledgeTask)
                    .collect(Collectors.toList());
            return new StandardKnowledgeTask(featureModelTask.getName(), description, featureModelTask.isAbstract(),
                    !featureModelTask.isMandatory(), subTasks);
        }

        private static FeatureModelTask toFeatureModelTask(StandardKnowledgeTask standardKnowledgeTask) {
            var description = new FeatureModelTask.Description(standardKnowledgeTask.getDocumentation());
            var subTasks = standardKnowledgeTask.getTasks()
                    .stream()
                    .map(FeatureModelTaskConverter::toFeatureModelTask)
                    .collect(Collectors.toList());
            return new FeatureModelTask(standardKnowledgeTask.getName(), standardKnowledgeTask.isAbstract(),
                    !standardKnowledgeTask.isOptional(), description, subTasks);
        }
    }

    @Override
    public FeatureModel fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        var convertedSubTasks = standardKnowledgeTree.getTasks()
                .stream()
                .map(FeatureModelTaskConverter::toFeatureModelTask)
                .collect(Collectors.toList());
        var convertedConstraints = standardKnowledgeTree.getConstraints()
                .stream()
                .map(c -> new FeatureModelRule(c, new FeatureModelRule.Description()))
                .collect(Collectors.toList());
        var structure = new FeatureModelStructure(convertedSubTasks);
        return new FeatureModel(structure, convertedConstraints);
    }

    @Override
    public StandardKnowledgeTree toStandardKnowledgeTree(FeatureModel customTree) {
        var convertedSubTasks = customTree.getStructure()
                .getChildren()
                .stream()
                .map(FeatureModelTaskConverter::toStandardKnowledgeTask)
                .collect(Collectors.toList());
        var convertedConstraints = customTree.getConstraints()
                .stream()
                .map(FeatureModelRule::getConstraint)
                .collect(Collectors.toList());
        return new StandardKnowledgeTree(convertedSubTasks, convertedConstraints);
    }
}
