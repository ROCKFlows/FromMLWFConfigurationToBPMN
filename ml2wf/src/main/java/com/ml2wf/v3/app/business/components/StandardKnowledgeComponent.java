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

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class StandardKnowledgeComponent {

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

    public StandardKnowledgeTree getStandardKnowledgeTreeWithName(String name) {
        ArangoStandardKnowledgeTask arangoStandardKnowledgeTask = standardKnowledgeTasksRepository.findOneByName(name);
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
            Iterable<ArangoStandardKnowledgeTask> arangoFeatureModelTasksIterable
    ) {
        // TODO: move to dedicated component
        Map<String, ArangoStandardKnowledgeTask> map = StreamSupport.stream(arangoFeatureModelTasksIterable.spliterator(), false)
                .collect(Collectors.toMap(ArangoStandardKnowledgeTask::getName, t -> t));
        map.forEach((k, v) -> {
            var knowledgeTask = containsTaskWithName(knowledgeTree.getTasks().get(0), k).orElseThrow();
            knowledgeTask.getTasks().forEach(c -> standardKnowledgeTasksLinkRepository.save(new ArangoStandardKnowledgeTaskLink(v, map.get(c.getName()))));
        });
    }

    public boolean importStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree) {
        var arangoFeatureModelTasks = arangoStandardKnowledgeConverter.fromStandardKnowledgeTree(standardKnowledgeTree);
        var iterableUpdatedArangoTasks = standardKnowledgeTasksRepository.saveAll(arangoFeatureModelTasks);
        saveLinks(standardKnowledgeTree, iterableUpdatedArangoTasks);
        return true;
    }
}
