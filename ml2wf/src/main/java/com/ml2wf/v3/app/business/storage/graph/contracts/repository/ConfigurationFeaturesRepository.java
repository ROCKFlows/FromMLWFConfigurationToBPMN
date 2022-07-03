package com.ml2wf.v3.app.business.storage.graph.contracts.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConfigurationFeature;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationFeaturesRepository<F extends GraphConfigurationFeature<T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<F, ID>, QueryByExampleExecutor<F> {

}
