package com.ml2wf.arango.business.components;

import com.ml2wf.arango.storage.converter.impl.ArangoConstraintsConverter;
import com.ml2wf.arango.storage.dto.ArangoConstraintOperand;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.arango.storage.repository.ArangoConstraintsRepository;
import com.ml2wf.contract.business.AbstractStandardWorkflowComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("arango")
@Component
public class ArangoStandardWorkflowComponent extends AbstractStandardWorkflowComponent<ArangoStandardKnowledgeTask,
        ArangoConstraintOperand, ArangoTaskVersion> {

    public ArangoStandardWorkflowComponent(@Autowired ArangoStandardKnowledgeComponent standardKnowledgeComponent,
                                           @Autowired ArangoConstraintsRepository constraintsRepository,
                                           @Autowired ArangoConstraintsConverter constraintsConverter) {
        super(standardKnowledgeComponent, constraintsRepository, constraintsConverter);
    }
}
