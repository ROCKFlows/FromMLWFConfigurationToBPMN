package com.ml2wf.contract.business;

import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface IStandardKnowledgeComponent {

    Mono<StandardKnowledgeTree> getStandardKnowledgeTree(String versionName);

    Mono<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName);

    Mono<StandardKnowledgeTree> getStandardKnowledgeTaskWithName(String taskName, String versionName);

    Mono<Boolean> importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree);
}
