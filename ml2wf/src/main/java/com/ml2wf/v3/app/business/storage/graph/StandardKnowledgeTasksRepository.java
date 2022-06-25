package com.ml2wf.v3.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoStandardKnowledgeTask;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardKnowledgeTasksRepository extends ArangoRepository<ArangoStandardKnowledgeTask, String> {
    Optional<ArangoStandardKnowledgeTask> findOneByNameAndVersion_Name(String name, String versionName);
}
