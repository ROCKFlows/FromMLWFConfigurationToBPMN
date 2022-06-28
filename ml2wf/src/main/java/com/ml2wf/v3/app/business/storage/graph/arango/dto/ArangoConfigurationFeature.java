package com.ml2wf.v3.app.business.storage.graph.arango.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Document("ConfigurationFeatures")
public class ArangoConfigurationFeature {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String automatic;
    private String manual;
    @Relations(edges = ArangoConfigurationFeatureToTaskLink.class, direction = Relations.Direction.OUTBOUND)
    private ArangoStandardKnowledgeTask task;
    private ArangoTaskVersion version;

    public ArangoConfigurationFeature(String automatic, String manual, ArangoStandardKnowledgeTask task,
                                      ArangoTaskVersion version) {
        this.automatic = automatic;
        this.manual = manual;
        this.task = task;
        this.version = version;
    }
}
