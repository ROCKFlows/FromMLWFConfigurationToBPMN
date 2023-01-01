package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArangoStandardKnowledgeTasksRepository extends ArangoRepository<ArangoStandardKnowledgeTask, String>,
        StandardKnowledgeTasksRepository<ArangoStandardKnowledgeTask, ArangoTaskVersion, String> {

    Optional<ArangoStandardKnowledgeTask> findOneByNameAndVersion_Name(String name, String versionName);
}
