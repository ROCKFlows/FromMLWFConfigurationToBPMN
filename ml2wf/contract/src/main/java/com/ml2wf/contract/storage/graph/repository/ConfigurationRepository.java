package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConfiguration;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository<C extends GraphConfiguration<V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<C, ID>, QueryByExampleExecutor<C> {

}