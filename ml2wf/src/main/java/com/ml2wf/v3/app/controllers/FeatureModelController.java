package com.ml2wf.v3.app.controllers;

import com.ml2wf.v3.app.business.components.FeatureModelComponent;
import com.ml2wf.v3.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.v3.xml.XMLObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fm")
public class FeatureModelController {

    private final FeatureModelComponent featureModelComponent;

    @Autowired
    public FeatureModelController(@Autowired FeatureModelComponent featureModelComponent) {
        this.featureModelComponent = featureModelComponent;
    }

    @GetMapping(value = {"/{name}"})
    FeatureModel getFeatureModel(@PathVariable String name) {
        return featureModelComponent.getFeatureModelWithName(name);
    }

    @PostMapping(value = {"", "/"},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    ResponseEntity<String> importFeatureModel(@RequestBody String featureModelString) throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        FeatureModel featureModel = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(featureModelString, FeatureModel.class);
        featureModelComponent.importFeatureModel(featureModel); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }
}
