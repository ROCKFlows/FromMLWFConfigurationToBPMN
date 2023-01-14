package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintsRepository<O extends GraphConstraintOperand<O, T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<O, ID>, QueryByExampleExecutor<O> {

    List<O> findAllByTypeAndVersionName(String constraintType, String versionName);
}
