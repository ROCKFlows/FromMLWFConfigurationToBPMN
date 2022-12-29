package com.ml2wf.neo4j.storage.dto;

import com.ml2wf.contract.storage.graph.dto.AbstractGraphKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("Task")
public class Neo4JStandardKnowledgeTask extends AbstractGraphKnowledgeTask<Neo4JTaskVersion> {

    @Id @GeneratedValue
    private Long id;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    private Neo4JTaskVersion version;
    @Relationship(type = "IS_PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Collection<GraphStandardKnowledgeTask<Neo4JTaskVersion>> children;

    public Neo4JStandardKnowledgeTask(String name, boolean isAbstract, boolean isMandatory, Neo4JTaskVersion version,
                                      String description, Collection<GraphStandardKnowledgeTask<Neo4JTaskVersion>> children) {
        super(name, isAbstract, isMandatory, description);
        this.version = version;
        this.children = new ArrayList<>(children);
    }
}
