package com.ml2wf.neo4j.storage.repository;

import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import com.ml2wf.neo4j.storage.dto.Neo4JTaskVersion;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface Neo4JVersionsRepository extends ReactiveNeo4jRepository<Neo4JTaskVersion, String>,
        VersionsRepository<Neo4JTaskVersion, String> {

    @Override
    @Query("MATCH (v:Version) WITH v ORDER BY v.major DESC, v.minor DESC, v.patch DESC LIMIT 1 RETURN v")
    Mono<Neo4JTaskVersion> getLastVersion();
}
