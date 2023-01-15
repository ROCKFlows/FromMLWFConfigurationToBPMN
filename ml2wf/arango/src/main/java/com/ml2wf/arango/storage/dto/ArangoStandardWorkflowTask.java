package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import com.arangodb.springframework.annotation.Relations;
import com.ml2wf.contract.storage.graph.dto.AbstractGraphWorkflowTask;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("WorkflowTask")
@PersistentIndex(fields = { "name", "version" })
public class ArangoStandardWorkflowTask extends AbstractGraphWorkflowTask<ArangoStandardWorkflowTask, ArangoTaskVersion> {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private ArangoTaskVersion version;
    @Relations(edges = ArangoStandardWorkflowTaskLink.class, direction = Relations.Direction.OUTBOUND, lazy = true)
    private ArangoStandardWorkflowTask nextTask;

    public ArangoStandardWorkflowTask(String name, boolean isAbstract, boolean isMandatory, ArangoTaskVersion version,
                                      String description, ArangoStandardWorkflowTask nextTask) {
        super(name, isAbstract, isMandatory, description);
        this.version = version;
        this.nextTask = nextTask;
    }
}
