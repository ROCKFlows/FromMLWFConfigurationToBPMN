package com.ml2wf.app.controllers;

import com.ml2wf.app.components.BPMNComponent;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bpmn")
public class BPMNController {
    private final BPMNComponent bpmnComponent;

    @Autowired
    public BPMNController(@Autowired BPMNComponent bpmnComponent) {
        this.bpmnComponent = bpmnComponent;
    }

    @GetMapping(value = {""}, produces = {MediaType.APPLICATION_XML_VALUE})
    BPMNWorkflow getFeatureModel(@RequestParam String versionName) {
        return bpmnComponent.getBPMNWorkflow(versionName);
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@RequestParam String newVersionName,
                                              @RequestBody BPMNWorkflow bpmnWorkflow) {
        bpmnComponent.importWorkflow(newVersionName, bpmnWorkflow); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }

    @PostMapping(value = {"/consistency"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> isBPMNWorkflowConsistent(@RequestParam String versionName,
                                                    @RequestBody BPMNWorkflow bpmnWorkflow) {
        return new ResponseEntity<>(String.valueOf(bpmnComponent.isBPMNWorkflowConsistent(versionName, bpmnWorkflow)),
                HttpStatus.ACCEPTED);
    }
}
