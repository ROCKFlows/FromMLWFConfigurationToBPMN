package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoStandardWorkflowTask;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.StandardWorkflowTasksRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArangoStandardWorkflowTasksRepository extends ArangoRepository<ArangoStandardWorkflowTask, String>,
        StandardWorkflowTasksRepository<ArangoStandardWorkflowTask, ArangoTaskVersion, String> {

}
