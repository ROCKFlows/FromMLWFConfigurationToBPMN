package com.ml2wf.v3.app.business.storage.graph.contracts.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionsRepository<V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<V, ID>, QueryByExampleExecutor<V> {

    Optional<V> getLastVersion();
}
