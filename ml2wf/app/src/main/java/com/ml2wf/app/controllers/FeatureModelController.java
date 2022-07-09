package com.ml2wf.app.controllers;

import com.ml2wf.app.components.FeatureModelComponent;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fm")
public class FeatureModelController {

    private final IObjectMapperFactory objectMapperFactory;
    private final FeatureModelComponent featureModelComponent;

    @Autowired
    public FeatureModelController(@Autowired IObjectMapperFactory objectMapperFactory,
                                  @Autowired FeatureModelComponent featureModelComponent) {
        this.objectMapperFactory = objectMapperFactory;
        this.featureModelComponent = featureModelComponent;
    }

    @GetMapping(value = {""})
    FeatureModel getFeatureModel(@RequestParam String versionName) {
        return featureModelComponent.getFeatureModel(versionName);
    }

    @GetMapping(value = {"/{name}"})
    FeatureModel getFeatureModelTask(@PathVariable String name, @RequestParam String versionName) {
        return featureModelComponent.getFeatureModelTaskWithName(name, versionName);
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importFeatureModel(@RequestParam String versionName, @RequestBody String featureModelString)
            throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        FeatureModel featureModel = objectMapperFactory.createNewObjectMapper()
                .readValue(featureModelString, FeatureModel.class);
        featureModelComponent.importFeatureModel(versionName, featureModel); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
