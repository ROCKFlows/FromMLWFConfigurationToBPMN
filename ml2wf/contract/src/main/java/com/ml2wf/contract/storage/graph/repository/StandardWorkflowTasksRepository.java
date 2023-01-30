package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphStandardWorkflowTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface StandardWorkflowTasksRepository<T extends GraphStandardWorkflowTask<T, V>,
        V extends GraphTaskVersion, ID> extends ReactiveSortingRepository<T, ID>,
        ReactiveQueryByExampleExecutor<T> {

    Mono<T> findOneByNameAndVersionName(String taskName, String versionName);
}
