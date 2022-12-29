package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConfigurationToFeatureLink {

    @Id
    private String id;
    @From
    private GraphConfiguration<ArangoTaskVersion> configuration;
    @To
    private GraphConfigurationFeature<ArangoTaskVersion> feature;

    public ArangoConfigurationToFeatureLink(GraphConfiguration<ArangoTaskVersion> configuration,
                                            GraphConfigurationFeature<ArangoTaskVersion> feature) {
        this.configuration = configuration;
        this.feature = feature;
    }
}