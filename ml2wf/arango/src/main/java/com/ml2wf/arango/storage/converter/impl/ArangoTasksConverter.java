package com.ml2wf.arango.storage.converter.impl;

import com.ml2wf.arango.storage.converter.IArangoStandardKnowledgeConverter;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArangoTasksConverter implements IArangoStandardKnowledgeConverter {

    @Override
    public List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        var newArangoTask = new ArangoStandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(),
                new ArangoTaskVersion(0, 0, 0, "unversioned"), // TODO: take source task version in consideration
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
