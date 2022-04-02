package com.ml2wf.v2.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoFeatureModelTaskLink {

    @Id
    private String id;
    @From
    private ArangoFeatureModelTask parent;
    @To
    private ArangoFeatureModelTask child;

    public ArangoFeatureModelTaskLink(ArangoFeatureModelTask parent, ArangoFeatureModelTask child) {
        this.parent = parent;
        this.child = child;
    }
}