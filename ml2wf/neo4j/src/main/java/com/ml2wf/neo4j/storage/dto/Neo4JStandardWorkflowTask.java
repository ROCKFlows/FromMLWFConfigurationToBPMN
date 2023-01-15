package com.ml2wf.neo4j.storage.dto;

import com.ml2wf.contract.storage.graph.dto.AbstractGraphWorkflowTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.annotation.Nullable;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("WorkflowTask")
public class Neo4JStandardWorkflowTask extends AbstractGraphWorkflowTask<Neo4JStandardWorkflowTask, Neo4JTaskVersion> {

    @Id @GeneratedValue
    private Long id;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    @Nullable
    private Neo4JTaskVersion version;
    @Relationship(type = "PRECEDES", direction = Relationship.Direction.OUTGOING)
    @Nullable
    private Neo4JStandardWorkflowTask nextTask;

    public Neo4JStandardWorkflowTask(String name, boolean isAbstract, boolean isMandatory, @Nullable Neo4JTaskVersion version,
                                     String description, @Nullable Neo4JStandardWorkflowTask nextTask) {
        super(name, isAbstract, isMandatory, description);
        this.version = version;
        this.nextTask = nextTask;
    }
}
