package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConfigurationFeature;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationFeaturesRepository<F extends GraphConfigurationFeature<T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<F, ID>, QueryByExampleExecutor<F> {

}
