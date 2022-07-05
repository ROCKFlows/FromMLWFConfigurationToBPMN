package com.ml2wf.v3.app.business.components.arango;

import com.ml2wf.v3.app.business.components.StandardWorkflowComponent;
import com.ml2wf.v3.app.business.storage.graph.arango.converter.impl.ArangoConstraintsConverter;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.arango.repository.ArangoConstraintsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("arango")
@Component
public class ArangoStandardWorkflowComponent extends StandardWorkflowComponent<ArangoStandardKnowledgeTask,
        ArangoConstraintOperand, ArangoTaskVersion> {

    public ArangoStandardWorkflowComponent(@Autowired ArangoStandardKnowledgeComponent standardKnowledgeComponent,
                                           @Autowired ArangoConstraintsRepository constraintsRepository,
                                           @Autowired ArangoConstraintsConverter constraintsConverter) {
        super(standardKnowledgeComponent, constraintsRepository, constraintsConverter);
    }
}
