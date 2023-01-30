package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VersionsRepository<V extends GraphTaskVersion, ID>
        extends ReactiveSortingRepository<V, ID>, ReactiveQueryByExampleExecutor<V> {

    Mono<V> getLastVersion();

    Mono<V> findOneByName(String versionName);
}
