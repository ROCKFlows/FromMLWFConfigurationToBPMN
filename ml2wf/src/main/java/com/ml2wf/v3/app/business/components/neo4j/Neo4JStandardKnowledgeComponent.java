package com.ml2wf.v3.app.business.components.neo4j;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.INeo4JConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.INeo4JStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JConstraintsRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JVersionsRepository;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Neo4JStandardKnowledgeComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final Neo4JConstraintsRepository constraintsRepository;
    private final Neo4JVersionsRepository versionsRepository;
    private final INeo4JStandardKnowledgeConverter neo4JStandardKnowledgeConverter;
    private final INeo4JConstraintsConverter neo4JConstraintsConverter;

    public Neo4JStandardKnowledgeComponent(@Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                           @Autowired Neo4JConstraintsRepository constraintsRepository,
                                           @Autowired Neo4JVersionsRepository versionsRepository,
                                           @Autowired INeo4JStandardKnowledgeConverter neo4JStandardKnowledgeConverter,
                                           @Autowired INeo4JConstraintsConverter neo4JConstraintsConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.constraintsRepository = constraintsRepository;
        this.versionsRepository = versionsRepository;
        this.neo4JStandardKnowledgeConverter = neo4JStandardKnowledgeConverter;
        this.neo4JConstraintsConverter = neo4JConstraintsConverter;
    }

    public StandardKnowledgeTree getStandardKnowledgeTree(String versionName) {
        var optArangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(ROOT_NODE_NAME, versionName);
        var arangoStandardKnowledgeTask = optArangoStandardKnowledgeTask.orElseThrow(
                () -> new BadRequestException("No task found for version " + versionName));
        // __ROOT node is for internal use only and should not be exported
        var firstArangoTreeTask = new ArrayList<>(arangoStandardKnowledgeTask.getChildren()).get(0);
        var rootConstraint = constraintsRepository.findAllByTypeEqualsAndVersion_Name(ROOT_CONSTRAINT_NODE_NAME, firstArangoTreeTask.getVersion().getName());
        return neo4JStandardKnowledgeConverter.toStandardKnowledgeTree(firstArangoTreeTask, rootConstraint.get(0).getOperands().stream()
                .map(neo4JConstraintsConverter::toConstraintTree)
                .collect(Collectors.toList()));
    }

    public Optional<Neo4JStandardKnowledgeTask> getArangoTaskWithName(String taskName, String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(taskName, versionName);
    }

    public StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName, String versionName) {
        var optArangoStandardKnowledgeTask = getArangoTaskWithName(taskName, versionName);
        return neo4JStandardKnowledgeConverter.toStandardKnowledgeTree(optArangoStandardKnowledgeTask.orElseThrow(
                () -> new BadRequestException(String.format("No task found for name %s and version %s.", taskName, versionName))
        ));
    }

    private Optional<StandardKnowledgeTask> containsTaskWithName(StandardKnowledgeTask task, String name) {
        // TODO: to move to dedicated class
        return task.getName().equals(name) ? Optional.of(task) : task.getTasks().stream()
                .map(t -> (t.getName().equals(name)) ? Optional.of(t) : containsTaskWithName(t, name))
                .filter(Optional::isPresent)
                .findAny()
                .orElse(Optional.empty());
    }

    public boolean importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree) {
        // TODO: split into dedicated components (one for tasks, one for constraints...)
        // saving new version
        var lastVersion = versionsRepository.getLastVersion()
                .orElseGet(() -> new Neo4JTaskVersion(0, 0, 0, "unversioned"));
        var newVersion = versionsRepository.save(new Neo4JTaskVersion(lastVersion.getMajor() + 1, 0, 0, versionName));
        // converting and saving tasks
        var neo4JStandardKnowledgeTasks = neo4JStandardKnowledgeConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        neo4JStandardKnowledgeTasks.forEach(t -> t.setVersion(newVersion));
        var iterableUpdatedNeo4JTasks = standardKnowledgeTasksRepository.saveAll(neo4JStandardKnowledgeTasks);
        var rootTask = new Neo4JStandardKnowledgeTask(ROOT_NODE_NAME, true, true, newVersion, "reserved tree root. internal use only. not exported.", Collections.singletonList(iterableUpdatedNeo4JTasks.get(0)));
        standardKnowledgeTasksRepository.save(rootTask); // saving reserved tree root
        // converting and saving constraints
        var neo4jConstraintOperands = neo4JConstraintsConverter.fromStandardKnowledgeTree(
                standardKnowledgeTree, ImmutableList.copyOf(iterableUpdatedNeo4JTasks)
        );
        neo4jConstraintOperands.forEach(t -> t.setVersion(newVersion));
        var iterableUpdatedNeo4JOperands = constraintsRepository.saveAll(neo4jConstraintOperands);
        var rootConstraint = new Neo4JConstraintOperand(ROOT_CONSTRAINT_NODE_NAME, newVersion, new ArrayList<>(iterableUpdatedNeo4JOperands));
        constraintsRepository.save(rootConstraint); // saving reserved constraint tree root
        return true;
    }
}
