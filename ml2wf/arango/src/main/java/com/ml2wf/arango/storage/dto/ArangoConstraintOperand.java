package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;
import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@Document("Constraints")
@PersistentIndex(fields = { "version" })
public class ArangoConstraintOperand implements GraphConstraintOperand<ArangoTaskVersion> {

    // TODO: support description

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private String type;
    private ArangoTaskVersion version;
    @Relations(edges = ArangoConstraintLink.class, direction = Relations.Direction.OUTBOUND, lazy = true)
    private Collection<GraphConstraintOperand<ArangoTaskVersion>> operands;
    @Relations(edges = ArangoConstraintToTaskLink.class, direction = Relations.Direction.OUTBOUND)
    private GraphStandardKnowledgeTask<ArangoTaskVersion> task;

    public ArangoConstraintOperand(String type, ArangoTaskVersion version,
                                   Collection<GraphConstraintOperand<ArangoTaskVersion>> operands) {
        this.type = type;
        this.version = version;
        this.operands = new ArrayList<>(operands);
        task = null;
    }

    public ArangoConstraintOperand(String type, ArangoTaskVersion version, GraphStandardKnowledgeTask<ArangoTaskVersion> task) {
        this.type = type;
        this.version = version;
        operands = new ArrayList<>();
        this.task = task;
    }
}
