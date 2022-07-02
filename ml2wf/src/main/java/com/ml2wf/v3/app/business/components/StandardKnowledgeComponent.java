package com.ml2wf.v3.app.business.components;

import com.google.common.collect.ImmutableList;
import com.ml2wf.v3.app.business.storage.graph.arango.converter.impl.ArangoTasksConverter;
import com.ml2wf.v3.app.business.storage.graph.arango.converter.IArangoConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.arango.converter.IArangoStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.*;
import com.ml2wf.v3.app.business.storage.graph.arango.repository.*;
import com.ml2wf.v3.app.exceptions.BadRequestException;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class StandardKnowledgeComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";
    private static final String ROOT_CONSTRAINT_NODE_NAME = "__ROOT_CONSTRAINT";

    private final StandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository;
    private final ConstraintsRepository constraintsRepository;
    private final ConstraintsLinksRepository constraintsLinksRepository;
    private final ConstraintsToTaskLinksRepository constraintsToTaskLinksRepository;
    private final VersionsRepository versionsRepository;
    private final IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter;
    private final IArangoConstraintsConverter arangoConstraintsConverter;
    private final ArangoTasksConverter arangoTasksConverter;

    public StandardKnowledgeComponent(@Autowired StandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                      @Autowired StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository,
                                      @Autowired ConstraintsRepository constraintsRepository,
                                      @Autowired ConstraintsLinksRepository constraintsLinksRepository,
                                      @Autowired ConstraintsToTaskLinksRepository constraintsToTaskLinksRepository,
                                      @Autowired VersionsRepository versionsRepository,
                                      @Autowired IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter,
                                      @Autowired IArangoConstraintsConverter arangoConstraintsConverter,
                                      @Autowired ArangoTasksConverter arangoTasksConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.standardKnowledgeTasksLinkRepository = standardKnowledgeTasksLinkRepository;
        this.constraintsRepository = constraintsRepository;
        this.constraintsLinksRepository = constraintsLinksRepository;
        this.constraintsToTaskLinksRepository = constraintsToTaskLinksRepository;
        this.versionsRepository = versionsRepository;
        this.arangoStandardKnowledgeConverter = arangoStandardKnowledgeConverter;
        this.arangoConstraintsConverter = arangoConstraintsConverter;
        this.arangoTasksConverter = arangoTasksConverter;
    }

    public StandardKnowledgeTree getStandardKnowledgeTree(String versionName) {
        var optArangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(ROOT_NODE_NAME, versionName);
        var arangoStandardKnowledgeTask = optArangoStandardKnowledgeTask.orElseThrow(
                () -> new BadRequestException("No task found for version " + versionName));
        // __ROOT node is for internal use only and should not be exported
        var firstArangoTreeTask = new ArrayList<>(arangoStandardKnowledgeTask.getChildren()).get(0);
        var rootConstraint = constraintsRepository.findAllByTypeEqualsAndVersion_Name(ROOT_CONSTRAINT_NODE_NAME, firstArangoTreeTask.getVersion().getName());
        return arangoTasksConverter.toStandardKnowledgeTree(firstArangoTreeTask, rootConstraint.get(0).getOperands().stream()
                .map(arangoConstraintsConverter::toConstraintTree)
                .collect(Collectors.toList()));
    }

    public Optional<ArangoStandardKnowledgeTask> getArangoTaskWithName(String taskName, String versionName) {
        return standardKnowledgeTasksRepository.findOneByNameAndVersion_Name(taskName, versionName);
    }

    public StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName, String versionName) {
        var optArangoStandardKnowledgeTask = getArangoTaskWithName(taskName, versionName);
        return arangoTasksConverter.toStandardKnowledgeTree(optArangoStandardKnowledgeTask.orElseThrow(
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
        var arangoStandardKnowledgeTasks = arangoStandardKnowledgeConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
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
        var arangoConstraintOperands = arangoConstraintsConverter.fromStandardKnowledgeTree(
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
