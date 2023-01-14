package com.ml2wf.neo4j.storage.dto;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.annotation.Nullable;

@Data
@Node("Version")
public class Neo4JTaskVersion implements GraphTaskVersion {

    @Id
    private String name;
    @Relationship(type = "PRECEDES", direction = Relationship.Direction.INCOMING)
    @Nullable
    private Neo4JTaskVersion version;
    private int major;
    private int minor;
    private int patch;

    public Neo4JTaskVersion(int major, int minor, int patch, String name, @Nullable Neo4JTaskVersion version) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.name = name;
        this.version = version;
    }
}
