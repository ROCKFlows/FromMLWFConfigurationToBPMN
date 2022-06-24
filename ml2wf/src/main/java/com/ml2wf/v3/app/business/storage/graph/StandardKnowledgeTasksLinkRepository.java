package com.ml2wf.v3.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTaskLink;

public interface StandardKnowledgeTasksLinkRepository extends ArangoRepository<ArangoStandardKnowledgeTaskLink, String> {

}
