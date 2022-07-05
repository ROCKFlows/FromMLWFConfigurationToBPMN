package com.ml2wf.v3.app.business.components.neo4j;

import com.ml2wf.v3.app.business.components.StandardWorkflowComponent;
import com.ml2wf.v3.app.business.storage.graph.neo4j.converter.impl.Neo4JConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.neo4j.repository.Neo4JConstraintsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("neo4j")
@Component
public class Neo4JStandardWorkflowComponent extends StandardWorkflowComponent<Neo4JStandardKnowledgeTask,
        Neo4JConstraintOperand, Neo4JTaskVersion>  {

    public Neo4JStandardWorkflowComponent(@Autowired Neo4JStandardKnowledgeComponent standardKnowledgeComponent,
                                          @Autowired Neo4JConstraintsRepository constraintsRepository,
                                          @Autowired Neo4JConstraintsConverter constraintsConverter) {
        super(standardKnowledgeComponent, constraintsRepository, constraintsConverter);
    }
}
