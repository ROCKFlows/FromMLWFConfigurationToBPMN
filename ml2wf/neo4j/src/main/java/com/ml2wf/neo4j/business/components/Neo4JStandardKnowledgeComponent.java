package com.ml2wf.neo4j.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.contract.business.AbstractStandardKnowledgeComponent;
import com.ml2wf.contract.exception.DuplicatedVersionNameException;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.repository.Neo4JConstraintsRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Profile("neo4j")
@Component
public class Neo4JStandardKnowledgeComponent extends AbstractStandardKnowledgeComponent<Neo4JStandardKnowledgeTask,
        Neo4JConstraintOperand, Neo4JTaskVersion> {

    private static final String ROOT_NODE_NAME = "__ROOT";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    Neo4JStandardKnowledgeComponent(@Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                    @Autowired Neo4JConstraintsRepository constraintsRepository,
                                    @Autowired Neo4JVersionsRepository versionsRepository,
                                    @Autowired Neo4JConstraintsConverter constraintsConverter,
                                    @Autowired Neo4JTasksConverter tasksConverter) {
        super(standardKnowledgeTasksRepository, constraintsRepository, versionsRepository, constraintsConverter, tasksConverter);
    }

    public boolean importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree) {
        // TODO: split into dedicated components (one for tasks, one for constraints...)
        // saving new version
        var optLastVersion = versionsRepository.getLastVersion();
        var lastVersion = optLastVersion
                .orElseGet(() -> new Neo4JTaskVersion(0, 0, 0, "unversioned", null));
        if (lastVersion.getName().equals(versionName)) {
            throw new DuplicatedVersionNameException(versionName);
        }
        var newVersion = versionsRepository.save(new Neo4JTaskVersion(lastVersion.getMajor() + 1, 0, 0, versionName, optLastVersion.orElse(null)));
        // converting and saving tasks
        // TODO: fix this unsafe cast
        var neo4JStandardKnowledgeTasks = tasksConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        var iterableUpdatedNeo4JTasks = ImmutableList.copyOf(
                standardKnowledgeTasksRepository.saveAll(neo4JStandardKnowledgeTasks));
        var rootTask = new Neo4JStandardKnowledgeTask(ROOT_NODE_NAME, true, true, newVersion, "reserved tree root. internal use only. not exported.", Collections.singletonList(iterableUpdatedNeo4JTasks.get(0)));
        standardKnowledgeTasksRepository.save(rootTask); // saving reserved tree root
        // converting and saving constraints
        // TODO: fix this unsafe cast
        var neo4jConstraintOperands = constraintsConverter.fromStandardKnowledgeTree(
                standardKnowledgeTree, ImmutableList.copyOf(iterableUpdatedNeo4JTasks)
        );
        var iterableUpdatedNeo4JOperands = constraintsRepository.saveAll(neo4jConstraintOperands);
        var rootConstraint = new Neo4JConstraintOperand(ROOT_CONSTRAINT_NODE_NAME, newVersion, ImmutableList.copyOf(iterableUpdatedNeo4JOperands));
        constraintsRepository.save(rootConstraint); // saving reserved constraint tree root
        return true;
    }
}
