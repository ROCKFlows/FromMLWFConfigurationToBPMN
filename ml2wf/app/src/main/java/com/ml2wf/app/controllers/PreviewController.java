package com.ml2wf.app.controllers;

import com.ml2wf.core.configurations.RawConfiguration;
import com.ml2wf.core.tree.custom.featuremodel.FeatureModel;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/preview")
public class PreviewController {

    @PostMapping(value = {"/fm"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    FeatureModel getFeatureModelPreview(@RequestBody FeatureModel featureModel) {
        return featureModel;
    }

    @PostMapping(value = {"/bpmn"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    BPMNWorkflow getBPMNWorkflowPreview(@RequestBody BPMNWorkflow bpmnWorkflow) {
        return bpmnWorkflow;
    }

    @PostMapping(value = {"/configuration"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    RawConfiguration getConfigurationPreview(@RequestBody RawConfiguration configuration) {
        return configuration;
    }
}
