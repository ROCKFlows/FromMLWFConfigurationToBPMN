package com.ml2wf.core.tree.converter;

import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelRule;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelStructure;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelTask;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FeatureModelConverter implements IKnowledgeTreeConverter<FeatureModel, FeatureModelTask> {

    @Override
    public FeatureModel fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        var convertedSubTasks = standardKnowledgeTree.getTasks()
                .stream()
                .map(this::fromStandardKnowledgeTask)
                .collect(Collectors.toList());
        var convertedConstraints = standardKnowledgeTree.getConstraints()
                .stream()
                .map(c -> new FeatureModelRule(c, new FeatureModelRule.Description())) // TODO: we don't support rules description yet
                .collect(Collectors.toList());
        var structure = new FeatureModelStructure(convertedSubTasks);
        return new FeatureModel(structure, convertedConstraints);
    }

    @Override
    public StandardKnowledgeTree toStandardKnowledgeTree(FeatureModel customTree) {
        var convertedSubTasks = customTree.getStructure()
                .getChildren()
                .stream()
                .map(this::toStandardKnowledgeTask)
                .collect(Collectors.toList());
        var convertedConstraints = customTree.getConstraints()
                .stream()
                .map(FeatureModelRule::getConstraint)
                .collect(Collectors.toList());
        return new StandardKnowledgeTree(convertedSubTasks, convertedConstraints);
    }

    @Override
    public FeatureModelTask fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        var description = standardKnowledgeTask.getDocumentation().isBlank() ? null :
                new FeatureModelTask.Description(standardKnowledgeTask.getDocumentation());
        var subTasks = standardKnowledgeTask.getTasks()
                .stream()
                .map(this::fromStandardKnowledgeTask)
                .collect(Collectors.toList());
        return new FeatureModelTask(standardKnowledgeTask.getName(), standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(), "version TODO", description, subTasks);
    }

    @Override
    public StandardKnowledgeTask toStandardKnowledgeTask(FeatureModelTask featureModelTask) {
        String description = featureModelTask.getDescriptions().isEmpty() ?
                "" : featureModelTask.getDescriptions().get(0).getContent();
        var subTasks = featureModelTask.getChildren().stream()
                .map(this::toStandardKnowledgeTask)
                .collect(Collectors.toList());
        return new StandardKnowledgeTask(featureModelTask.getName(), description, featureModelTask.isAbstract(),
                !featureModelTask.isMandatory(), featureModelTask.getVersion(), subTasks);
    }
}
