package com.ml2wf.arango.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.arango.storage.dto.*;
import com.ml2wf.arango.storage.repository.*;
import com.ml2wf.arango.storage.converter.IArangoStandardKnowledgeConverter;
import com.ml2wf.arango.storage.converter.impl.ArangoConstraintsConverter;
import com.ml2wf.contract.business.AbstractStandardKnowledgeComponent;
import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Profile("arango")
@Component
public class ArangoStandardKnowledgeComponent extends AbstractStandardKnowledgeComponent<ArangoStandardKnowledgeTask, ArangoConstraintOperand, ArangoTaskVersion> {

    private final ArangoStandardKnowledgeTasksLinksRepository standardKnowledgeTasksLinkRepository;
    private final ArangoConstraintsLinksRepository constraintsLinksRepository;
    private final ArangoConstraintsToTaskLinksRepository constraintsToTaskLinksRepository;

    public ArangoStandardKnowledgeComponent(
            @Autowired ArangoStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
            @Autowired ArangoConstraintsRepository constraintsRepository,
            @Autowired ArangoVersionsRepository versionsRepository,
            @Autowired ArangoConstraintsConverter constraintsConverter,
            @Autowired IArangoStandardKnowledgeConverter tasksConverter,
            @Autowired ArangoStandardKnowledgeTasksLinksRepository standardKnowledgeTasksLinkRepository,
            @Autowired ArangoConstraintsLinksRepository constraintsLinksRepository,
            @Autowired ArangoConstraintsToTaskLinksRepository constraintsToTaskLinksRepository
    ) {
        super(standardKnowledgeTasksRepository, constraintsRepository, versionsRepository, constraintsConverter, tasksConverter);
        this.standardKnowledgeTasksLinkRepository = standardKnowledgeTasksLinkRepository;
        this.constraintsLinksRepository = constraintsLinksRepository;
        this.constraintsToTaskLinksRepository = constraintsToTaskLinksRepository;
    }

    private Optional<StandardKnowledgeTask> containsTaskWithName(StandardKnowledgeTask task, String name) {
        // TODO: to move to dedicated class
        return task.getName().equals(name) ? Optional.of(task) : task.getTasks().stream()
                .map(t -> (t.getName().equals(name)) ? Optional.of(t) : containsTaskWithName(t, name))
                .filter(Optional::isPresent)
                .findAny()
                .orElse(Optional.empty());
    }

    private void saveLinks(
            StandardKnowledgeTree knowledgeTree,
            Iterable<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks
    ) {
        // TODO: move to dedicated component
        Map<String, ArangoStandardKnowledgeTask> map = StreamSupport.stream(arangoStandardKnowledgeTasks.spliterator(), false)
                .collect(Collectors.toMap(ArangoStandardKnowledgeTask::getName, t -> t));
        map.forEach((k, v) -> {
            var knowledgeTask = containsTaskWithName(knowledgeTree.getTasks().get(0), k).orElseThrow();
            knowledgeTask.getTasks().forEach(c -> standardKnowledgeTasksLinkRepository.save(new ArangoStandardKnowledgeTaskLink(v, map.get(c.getName()))));
        });
    }

    private void saveRootLink(StandardKnowledgeTree standardKnowledgeTree,
                              Iterable<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks,
                              ArangoStandardKnowledgeTask root) {
        Map<String, ArangoStandardKnowledgeTask> map = StreamSupport.stream(arangoStandardKnowledgeTasks.spliterator(), false)
                .collect(Collectors.toMap(ArangoStandardKnowledgeTask::getName, t -> t));
        standardKnowledgeTasksLinkRepository.save(new ArangoStandardKnowledgeTaskLink(root, map.get(standardKnowledgeTree.getTasks().get(0).getName())));
    }

    private Collection<ArangoConstraintOperand> saveConstraints(Collection<ArangoConstraintOperand> operands) {
        var updatedParentsOperands = ImmutableList.copyOf(constraintsRepository.saveAll(operands));
        for (var parentOperand : updatedParentsOperands) {
            for (var childOperand : saveConstraints(parentOperand.getOperands())) {
                constraintsLinksRepository.save(new ArangoConstraintLink(parentOperand, childOperand));
            }
            if (parentOperand.getTask() != null) {
                constraintsToTaskLinksRepository.save(new ArangoConstraintToTaskLink(parentOperand, parentOperand.getTask()));
            }
        }
        return updatedParentsOperands;
    }

    private void saveConstraintRootLinks(Iterable<ArangoConstraintOperand> operands, ArangoConstraintOperand root) {
        operands.forEach(o -> constraintsLinksRepository.save(new ArangoConstraintLink(root, o)));
    }

    public boolean importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree) {
        // TODO: split into dedicated components (one for tasks, one for constraints...)
        var lastVersion = versionsRepository.getLastVersion().orElseGet(() -> new ArangoTaskVersion(0, 0, 0, "unversioned"));
        // converting and saving tasks
        var arangoStandardKnowledgeTasks = tasksConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        arangoStandardKnowledgeTasks.forEach(t -> {
            t.getVersion().setMajor(lastVersion.getMajor() + 1);
            t.getVersion().setName(versionName);
        });
        var newRootVersion = new ArangoTaskVersion(lastVersion.getMajor() + 1, 0, 0, versionName);
        versionsRepository.save(newRootVersion);
        var rootTask = new ArangoStandardKnowledgeTask(ROOT_NODE_NAME, true, true, newRootVersion, "reserved tree root. internal use only. not exported.");
        standardKnowledgeTasksRepository.save(rootTask); // saving reserved tree root
        var iterableUpdatedArangoTasks = standardKnowledgeTasksRepository.saveAll(arangoStandardKnowledgeTasks);
        saveRootLink(standardKnowledgeTree, arangoStandardKnowledgeTasks, rootTask); // saving reserved tree root link to first knowledge tree task
        saveLinks(standardKnowledgeTree, iterableUpdatedArangoTasks);
        // converting and saving constraints
        var rootConstraint = new ArangoConstraintOperand(ROOT_CONSTRAINT_NODE_NAME, new ArangoTaskVersion(lastVersion.getMajor() + 1, 0, 0, versionName), Collections.emptyList());
        constraintsRepository.save(rootConstraint); // saving reserved constraint tree root
        var arangoConstraintOperands = constraintsConverter.fromStandardKnowledgeTree(
                standardKnowledgeTree, ImmutableList.copyOf(iterableUpdatedArangoTasks)
        );
        arangoConstraintOperands.forEach(t -> {
            t.getVersion().setMajor(lastVersion.getMajor() + 1);
            t.getVersion().setName(versionName);
        });
        var iterableUpdatedArangoConstraints = saveConstraints(arangoConstraintOperands);
        saveConstraintRootLinks(iterableUpdatedArangoConstraints, rootConstraint);
        return true;
    }
}
