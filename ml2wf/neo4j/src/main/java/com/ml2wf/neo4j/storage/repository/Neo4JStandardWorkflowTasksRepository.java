package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.StandardWorkflowTasksRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardWorkflowTask;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Neo4JStandardWorkflowTasksRepository extends Neo4jRepository<Neo4JStandardWorkflowTask, Long>,
        StandardWorkflowTasksRepository<Neo4JStandardWorkflowTask, Neo4JTaskVersion, Long> {

    // TODO: fix hardcoded __ROOT_WORKFLOW task name
    @Override
    @Query("""
            MATCH (r:WorkflowTask {name: $taskName})<-[:PRECEDES*0..]-(:WorkflowTask {name: '__ROOT_WORKFLOW'})-[:IS_VERSIONED_BY*]->({name: $versionName}), f = (c:WorkflowTask)<-[:PRECEDES*]-(r)
            RETURN r, collect(nodes(f)), collect(relationships(f))
            """)
    Optional<Neo4JStandardWorkflowTask> findOneByNameAndVersionName(String taskName, String versionName);
}
