package com.ml2wf.neo4j.storage.converter.impl;

import com.ml2wf.core.workflow.StandardWorkflowTask;
import com.ml2wf.neo4j.storage.converter.INeo4JStandardWorkflowConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
public class Neo4JWorkflowTasksConverter implements INeo4JStandardWorkflowConverter {

    @Override
    public @NonNull Neo4JStandardWorkflowTask fromStandardWorkflowTask(@NonNull StandardWorkflowTask standardWorkflowTask,
                                                                       @Nullable StandardWorkflowTask child) {
        return new Neo4JStandardWorkflowTask(
                standardWorkflowTask.getName(),
                standardWorkflowTask.isAbstract(),
                standardWorkflowTask.isOptional(),
                null,
                standardWorkflowTask.getDescription(),
                child != null ? fromStandardWorkflowTask(child) : null
        );
    }
}
