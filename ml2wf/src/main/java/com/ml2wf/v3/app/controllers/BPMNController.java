package com.ml2wf.v3.app.controllers;

import com.ml2wf.v3.app.business.components.BPMNComponent;
import com.ml2wf.v3.app.workflow.custom.bpmn.BPMNWorkflow;
import com.ml2wf.v3.app.xml.XMLObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bpmn")
public class BPMNController {

    /*private final BPMNComponent bpmnComponent;

    @Autowired
    public BPMNController(@Autowired BPMNComponent bpmnComponent) {
        this.bpmnComponent = bpmnComponent;
    }

    @PostMapping(value = {"", "/"},
            consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> importBPMNWorkflow(@RequestParam String versionName, @RequestBody String bpmnWorkflowString)
            throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        BPMNWorkflow bpmnWorkflow = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(bpmnWorkflowString, BPMNWorkflow.class);
        bpmnComponent.importWorkflow(versionName, bpmnWorkflow); // TODO: check result
        return new ResponseEntity<>("OK", HttpStatus.ACCEPTED);
    }

    @PostMapping(value = {"/consistency"},
            consumes = {MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<String> isBPMNWorkflowConsistent(@RequestParam String versionName,
                                                    @RequestBody String bpmnWorkflowString) throws Exception {
        // TODO: use jackson to automatically convert requestbody to featureModel
        BPMNWorkflow bpmnWorkflow = XMLObjectMapperFactory.getInstance()
                .createNewObjectMapper()
                .readValue(bpmnWorkflowString, BPMNWorkflow.class);
        return new ResponseEntity<>(String.valueOf(bpmnComponent.isBPMNWorkflowConsistent(versionName, bpmnWorkflow)),
                HttpStatus.ACCEPTED);
    }*/
}
