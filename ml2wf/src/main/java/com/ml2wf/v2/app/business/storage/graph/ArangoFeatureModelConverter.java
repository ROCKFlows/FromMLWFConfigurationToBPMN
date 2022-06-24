package com.ml2wf.v2.app.business.storage.graph;

import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTask;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Component
public class ArangoFeatureModelConverter implements IArangoFeatureModelConverter {

    @Override
    public FeatureModel toRawFeatureModel(ArangoFeatureModelTask arangoFeatureModelTask) {
        return FeatureModel.FeatureModelFactory.fromFeatureModelTask(toRawFeatureModelTask(arangoFeatureModelTask));
    }

    @Override
    public FeatureModelTask toRawFeatureModelTask(ArangoFeatureModelTask arangoFeatureModelTask) {
        var newRawTask = FeatureModelTask.FeatureModelTaskFactory.createTask(
                arangoFeatureModelTask.getName(),arangoFeatureModelTask.isAbstract(),
                arangoFeatureModelTask.isMandatory(), singletonList(arangoFeatureModelTask.getDescription()));
        arangoFeatureModelTask.getChildren().stream()
                .map(this::toRawFeatureModelTask)
                .forEach(newRawTask::appendDirectChild);
        return newRawTask;
    }

    @Override
    public List<ArangoFeatureModelTask> fromRawFeatureModel(FeatureModel featureModel) {
        if (featureModel.getChildren().isEmpty()) {
            throw new IllegalStateException("Can't convert an empty feature model.");
        }
        return fromRawFeatureModelTask(featureModel.getChildren().get(0)); // TODO: currently, we only support one root task
    }

    @Override
    public List<ArangoFeatureModelTask> fromRawFeatureModelTask(FeatureModelTask featureModelTask) {
        String description = (featureModelTask.getDescriptions().isEmpty()) ? "" :
                featureModelTask.getDescriptions().get(0).getContent(); // TODO: currently, only one doc is supported
        var newArangoTask = new ArangoFeatureModelTask(featureModelTask.getName(), featureModelTask.isAbstract(),
                featureModelTask.isMandatory(), description);
        if (featureModelTask.getChildren().isEmpty()) {
            return Collections.singletonList(newArangoTask);
        }
        var newArangoTasks = featureModelTask.getChildren().stream()
                .map(this::fromRawFeatureModelTask)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        newArangoTasks.add(newArangoTask);
        return newArangoTasks;
    }
}
