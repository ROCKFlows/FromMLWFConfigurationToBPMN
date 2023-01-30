package com.ml2wf.app.controllers;

import com.ml2wf.app.components.BPMNComponent;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bpmn")
public class BPMNController {
    private final BPMNComponent bpmnComponent;

    @Autowired
    public BPMNController(@Autowired BPMNComponent bpmnComponent) {
        this.bpmnComponent = bpmnComponent;
    }

    @GetMapping(value = {""}, produces = {MediaType.APPLICATION_XML_VALUE})
    Mono<BPMNWorkflow> getFeatureModel(@RequestParam String versionName) {
        return bpmnComponent.getBPMNWorkflow(versionName);
    }

    @PostMapping(value = {"", "/"}, consumes = {MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<String> importBPMNWorkflow(@RequestParam String newVersionName,
                                    @RequestBody BPMNWorkflow bpmnWorkflow) {
        return bpmnComponent.importWorkflow(newVersionName, bpmnWorkflow)
                .map((t) -> "OK"); // TODO: check result
    }

    @PostMapping(value = {"/consistency"}, consumes = {MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    Mono<Boolean> isBPMNWorkflowConsistent(@RequestParam String versionName,
                                                    @RequestBody BPMNWorkflow bpmnWorkflow) {
        return bpmnComponent.isBPMNWorkflowConsistent(versionName, bpmnWorkflow);
    }
}
