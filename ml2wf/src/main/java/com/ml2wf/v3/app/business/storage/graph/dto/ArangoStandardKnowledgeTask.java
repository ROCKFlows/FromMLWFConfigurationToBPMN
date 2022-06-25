package com.ml2wf.v3.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Document("StandardKnowledgeTask")
@PersistentIndex(fields = { "name" })
public class ArangoStandardKnowledgeTask {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private String description;
    @Relations(edges = ArangoStandardKnowledgeTaskLink.class, direction = Relations.Direction.OUTBOUND, lazy = true)
    private Collection<ArangoStandardKnowledgeTask> children;

    public ArangoStandardKnowledgeTask(String name, boolean isAbstract, boolean isMandatory, String description) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.description = description;
        children = new ArrayList<>();
    }
}
