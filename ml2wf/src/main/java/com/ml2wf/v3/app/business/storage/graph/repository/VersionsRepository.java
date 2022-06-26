package com.ml2wf.v3.app.business.storage.graph.repository;

import com.arangodb.springframework.annotation.Query;
import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v3.app.business.storage.graph.dto.ArangoTaskVersion;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionsRepository extends ArangoRepository<ArangoTaskVersion, String> {
    @Query("FOR v IN Versions SORT v.major DESC, v.minor DESC, v.patch DESC LIMIT 1 RETURN v")
    Optional<ArangoTaskVersion> getLastVersion();
}
