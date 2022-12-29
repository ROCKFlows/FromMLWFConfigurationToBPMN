package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoStandardKnowledgeTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArangoStandardKnowledgeTasksRepository extends ArangoRepository<GraphStandardKnowledgeTask<ArangoTaskVersion>, String>,
        StandardKnowledgeTasksRepository<ArangoTaskVersion, String> {

    Optional<GraphStandardKnowledgeTask<ArangoTaskVersion>> findOneByNameAndVersion_Name(String name, String versionName);
}
