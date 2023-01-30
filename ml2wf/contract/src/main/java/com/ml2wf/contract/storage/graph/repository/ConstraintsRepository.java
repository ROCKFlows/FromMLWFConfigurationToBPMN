package com.ml2wf.contract.storage.graph.repository;

import com.ml2wf.contract.storage.graph.dto.GraphConstraintOperand;
import com.ml2wf.contract.storage.graph.dto.GraphStandardKnowledgeTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ConstraintsRepository<O extends GraphConstraintOperand<O, T, V>,
        T extends GraphStandardKnowledgeTask<T, V>, V extends GraphTaskVersion, ID>
        extends ReactiveSortingRepository<O, ID>, ReactiveQueryByExampleExecutor<O> {

    Flux<O> findAllByTypeAndVersionName(String constraintType, String versionName);
}
