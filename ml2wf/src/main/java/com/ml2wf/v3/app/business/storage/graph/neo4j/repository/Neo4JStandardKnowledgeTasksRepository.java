package com.ml2wf.v3.app.business.storage.graph.neo4j.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JStandardKnowledgeTasksRepository extends Neo4jRepository<Neo4JStandardKnowledgeTask, Long>,
        StandardKnowledgeTasksRepository<Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Long> {
    // TODO: find a way to remove Neo4JTaskVersion from generics

}
