package com.ml2wf.v3.app.controllers;

import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.tree.converter.FeatureModelConverter;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.v3.xml.XMLObjectMapperFactory;
import com.ml2wf.v3.app.business.storage.graph.StandardKnowledgeTasksRepository;
import com.ml2wf.v3.app.business.storage.graph.StandardKnowledgeTasksLinkRepository;
import com.ml2wf.v3.app.business.storage.graph.IArangoStandardKnowledgeConverter;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTaskLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/fm")
public class FeatureModelController {

    private final StandardKnowledgeTasksRepository standardKnowledgeTasksRepository;
    private final StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository;
    private final IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter;
    private final FeatureModelConverter featureModelConverter;

    @Autowired
    public FeatureModelController(StandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                  StandardKnowledgeTasksLinkRepository standardKnowledgeTasksLinkRepository,
                                  IArangoStandardKnowledgeConverter arangoStandardKnowledgeConverter) {
        this.standardKnowledgeTasksRepository = standardKnowledgeTasksRepository;
        this.standardKnowledgeTasksLinkRepository = standardKnowledgeTasksLinkRepository;
        this.arangoStandardKnowledgeConverter = arangoStandardKnowledgeConverter;
        featureModelConverter = new FeatureModelConverter();
    }

    @GetMapping(value = {"/{name}"})
    ArangoStandardKnowledgeTask getFeatureModel(@PathVariable String name) {
        return standardKnowledgeTasksRepository.findOneByName(name);
    }

    private Optional<StandardKnowledgeTask> containsTaskWithName(StandardKnowledgeTask task, String name) {
        // TODO: to move to dedicated class
        return task.getName().equals(name) ? Optional.of(task) : task.getTasks().stream()
                .map((t) -> (t.getName().equals(name)) ? Optional.of(t) : containsTaskWithName(t, name))
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
            var knowledgeTask = containsTaskWithName(knowledgeTree.getTasks().get(0), k).orElseThrow(); // TODO: use getChildWithIdentity
            knowledgeTask.getTasks().forEach(c -> standardKnowledgeTasksLinkRepository.save(new ArangoStandardKnowledgeTaskLink(v, map.get(c.getName()))));
        });
    }

    @PostMapping(value = {"", "/"},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> importFeatureModel(@RequestBody String featureModelString) throws Exception {
        FeatureModel featureModel = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(featureModelString, FeatureModel.class);
        var knowledgeTree = featureModelConverter.toStandardKnowledgeTree(featureModel);
        var arangoFeatureModelTasks = arangoStandardKnowledgeConverter.fromStandardKnowledgeTree(knowledgeTree);
        var iterableUpdatedArangoTasks = standardKnowledgeTasksRepository.saveAll(arangoFeatureModelTasks);
        saveLinks(knowledgeTree, iterableUpdatedArangoTasks);
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
