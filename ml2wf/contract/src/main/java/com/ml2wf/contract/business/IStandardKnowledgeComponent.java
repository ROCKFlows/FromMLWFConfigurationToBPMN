package com.ml2wf.contract.business;

import com.ml2wf.core.tree.StandardKnowledgeTask;
import com.ml2wf.core.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IStandardKnowledgeComponent {

    StandardKnowledgeTree getStandardKnowledgeTree(String versionName);

    Optional<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName);

    StandardKnowledgeTask getStandardKnowledgeTaskWithName(String taskName, String versionName);

    boolean importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree);
}
