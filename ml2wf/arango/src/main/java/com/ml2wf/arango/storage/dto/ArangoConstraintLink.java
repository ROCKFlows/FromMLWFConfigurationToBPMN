package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConstraintLink {

    @Id
    private String id;
    @From
    private GraphConstraintOperand<ArangoTaskVersion> parent;
    @To
    private GraphConstraintOperand<ArangoTaskVersion> child;

    public ArangoConstraintLink(GraphConstraintOperand<ArangoTaskVersion> parent,
                                GraphConstraintOperand<ArangoTaskVersion> child) {
        this.parent = parent;
        this.child = child;
    }
}