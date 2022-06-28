package com.ml2wf.v3.app.business.storage.graph.arango.converter;

import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.constraints.ConstraintTree;
import com.ml2wf.v3.app.constraints.operands.AbstractOperand;
import com.ml2wf.v3.app.tree.StandardKnowledgeTree;
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
