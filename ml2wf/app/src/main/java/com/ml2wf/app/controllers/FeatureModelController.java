package com.ml2wf.app.controllers;

import com.ml2wf.app.components.FeatureModelComponent;
import com.ml2wf.contract.business.IVersionsComponent;
import com.ml2wf.core.tree.StandardKnowledgeVersion;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModelTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fm")
public class FeatureModelController {

    private final FeatureModelComponent featureModelComponent;
    private final IVersionsComponent versionsComponent;

    @Autowired
    public FeatureModelController(@Autowired FeatureModelComponent featureModelComponent,
                                  @Autowired IVersionsComponent versionsComponent) {
        this.featureModelComponent = featureModelComponent;
        this.versionsComponent = versionsComponent;
    }

    @GetMapping(value = {"/versions/all"},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    List<StandardKnowledgeVersion> getVersions() {
        return versionsComponent.getVersions();
    }

    @GetMapping(value = {"/versions/last"},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    StandardKnowledgeVersion getLastVersion() {
        return versionsComponent.getLastVersion();
    }

    @GetMapping(value = {""}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    FeatureModel getFeatureModel(@RequestParam String versionName) {
        return featureModelComponent.getFeatureModel(versionName);
    }

    @GetMapping(value = {"/{name}"}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    FeatureModelTask getFeatureModelTask(@PathVariable String name, @RequestParam String versionName) {
        return featureModelComponent.getFeatureModelTaskWithName(name, versionName);
    }

    @PostMapping(value = {""}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> importFeatureModel(@RequestParam String versionName,
                                              @RequestBody FeatureModel featureModel) {
        featureModelComponent.importFeatureModel(versionName, featureModel); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
