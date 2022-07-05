package com.ml2wf.v3.app.business.components.neo4j;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.components.StandardKnowledgeComponent;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl.Neo4JTasksConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JConstraintsRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JVersionsRepository;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Profile("neo4j")
@Component
public class Neo4JStandardKnowledgeComponent extends StandardKnowledgeComponent<Neo4JStandardKnowledgeTask, Neo4JConstraintOperand, Neo4JTaskVersion> {

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
        var lastVersion = versionsRepository.getLastVersion()
                .orElseGet(() -> new Neo4JTaskVersion(0, 0, 0, "unversioned"));
        var newVersion = versionsRepository.save(new Neo4JTaskVersion(lastVersion.getMajor() + 1, 0, 0, versionName));
        // converting and saving tasks
        var neo4JStandardKnowledgeTasks = tasksConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        neo4JStandardKnowledgeTasks.forEach(t -> t.setVersion(newVersion));
        var iterableUpdatedNeo4JTasks = ImmutableList.copyOf(standardKnowledgeTasksRepository.saveAll(neo4JStandardKnowledgeTasks));
        var rootTask = new Neo4JStandardKnowledgeTask(ROOT_NODE_NAME, true, true, newVersion, "reserved tree root. internal use only. not exported.", Collections.singletonList(iterableUpdatedNeo4JTasks.get(0)));
        standardKnowledgeTasksRepository.save(rootTask); // saving reserved tree root
        // converting and saving constraints
        var neo4jConstraintOperands = constraintsConverter.fromStandardKnowledgeTree(
                standardKnowledgeTree, ImmutableList.copyOf(iterableUpdatedNeo4JTasks)
        );
        neo4jConstraintOperands.forEach(t -> t.setVersion(newVersion));
        var iterableUpdatedNeo4JOperands = constraintsRepository.saveAll(neo4jConstraintOperands);
        var rootConstraint = new Neo4JConstraintOperand(ROOT_CONSTRAINT_NODE_NAME, newVersion, ImmutableList.copyOf(iterableUpdatedNeo4JOperands));
        constraintsRepository.save(rootConstraint); // saving reserved constraint tree root
        return true;
    }
}
