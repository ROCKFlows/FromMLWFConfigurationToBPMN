package com.ml2wf.v3.app.business.storage.graph;

import com.ml2wf.v3.tree.StandardKnowledgeTask;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;

import java.util.List;

public interface IArangoStandardKnowledgeConverter {

    StandardKnowledgeTree toStandardKnowledgeTree(ArangoStandardKnowledgeTask arangoTreeTask);

    StandardKnowledgeTask toStandardKnowledgeTask(ArangoStandardKnowledgeTask arangoTreeTask);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);
}
