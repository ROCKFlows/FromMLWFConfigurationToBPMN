package com.ml2wf.v3.app.business.storage.graph.converter;

import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IArangoStandardKnowledgeConverter {

    StandardKnowledgeTree toStandardKnowledgeTree(ArangoStandardKnowledgeTask arangoTreeTask);

    StandardKnowledgeTree toStandardKnowledgeTree(ArangoStandardKnowledgeTask arangoTreeTask,
                                                  List<ConstraintTree> constraintTrees);

    StandardKnowledgeTask toStandardKnowledgeTask(ArangoStandardKnowledgeTask arangoTreeTask);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree);

    List<ArangoStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);
}
