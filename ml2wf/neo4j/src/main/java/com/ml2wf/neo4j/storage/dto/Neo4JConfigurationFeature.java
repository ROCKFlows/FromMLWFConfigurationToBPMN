package com.ml2wf.neo4j.storage.dto;

import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("ConfigurationFeature")
public class Neo4JConfigurationFeature implements GraphConfigurationFeature<Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {

    @Id @GeneratedValue
    private Long id;
    private String automatic;
    private String manual;
    @Relationship(type = "REFERS_TO", direction = Relationship.Direction.OUTGOING)
    private Neo4JStandardKnowledgeTask task;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    private Neo4JTaskVersion version;

    public Neo4JConfigurationFeature(String automatic, String manual, Neo4JStandardKnowledgeTask task,
                                     Neo4JTaskVersion version) {
        this.automatic = automatic;
        this.manual = manual;
        this.task = task;
        this.version = version;
    }
}
