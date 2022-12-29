package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StandardKnowledgeTasksRepository<V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<GraphStandardKnowledgeTask<V>, ID>,
        QueryByExampleExecutor<GraphStandardKnowledgeTask<V>> {

    Optional<GraphStandardKnowledgeTask<V>> findOneByNameAndVersion_Name(String name, String versionName);
}
