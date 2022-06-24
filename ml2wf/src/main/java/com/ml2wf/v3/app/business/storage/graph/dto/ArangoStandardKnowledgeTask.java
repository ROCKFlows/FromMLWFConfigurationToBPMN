package com.ml2wf.v3.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Relations;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Document("StandardKnowledgeTask")
public class ArangoStandardKnowledgeTask {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private String description;
    @Relations(edges = ArangoStandardKnowledgeTaskLink.class, lazy = true)
    private Collection<ArangoStandardKnowledgeTask> children;

    public ArangoStandardKnowledgeTask(String name, boolean isAbstract, boolean isMandatory, String description) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.description = description;
        children = new ArrayList<>();
    }
}
