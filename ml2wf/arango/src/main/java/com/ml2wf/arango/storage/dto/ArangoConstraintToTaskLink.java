package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConstraintToTaskLink {

    @Id
    private String id;
    @From
    private GraphConstraintOperand<ArangoTaskVersion> operand;
    @To
    private GraphStandardKnowledgeTask<ArangoTaskVersion> task;

    public ArangoConstraintToTaskLink(GraphConstraintOperand<ArangoTaskVersion> operand,
                                      GraphStandardKnowledgeTask<ArangoTaskVersion> task) {
        this.operand = operand;
        this.task = task;
    }
}