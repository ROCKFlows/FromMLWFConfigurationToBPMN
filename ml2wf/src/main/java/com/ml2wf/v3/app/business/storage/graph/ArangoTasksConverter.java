package com.ml2wf.v3.app.business.storage.graph;

import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArangoTasksConverter implements IArangoStandardKnowledgeConverter {

    @Override
    public StandardKnowledgeTree toStandardKnowledgeTree(ArangoStandardKnowledgeTask arangoTreeTask) {
        return new StandardKnowledgeTree(Collections.singletonList(toStandardKnowledgeTask(arangoTreeTask)), Collections.emptyList()); // TODO: process constraints
    }

    @Override
    public StandardKnowledgeTask toStandardKnowledgeTask(ArangoStandardKnowledgeTask arangoTreeTask) {
        return new StandardKnowledgeTask(
                arangoTreeTask.getName(),
                arangoTreeTask.getDescription(),
                arangoTreeTask.isAbstract(),
                !arangoTreeTask.isMandatory(),
                arangoTreeTask.getChildren().stream()
                        .map(this::toStandardKnowledgeTask)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        if (standardKnowledgeTree.getTasks().isEmpty()) {
            throw new IllegalStateException("Can't convert an empty knowledge tree model.");
        }
        return fromStandardKnowledgeTask(standardKnowledgeTree.getTasks().get(0)); // TODO: currently, we only support one root task
    }

    @Override
    public List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        var newArangoTask = new ArangoStandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(),
                standardKnowledgeTask.getDocumentation());
        if (standardKnowledgeTask.getTasks().isEmpty()) {
            return Collections.singletonList(newArangoTask);
        }
        var newArangoTasks = standardKnowledgeTask.getTasks().stream()
                .map(this::fromStandardKnowledgeTask)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        newArangoTasks.add(newArangoTask);
        return newArangoTasks;
    }
}
