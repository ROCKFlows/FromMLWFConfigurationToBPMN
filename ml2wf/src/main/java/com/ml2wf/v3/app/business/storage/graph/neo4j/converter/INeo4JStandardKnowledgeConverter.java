package com.ml2wf.v3.app.business.storage.graph.neo4j.converter;

import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.tree.StandardKnowledgeTask;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface INeo4JStandardKnowledgeConverter {

    StandardKnowledgeTree toStandardKnowledgeTree(Neo4JStandardKnowledgeTask standardKnowledgeTask);

    StandardKnowledgeTree toStandardKnowledgeTree(Neo4JStandardKnowledgeTask standardKnowledgeTask,
                                                  List<ConstraintTree> constraintTrees);

    StandardKnowledgeTask toStandardKnowledgeTask(Neo4JStandardKnowledgeTask standardKnowledgeTask);

    List<Neo4JStandardKnowledgeTask> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree);

    List<Neo4JStandardKnowledgeTask> fromStandardKnowledgeTask(StandardKnowledgeTask standardKnowledgeTask);
}
