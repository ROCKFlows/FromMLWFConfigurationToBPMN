package com.ml2wf.v2.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Document("FeatureModelTask")
public class ArangoFeatureModelTask {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private String description;
    @Relations(edges = ArangoFeatureModelTaskLink.class, lazy = true)
    private Collection<ArangoFeatureModelTask> children;

    public ArangoFeatureModelTask(String name, boolean isAbstract, boolean isMandatory, String description) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.description = description;
        children = new ArrayList<>();
    }
}
