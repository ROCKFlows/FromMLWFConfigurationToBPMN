package com.ml2wf.neo4j.storage.dto;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node("Configuration")
public class Neo4JConfiguration implements GraphConfiguration<Neo4JTaskVersion> {

    @Id
    private String name;
    @Relationship(type = "IS_VERSIONED_BY", direction = Relationship.Direction.OUTGOING)
    private Neo4JTaskVersion version;
    @Relationship(type = "REFERS_TO", direction = Relationship.Direction.OUTGOING)
    private List<Neo4JConfigurationFeature> features;

    public Neo4JConfiguration(String name, Neo4JTaskVersion version, List<Neo4JConfigurationFeature> features) {
        this.name = name;
        this.version = version;
        this.features = new ArrayList<>(features);
    }
}