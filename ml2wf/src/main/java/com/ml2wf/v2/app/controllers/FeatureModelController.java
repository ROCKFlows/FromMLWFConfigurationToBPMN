package com.ml2wf.v2.app.controllers;

import com.ml2wf.v2.app.business.storage.graph.FeatureModelRepository;
import com.ml2wf.v2.app.business.storage.graph.FeatureModelTaskLinkRepository;
import com.ml2wf.v2.app.business.storage.graph.IArangoFeatureModelConverter;
import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTask;
import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTaskLink;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.xml.XMLObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/fm")
public class FeatureModelController {

    private final FeatureModelRepository featureModelRepository;
    private final FeatureModelTaskLinkRepository featureModelTaskLinkRepository;
    private final IArangoFeatureModelConverter arangoFeatureModelConverter;

    @Autowired
    public FeatureModelController(FeatureModelRepository featureModelRepository,
                                  FeatureModelTaskLinkRepository featureModelTaskLinkRepository,
                                  IArangoFeatureModelConverter arangoFeatureModelConverter) {
        this.featureModelRepository = featureModelRepository;
        this.featureModelTaskLinkRepository = featureModelTaskLinkRepository;
        this.arangoFeatureModelConverter = arangoFeatureModelConverter;
    }

    @GetMapping(value = {"/{name}"})
    ArangoFeatureModelTask getFeatureModel(@PathVariable String name) {
        return featureModelRepository.findOneByName(name);
    }

    private void saveLinks(FeatureModel featureModel, Iterable<ArangoFeatureModelTask> arangoFeatureModelTasksIterable) {
        Map<String, ArangoFeatureModelTask> map = StreamSupport.stream(arangoFeatureModelTasksIterable.spliterator(), false)
                .collect(Collectors.toMap(ArangoFeatureModelTask::getName, t -> t));
        map.forEach((k, v) -> {
            var fmTask = featureModel.getChildWithIdentity(k).orElseThrow();
            fmTask.getChildren().forEach(c -> featureModelTaskLinkRepository.save(new ArangoFeatureModelTaskLink(v, map.get(c.getName()))));
        });
    }

    @PostMapping(value = {"", "/"},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> importFeatureModel(@RequestBody String featureModelString) throws Exception {
        FeatureModel featureModel = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(featureModelString, FeatureModel.class);
        var arangoFeatureModelTasks = arangoFeatureModelConverter.fromRawFeatureModel(featureModel);
        var iterableUpdatedArangoTasks = featureModelRepository.saveAll(arangoFeatureModelTasks);
        saveLinks(featureModel, iterableUpdatedArangoTasks);
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
