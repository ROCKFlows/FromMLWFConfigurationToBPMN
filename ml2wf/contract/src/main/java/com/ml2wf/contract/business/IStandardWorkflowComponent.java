package com.ml2wf.contract.business;

import com.ml2wf.core.workflow.StandardWorkflow;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface IStandardWorkflowComponent {

    Mono<StandardWorkflow> getStandardWorkflow(String versionName);

    Mono<Boolean> importStandardWorkflow(String newVersionName, StandardWorkflow standardWorkflow);

    Mono<Boolean> isStandardWorkflowConsistent(String versionName, StandardWorkflow standardWorkflow);
}
