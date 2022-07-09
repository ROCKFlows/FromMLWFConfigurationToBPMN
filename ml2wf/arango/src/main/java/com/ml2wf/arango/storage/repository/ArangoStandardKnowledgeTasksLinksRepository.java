package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTaskLink;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoStandardKnowledgeTasksLinksRepository extends ArangoRepository<ArangoStandardKnowledgeTaskLink, String> {

}
