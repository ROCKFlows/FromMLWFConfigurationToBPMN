package com.ml2wf.app.controllers;

import com.ml2wf.app.components.BPMNComponent;
import com.ml2wf.contract.mapper.IObjectMapperFactory;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bpmn")
public class BPMNController {

    private final IObjectMapperFactory objectMapperFactory;
    private final BPMNComponent bpmnComponent;

    @Autowired
    public BPMNController(@Autowired IObjectMapperFactory objectMapperFactory,
                          @Autowired BPMNComponent bpmnComponent) {
        this.objectMapperFactory = objectMapperFactory;
        this.bpmnComponent = bpmnComponent;
    }

    @PostMapping(value = {"", "/"},
            consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@RequestParam String versionName, @RequestBody String bpmnWorkflowString)
            throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        BPMNWorkflow bpmnWorkflow = objectMapperFactory.createNewObjectMapper()
                .readValue(bpmnWorkflowString, BPMNWorkflow.class);
        bpmnComponent.importWorkflow(versionName, bpmnWorkflow); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }

    @PostMapping(value = {"/consistency"},
            consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> isBPMNWorkflowConsistent(@RequestParam String versionName,
                                                    @RequestBody String bpmnWorkflowString) throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        BPMNWorkflow bpmnWorkflow = objectMapperFactory.createNewObjectMapper()
                .readValue(bpmnWorkflowString, BPMNWorkflow.class);
        return new ResponseEntity<>(String.valueOf(bpmnComponent.isBPMNWorkflowConsistent(versionName, bpmnWorkflow)),
                HttpStatus.ACCEPTED);
    }
}
