package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Document("ConfigurationFeatures")
public class ArangoConfigurationFeature implements GraphConfigurationFeature<ArangoTaskVersion> {

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
