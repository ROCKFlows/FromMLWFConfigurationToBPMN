package com.ml2wf.v3.app.business.storage.graph;

import com.ml2wf.v3.app.business.storage.graph.dto.ArangoConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.constraints.ConstraintTree;
import com.ml2wf.v3.constraints.operands.AbstractOperand;
import com.ml2wf.v3.tree.StandardKnowledgeTree;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IArangoConstraintsConverter {

    ConstraintTree toConstraintTree(ArangoConstraintOperand arangoConstraintOperand);

    ArangoConstraintOperand fromAbstractOperand(AbstractOperand abstractOperand,
                                                List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks);

    ArangoConstraintOperand fromConstraintTree(ConstraintTree constraintTree,
                                               List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks);

    List<ArangoConstraintOperand> fromStandardKnowledgeTree(StandardKnowledgeTree standardKnowledgeTree,
                                                            List<ArangoStandardKnowledgeTask> arangoStandardKnowledgeTasks);
}
