package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.ConstraintsRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JConstraintOperand;
import com.ml2wf.neo4j.storage.dto.Neo4JStandardKnowledgeTask;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Neo4JConstraintsRepository extends Neo4jRepository<Neo4JConstraintOperand, Long>,
        ConstraintsRepository<Neo4JConstraintOperand, Neo4JStandardKnowledgeTask, Neo4JTaskVersion, Long> {

    // TODO: fix hardcoded __ROOT_CONSTRAINT task name
    @Override
    @Query("""
            MATCH (r:Constraint {type: $constraintType})<-[:IS_PARENT_OF*0..]-(:Constraint {type: '__ROOT_CONSTRAINT'})-[:IS_VERSIONED_BY*]->({name: $versionName}), f = (:Task)<-[:CONSTRAINS]-(c:Constraint)<-[:IS_PARENT_OF*]-(r)
            RETURN r, collect(nodes(f)), collect(relationships(f))
            """)
    List<Neo4JConstraintOperand> findAllByTypeAndVersionName(String constraintType, String versionName);
}
