package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.converter.INeo4JStandardKnowledgeConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JKnowledgeTasksConverter implements INeo4JStandardKnowledgeConverter {

    @Override
    public List<Neo4JStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        List<Neo4JStandardKnowledgeTask> newTasks = new ArrayList<>();
        var newChildrenTasks = standardKnowledgeTask.getTasks().stream()
                .map(this::fromStandardKnowledgeTask)
                .collect(Collectors.toMap(t -> t.get(0), t -> t.subList(1, t.size())));
        var newNeo4JTask = new Neo4JStandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(),
                null, // TODO: take source task version in consideration
                standardKnowledgeTask.getDocumentation(),
                newChildrenTasks.keySet());
        if (standardKnowledgeTask.getTasks().isEmpty()) {
            return Collections.singletonList(newNeo4JTask);
        }
        newTasks.add(newNeo4JTask);
        newTasks.addAll(newChildrenTasks.keySet());
        newTasks.addAll(newChildrenTasks.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        return newTasks;
    }
}
