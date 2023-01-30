package com.ml2wf.app.components;

import com.ml2wf.contract.business.IStandardWorkflowComponent;
import com.ml2wf.core.workflow.converter.BPMNWorkflowConverter;
import com.ml2wf.core.workflow.custom.bpmn.BPMNWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BPMNComponent {

    // TODO: intercept this method return and convert automatically standard workflow to bpmn (Aspect4J ?)

    private final IStandardWorkflowComponent standardWorkflowComponent;
    private final BPMNWorkflowConverter bpmnWorkflowConverter; // TODO: make autowirable

    public BPMNComponent(@Autowired IStandardWorkflowComponent standardWorkflowComponent) {
        this.standardWorkflowComponent = standardWorkflowComponent;
        bpmnWorkflowConverter = new BPMNWorkflowConverter();
    }

    public Mono<BPMNWorkflow> getBPMNWorkflow(String versionName) {
        return standardWorkflowComponent.getStandardWorkflow(versionName)
                .map(bpmnWorkflowConverter::fromStandardWorkflow);
    }

    public Mono<Boolean> importWorkflow(String newVersionName, BPMNWorkflow bpmnWorkflow) {
        var standardWorkflow = bpmnWorkflowConverter.toStandardWorkflow(bpmnWorkflow);
        return standardWorkflowComponent.importStandardWorkflow(newVersionName, standardWorkflow);
    }

    public Mono<Boolean> isBPMNWorkflowConsistent(String versionName, BPMNWorkflow bpmnWorkflow) {
        var standardWorkflow = bpmnWorkflowConverter.toStandardWorkflow(bpmnWorkflow);
        return standardWorkflowComponent.isStandardWorkflowConsistent(versionName, standardWorkflow);
    }
}
