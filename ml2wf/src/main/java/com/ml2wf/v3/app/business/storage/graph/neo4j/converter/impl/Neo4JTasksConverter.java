package com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl;

import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.INeo4JStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Neo4JTasksConverter implements INeo4JStandardKnowledgeConverter {

    @Override
    public StandardKnowledgeTree toStandardKnowledgeTree(Neo4JStandardKnowledgeTask standardKnowledgeTask) {
        return new StandardKnowledgeTree(
                Collections.singletonList(toStandardKnowledgeTask(standardKnowledgeTask)),
                Collections.emptyList()
        );
    }

    @Override
    public StandardKnowledgeTree toStandardKnowledgeTree(Neo4JStandardKnowledgeTask standardKnowledgeTask,
                                                         List<ConstraintTree> constraintTrees) {
        return new StandardKnowledgeTree(
                Collections.singletonList(toStandardKnowledgeTask(standardKnowledgeTask)),
                constraintTrees
        );
    }

    private String formatVersion(Neo4JTaskVersion version) {
        return String.format("v%d.%d.%d <%s>", version.getMajor(), version.getMinor(), version.getPatch(), version.getName());
    }

    @Override
    public StandardKnowledgeTask toStandardKnowledgeTask(Neo4JStandardKnowledgeTask standardKnowledgeTask) {
        return new StandardKnowledgeTask(
                standardKnowledgeTask.getName(),
                standardKnowledgeTask.getDescription(),
                standardKnowledgeTask.isAbstract(),
                !standardKnowledgeTask.isMandatory(),
                formatVersion(standardKnowledgeTask.getVersion()),
                standardKnowledgeTask.getChildren().stream()
                        .map(this::toStandardKnowledgeTask)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Neo4JStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        if (standardKnowledgeTree.getTasks().isEmpty()) {
            throw new IllegalStateException("Can't convert an empty knowledge tree model.");
        }
        return fromStandardKnowledgeTask(standardKnowledgeTree.getTasks().get(0)); // TODO: currently, we only support one root task
    }

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
