package com.ml2wf.v3.app.business.storage.graph.contracts.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConfiguration;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository<C extends GraphConfiguration<T, V, F>, T extends GraphStandardKnowledgeTask<T, V>,
        V extends GraphTaskVersion, F extends GraphConfigurationFeature<T, V>, ID>
        extends PagingAndSortingRepository<C, ID>, QueryByExampleExecutor<C> {

}
