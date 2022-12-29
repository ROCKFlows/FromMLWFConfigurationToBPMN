package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoStandardKnowledgeTaskLink {

    @Id
    private String id;
    @From
    private GraphStandardKnowledgeTask<ArangoTaskVersion> parent;
    @To
    private GraphStandardKnowledgeTask<ArangoTaskVersion> child;

    public ArangoStandardKnowledgeTaskLink(GraphStandardKnowledgeTask<ArangoTaskVersion> parent,
                                           GraphStandardKnowledgeTask<ArangoTaskVersion>child) {
        this.parent = parent;
        this.child = child;
    }
}