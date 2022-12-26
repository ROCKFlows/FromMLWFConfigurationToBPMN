package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintsRepository<C extends GraphConstraintOperand<V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<C, ID>, QueryByExampleExecutor<C> {

    List<C> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
