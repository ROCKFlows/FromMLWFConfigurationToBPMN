package com.ml2wf.v3.app.business.storage.graph.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardKnowledgeTasksLinkRepository extends ArangoRepository<ArangoStandardKnowledgeTaskLink, String> {

}
