package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;
import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("Configurations")
@PersistentIndex(fields = { "name" })
public class ArangoConfiguration implements GraphConfiguration<ArangoTaskVersion> {

    @Id
    private String id;
    @ArangoId
    private String name;
    private ArangoTaskVersion version;
    @Relations(edges = ArangoConfigurationToFeatureLink.class, direction = Relations.Direction.OUTBOUND)
    private List<ArangoConfigurationFeature> features;

    public ArangoConfiguration(String name, ArangoTaskVersion version, List<ArangoConfigurationFeature> features) {
        this.name = name;
        this.version = version;
        this.features = new ArrayList<>(features);
    }
}
