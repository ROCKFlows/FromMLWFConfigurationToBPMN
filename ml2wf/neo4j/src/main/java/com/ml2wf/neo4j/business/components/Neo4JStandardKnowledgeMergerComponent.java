package com.ml2wf.neo4j.business.components;

import com.ml2wf.contract.business.AbstractStandardKnowledgeMergerComponent;
import com.ml2wf.neo4j.storage.converter.impl.Neo4JTasksConverter;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.repository.Neo4JStandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.repository.Neo4JVersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JStandardKnowledgeMergerComponent extends AbstractStandardKnowledgeMergerComponent<
        Neo4JStandardKnowledgeTask, Neo4JTaskVersion> {


    Neo4JStandardKnowledgeMergerComponent(@Autowired Neo4JStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                          @Autowired Neo4JVersionsRepository versionsRepository,
                                          @Autowired Neo4JTasksConverter neo4JTasksConverter) {
        super(standardKnowledgeTasksRepository, versionsRepository, neo4JTasksConverter);
    }
}
