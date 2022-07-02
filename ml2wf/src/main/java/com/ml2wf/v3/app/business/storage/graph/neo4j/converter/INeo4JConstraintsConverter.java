package com.ml2wf.v3.app.business.storage.graph.neo4j.converter;

import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface INeo4JConstraintsConverter {

    ConstraintTree toConstraintTree(Neo4JConstraintOperand constraintOperand);

    Neo4JConstraintOperand fromAbstractOperand(AbstractOperand abstractOperand,
                                               List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks);

    Neo4JConstraintOperand fromConstraintTree(ConstraintTree constraintTree,
                                              List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks);

    List<Neo4JConstraintOperand> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree,
                                                           List<Neo4JStandardKnowledgeTask> standardKnowledgeTasks);
}
