package com.ml2wf.v3.app.business.storage.graph.arango.dto;

import com.arangodb.springframework.annotation.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Document("StandardKnowledgeTask")
@PersistentIndex(fields = { "name", "version" })
public class ArangoStandardKnowledgeTask {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String name;
    private boolean isAbstract;
    private boolean isMandatory;
    private ArangoTaskVersion version;
    private String description;
    @Relations(edges = ArangoStandardKnowledgeTaskLink.class, direction = Relations.Direction.OUTBOUND, lazy = true)
    private Collection<ArangoStandardKnowledgeTask> children;

    public ArangoStandardKnowledgeTask(String name, boolean isAbstract, boolean isMandatory, ArangoTaskVersion version,
                                       String description) {
        this.name = name;
        this.isAbstract = isAbstract;
        this.isMandatory = isMandatory;
        this.version = version;
        this.description = description;
        children = new ArrayList<>();
    }
}
