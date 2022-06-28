package com.ml2wf.v3.app.business.storage.graph.arango.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConfigurationToFeatureLink {

    @Id
    private String id;
    @From
    private ArangoConfiguration configuration;
    @To
    private ArangoConfigurationFeature feature;

    public ArangoConfigurationToFeatureLink(ArangoConfiguration configuration, ArangoConfigurationFeature feature) {
        this.configuration = configuration;
        this.feature = feature;
    }
}