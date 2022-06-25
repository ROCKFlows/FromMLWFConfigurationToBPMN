package com.ml2wf.v3.app.business.storage.graph;

import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IArangoStandardKnowledgeConverter {

    StandardKnowledgeTree toStandardKnowledgeTree(ArangoStandardKnowledgeTask arangoTreeTask);

    StandardKnowledgeTask toStandardKnowledgeTask(ArangoStandardKnowledgeTask arangoTreeTask);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);
}
