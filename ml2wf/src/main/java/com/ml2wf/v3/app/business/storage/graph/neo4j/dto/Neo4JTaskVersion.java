package com.ml2wf.v3.app.business.storage.graph.neo4j.dto;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Version")
public class Neo4JTaskVersion {

    @Id
    private String name;
    private int major;
    private int minor;
    private int patch;

    public Neo4JTaskVersion(int major, int minor, int patch, String name) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.name = name;
    }
}
