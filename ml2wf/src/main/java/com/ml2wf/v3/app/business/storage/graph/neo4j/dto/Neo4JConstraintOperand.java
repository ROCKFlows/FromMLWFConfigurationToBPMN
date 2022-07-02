package com.ml2wf.v3.app.business.storage.graph.neo4j.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@Node("Constraint")
public class Neo4JConstraintOperand {

    // TODO: support description

    @Id @GeneratedValue
    private Long id;
    private String type;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    private Neo4JTaskVersion version; // we should remove it as we can access it from the constrained task
    @Relationship(type = "IS_PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Collection<Neo4JConstraintOperand> operands;
    @Relationship(type = "CONSTRAINS", direction = Relationship.Direction.OUTGOING)
    private Neo4JStandardKnowledgeTask task;

    public Neo4JConstraintOperand(String type, Neo4JTaskVersion version, Collection<Neo4JConstraintOperand> operands) {
        this.type = type;
        this.version = version;
        this.operands = new ArrayList<>(operands);
        task = null;
    }

    public Neo4JConstraintOperand(String type, Neo4JTaskVersion version, Neo4JStandardKnowledgeTask task) {
        this.type = type;
        this.version = version;
        operands = new ArrayList<>();
        this.task = task;
    }
}
