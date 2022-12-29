package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.converter.INeo4JStandardKnowledgeConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JTasksConverter implements INeo4JStandardKnowledgeConverter {

    @Override
    public List<GraphStandardKnowledgeTask<Neo4JTaskVersion>> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask) {
        List<GraphStandardKnowledgeTask<Neo4JTaskVersion>> newTasks = new ArrayList<>();
        var newChildrenTasks = standardKnowledgeTask.getTasks().stream()
                .map(this::fromStandardKnowledgeTask)
                .collect(Collectors.toMap(t -> t.get(0), t -> t.subList(1, t.size())));
        var newNeo4JTask = new Neo4JStandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isOptional(),
                new Neo4JTaskVersion(0, 0, 0, "unversioned"), // TODO: take source task version in consideration
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
