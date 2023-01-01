package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VersionsRepository<V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<V, ID>, QueryByExampleExecutor<V> {

    Optional<V> getLastVersion();

    Optional<V> findOneByName(String versionName);
}
