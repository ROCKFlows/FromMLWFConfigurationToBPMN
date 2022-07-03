package com.ml2wf.v3.app.business.storage.graph.neo4j.dto;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Node("Task")
public class Neo4JStandardKnowledgeTask implements GraphStandardKnowledgeTask<Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    private Neo4JTaskVersion version;
    private String description;
    @Relationship(type = "IS_PARENT_OF", direction = Relationship.Direction.OUTGOING)
    private Collection<Neo4JStandardKnowledgeTask> children;

    public Neo4JStandardKnowledgeTask(String name, boolean isAbstract, boolean isMandatory, Neo4JTaskVersion version,
                                      String description, Collection<Neo4JStandardKnowledgeTask> children) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.version = version;
        this.description = description;
        this.children = new ArrayList<>(children);
    }
}
