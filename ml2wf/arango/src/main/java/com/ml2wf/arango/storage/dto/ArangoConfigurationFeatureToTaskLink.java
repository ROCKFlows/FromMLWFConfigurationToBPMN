package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConfigurationFeatureToTaskLink {

    @Id
    private String id;
    @From
    private GraphConfigurationFeature<ArangoTaskVersion> configuration;
    @To
    private GraphStandardKnowledgeTask<ArangoTaskVersion> task;

    public ArangoConfigurationFeatureToTaskLink(GraphConfigurationFeature<ArangoTaskVersion> configuration,
                                                GraphStandardKnowledgeTask<ArangoTaskVersion> task) {
        this.configuration = configuration;
        this.task = task;
    }
}