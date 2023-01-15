package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphStandardWorkflowTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardWorkflowTasksRepository<T extends GraphStandardWorkflowTask<T, V>,
        V extends GraphTaskVersion, ID> extends PagingAndSortingRepository<T, ID>,
        QueryByExampleExecutor<T> {

    Optional<T> findOneByNameAndVersionName(String taskName, String versionName);
}
