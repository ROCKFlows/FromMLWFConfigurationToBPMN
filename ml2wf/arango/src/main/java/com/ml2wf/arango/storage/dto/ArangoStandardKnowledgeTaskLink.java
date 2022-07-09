package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoStandardKnowledgeTaskLink {

    @Id
    private String id;
    @From
    private ArangoStandardKnowledgeTask parent;
    @To
    private ArangoStandardKnowledgeTask child;

    public ArangoStandardKnowledgeTaskLink(ArangoStandardKnowledgeTask parent, ArangoStandardKnowledgeTask child) {
        this.parent = parent;
        this.child = child;
    }
}