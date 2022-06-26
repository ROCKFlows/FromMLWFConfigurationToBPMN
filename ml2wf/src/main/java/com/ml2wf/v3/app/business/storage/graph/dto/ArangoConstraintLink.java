package com.ml2wf.v3.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConstraintLink {

    @Id
    private String id;
    @From
    private ArangoConstraintOperand parent;
    @To
    private ArangoConstraintOperand child;

    public ArangoConstraintLink(ArangoConstraintOperand parent, ArangoConstraintOperand child) {
        this.parent = parent;
        this.child = child;
    }
}