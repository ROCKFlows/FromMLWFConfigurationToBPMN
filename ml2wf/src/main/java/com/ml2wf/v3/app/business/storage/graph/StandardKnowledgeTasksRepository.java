package com.ml2wf.v3.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;

public interface StandardKnowledgeTasksRepository extends ArangoRepository<ArangoStandardKnowledgeTask, String> {
    ArangoStandardKnowledgeTask findOneByName(String name);
}
