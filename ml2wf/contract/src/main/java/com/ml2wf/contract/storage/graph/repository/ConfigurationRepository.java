package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConfigurationRepository<C extends GraphConfiguration<F, T, V>,
        F extends GraphConfigurationFeature<T, V>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, ID> extends ReactiveSortingRepository<C, ID>, ReactiveQueryByExampleExecutor<C> {

    Mono<C> findOneByName(String configurationName);
}
