package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoConfigurationFeatureToTaskLink {

    @Id
    private String id;
    @From
    private ArangoConfigurationFeature configuration;
    @To
    private ArangoStandardKnowledgeTask task;

    public ArangoConfigurationFeatureToTaskLink(ArangoConfigurationFeature configuration,
                                                ArangoStandardKnowledgeTask task) {
        this.configuration = configuration;
        this.task = task;
    }
}