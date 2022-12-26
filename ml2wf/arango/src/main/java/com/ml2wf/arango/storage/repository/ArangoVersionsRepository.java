package com.ml2wf.arango.storage.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.repository.VersionsRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArangoVersionsRepository extends ArangoRepository<ArangoTaskVersion, String>,
        VersionsRepository<ArangoTaskVersion, String> {

    @Query("FOR v IN Versions SORT v.major DESC, v.minor DESC, v.patch DESC LIMIT 1 RETURN v")
    Optional<ArangoTaskVersion> getLastVersion();
}