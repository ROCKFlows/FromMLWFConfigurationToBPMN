package com.ml2wf.contract.business;

import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface IStandardKnowledgeMergerComponent {

    Mono<Void> mergeWorkflowWithTree(String version, StandardWorkflow workflow);
}
