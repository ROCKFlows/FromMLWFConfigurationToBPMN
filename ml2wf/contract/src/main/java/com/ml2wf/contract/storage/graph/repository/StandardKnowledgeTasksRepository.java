package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardKnowledgeTasksRepository<T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

    Optional<T> findOneByNameAndVersion_Name(String name, String versionName);
}
