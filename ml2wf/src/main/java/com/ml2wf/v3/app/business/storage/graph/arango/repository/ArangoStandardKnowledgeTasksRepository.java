package com.ml2wf.v3.app.business.storage.graph.arango.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.arango.dto.ArangoTaskVersion;
import com.ml2wf.v3.app.business.storage.graph.contracts.repository.StandardKnowledgeTasksRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArangoStandardKnowledgeTasksRepository extends ArangoRepository<ArangoStandardKnowledgeTask, String>,
        StandardKnowledgeTasksRepository<ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

    Optional<ArangoStandardKnowledgeTask> findOneByNameAndVersion_Name(String name, String versionName);
}
