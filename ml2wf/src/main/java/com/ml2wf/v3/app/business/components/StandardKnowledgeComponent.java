package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.business.storage.graph.ArangoTasksConverter;
import com.ml2wf.v3.app.business.storage.graph.IArangoStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.StandardKnowledgeTasksLinkRepository;
import com.ml2wf.v3.app.business.storage.graph.StandardKnowledgeTasksRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTaskLink;
import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class StandardKnowledgeComponent {

    private static final String ROOT_NODE_NAME = "__ROOT";

    private final StandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository;
    private final IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter;
    private final ArangoTasksConverter arangoTasksConverter; // TODO: make autowirable

    public StandardKnowledgeComponent(@Autowired StandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                      @Autowired StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository,
                                      @Autowired IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.standardKnowledgeTasksLinkRepository = standardKnowledgeTasksLinkRepository;
        this.arangoStandardKnowledgeConverter = arangoStandardKnowledgeConverter;
        this.arangoTasksConverter = new ArangoTasksConverter();
    }

    public StandardKnowledgeTree getStandardKnowledgeTree() {
        ArangoStandardKnowledgeTask arangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByName(ROOT_NODE_NAME);
        System.out.println("arangoStandardKnowledgeTask = " + arangoStandardKnowledgeTask); // TODO: manage possible missing -> null
        // __ROOT node is for internal use only and should not be exported
        var firstArangoTreeTask = new ArrayList<>(arangoStandardKnowledgeTask.getChildren()).get(0);
        return arangoTasksConverter.toStandardKnowledgeTree(firstArangoTreeTask);
    }

    public StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName) {
        ArangoStandardKnowledgeTask arangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByName(taskName);
        System.out.println("arangoStandardKnowledgeTask = " + arangoStandardKnowledgeTask); // TODO: manage possible missing -> null
        return arangoTasksConverter.toStandardKnowledgeTree(arangoStandardKnowledgeTask);
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

    public boolean importStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        var arangoStandardKnowledgeTasks = arangoStandardKnowledgeConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        var rootTask = new ArangoStandardKnowledgeTask(ROOT_NODE_NAME, true, true, "reserved tree root. internal use only. not exported.");
        standardKnowledgeTasksRepository.save(rootTask); // saving reserved tree root
        var iterableUpdatedArangoTasks = standardKnowledgeTasksRepository.saveAll(arangoStandardKnowledgeTasks);
        saveRootLink(standardKnowledgeTree, arangoStandardKnowledgeTasks, rootTask); // saving reserved tree root link to first knowledge tree task
        saveLinks(standardKnowledgeTree, iterableUpdatedArangoTasks);
        return true;
    }
}
