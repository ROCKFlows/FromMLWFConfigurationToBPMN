package com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl;

import com.ml2wf.v3.app.business.storage.graph.contracts.converter.IGraphStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JTasksConverter implements IGraphStandardKnowledgeConverter<Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

    @Override
    public List<Neo4JStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        List<Neo4JStandardKnowledgeTask> newTasks = new ArrayList<>();
        var newChildrenTasks = standardKnowledgeTask.getTasks().stream()
                .map(this::fromStandardKnowledgeTask)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        var newNeo4JTask = new Neo4JStandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(),
                new Neo4JTaskVersion(0, 0, 0, "unversioned"), // TODO: take source task version in consideration
                standardKnowledgeTask.getDocumentation(),
                newChildrenTasks);
        if (standardKnowledgeTask.getTasks().isEmpty()) {
            return Collections.singletonList(newNeo4JTask);
        }
        newTasks.add(newNeo4JTask);
        newTasks.addAll(newChildrenTasks);
        return newTasks;
    }
}
