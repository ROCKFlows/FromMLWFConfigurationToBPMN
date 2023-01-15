package com.ml2wf.arango.storage.dto;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Edge
public class ArangoStandardWorkflowTaskLink {

    @Id
    private String id;
    @From
    private ArangoStandardKnowledgeTask precedingTask;
    @To
    private ArangoStandardKnowledgeTask succeedingTask;

    public ArangoStandardWorkflowTaskLink(ArangoStandardKnowledgeTask precedingTask,
                                          ArangoStandardKnowledgeTask succeedingTask) {
        this.precedingTask = precedingTask;
        this.succeedingTask = succeedingTask;
    }
}