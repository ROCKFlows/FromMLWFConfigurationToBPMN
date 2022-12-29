package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstraintsRepository<V extends GraphTaskVersion, ID>
        extends PagingAndSortingRepository<GraphConstraintOperand<V>, ID>, QueryByExampleExecutor<GraphConstraintOperand<V>> {

    List<GraphConstraintOperand<V>> findAllByTypeEqualsAndVersion_Name(String name, String versionName);
}
