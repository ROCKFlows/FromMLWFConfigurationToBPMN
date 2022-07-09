package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4JConstraintsRepository extends Neo4jRepository<Neo4JConstraintOperand, Long>,
        ConstraintsRepository<Neo4JConstraintOperand, Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Long> {
    // TODO: find a way to remove Neo4JStandardKnowledgeTask, Neo4JTaskVersion from generics

}
