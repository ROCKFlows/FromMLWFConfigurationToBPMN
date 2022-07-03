package com.ml2wf.v3.app.business.components;

import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface IStandardKnowledgeComponent {

    StandardKnowledgeTree getStandardKnowledgeTree(String versionName);

    Optional<StandardKnowledgeTask> getTaskWithName(String taskName, String versionName);

    StandardKnowledgeTree getStandardKnowledgeTaskWithName(String taskName, String versionName);

    boolean importStandardKnowledgeTree(String versionName, StandardKnowledgeTree standardKnowledgeTree);
}
