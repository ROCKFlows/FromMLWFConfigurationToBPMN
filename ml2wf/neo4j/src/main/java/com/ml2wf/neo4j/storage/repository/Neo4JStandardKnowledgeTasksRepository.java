package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Neo4JStandardKnowledgeTasksRepository extends Neo4jRepository<Neo4JStandardKnowledgeTask, Long>,
        StandardKnowledgeTasksRepository<Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Long> {

    // TODO: fix hardcoded __ROOT task name
    @Override
    @Query("""
            MATCH (r:Task {name: $taskName})<-[:IS_PARENT_OF*0..]-(:Task {name: '__ROOT'})-[:IS_VERSIONED_BY*]->({name: $versionName}), f = (c:Task)<-[:IS_PARENT_OF*0..]-(r)
            RETURN r, collect(nodes(f)), collect(relationships(f))
            """)
    Optional<Neo4JStandardKnowledgeTask> findOneByNameAndVersionName(String taskName, String versionName);
}
