package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.AbstractStandardWorkflowComponent;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JWorkflowTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import com.ml2wf.neo4j.storage.repository.Neo4JConstraintsRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardWorkflowTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JStandardWorkflowComponent extends AbstractStandardWorkflowComponent<Neo4JStandardWorkflowTask,
        Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Neo4JConstraintOperand> {

    public Neo4JStandardWorkflowComponent(@Autowired Neo4JStandardWorkflowTasksRepository standardWorkflowTasksRepository,
                                          @Autowired Neo4JWorkflowTasksConverter workflowTasksConverter,
                                          @Autowired Neo4JStandardKnowledgeMergerComponent standardKnowledgeMergerComponent,
                                          @Autowired Neo4JVersionsRepository versionsRepository,
                                          @Autowired Neo4JConstraintsRepository constraintsRepository,
                                          @Autowired Neo4JConstraintsConverter constraintsConverter) {
        super(standardWorkflowTasksRepository, workflowTasksConverter, standardKnowledgeMergerComponent,
                versionsRepository, constraintsRepository, constraintsConverter);
    }
}
