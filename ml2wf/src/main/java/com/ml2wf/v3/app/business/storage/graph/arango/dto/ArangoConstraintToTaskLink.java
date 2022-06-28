package com.ml2wf.v3.app.business.storage.graph.arango.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConstraintToTaskLink {

    @Id
    private String id;
    @From
    private ArangoConstraintOperand operand;
    @To
    private ArangoStandardKnowledgeTask task;

    public ArangoConstraintToTaskLink(ArangoConstraintOperand operand, ArangoStandardKnowledgeTask task) {
        this.operand = operand;
        this.task = task;
    }
}