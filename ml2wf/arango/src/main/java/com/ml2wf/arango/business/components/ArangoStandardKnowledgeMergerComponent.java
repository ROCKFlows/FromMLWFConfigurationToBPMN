package com.ml2wf.arango.business.components;

import com.ml2wf.arango.storage.converter.impl.ArangoTasksConverter;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.arango.storage.repository.ArangoStandardKnowledgeTasksRepository;
import com.ml2wf.arango.storage.repository.ArangoVersionsRepository;
import com.ml2wf.contract.business.AbstractStandardKnowledgeMergerComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("arango")
@Component
public class ArangoStandardKnowledgeMergerComponent extends AbstractStandardKnowledgeMergerComponent<
        ArangoStandardKnowledgeTask, ArangoTaskVersion> {


    ArangoStandardKnowledgeMergerComponent(@Autowired ArangoStandardKnowledgeTasksRepository standardKnowledgeTasksRepository,
                                           @Autowired ArangoVersionsRepository versionsRepository,
                                           @Autowired ArangoTasksConverter arangoTasksConverter) {
        super(standardKnowledgeTasksRepository, versionsRepository, arangoTasksConverter);
    }
}
