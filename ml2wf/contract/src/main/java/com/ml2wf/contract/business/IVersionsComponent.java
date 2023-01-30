package com.ml2wf.contract.business;

import com.ml2wf.core.tree.StandardKnowledgeVersion;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public interface IVersionsComponent {

    Mono<StandardKnowledgeVersion> getLastVersion();

    Flux<StandardKnowledgeVersion> getVersions();
}
