package com.ml2wf.v3.app.business.storage.graph.neo4j.repository;

import com.ml2wf.v3.app.business.storage.graph.neo4j.dto.Neo4JStandardKnowledgeTask;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Neo4JStandardKnowledgeTasksRepository extends Neo4jRepository<Neo4JStandardKnowledgeTask, Long> {

    Optional<Neo4JStandardKnowledgeTask> findOneByNameAndVersion_Name(String name, String versionName); // TODO
}
