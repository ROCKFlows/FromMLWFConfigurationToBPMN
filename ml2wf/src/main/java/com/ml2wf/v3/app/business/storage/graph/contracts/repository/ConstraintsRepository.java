package com.ml2wf.v3.app.business.storage.graph.contracts.repository;

import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphConstraintOperand;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphStandardKnowledgeTask;
import com.ml2wf.v3.app.business.storage.graph.contracts.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintsRepository<C extends GraphConstraintOperand<T, V, C>, T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<C, ID>, QueryByExampleExecutor<C> {

    List<C> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
