package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository<C extends GraphConfiguration<T, V, F>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, F extends GraphConfigurationFeature<T, V>, ID>
        extends PagingAndSortingRepository<C, ID>, QueryByExampleExecutor<C> {

}
