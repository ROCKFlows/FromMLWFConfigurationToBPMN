package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.repository.StandardKnowledgeTasksRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JStandardKnowledgeTasksRepository extends Neo4jRepository<GraphStandardKnowledgeTask<Neo4JTaskVersion>, Long>,
        StandardKnowledgeTasksRepository<Neo4JTaskVersion, Long> {

}
