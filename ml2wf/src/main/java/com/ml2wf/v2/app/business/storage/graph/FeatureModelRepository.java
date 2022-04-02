package com.ml2wf.v2.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTask;

public interface FeatureModelRepository extends ArangoRepository<ArangoFeatureModelTask, String> {
    ArangoFeatureModelTask findOneByName(String name);
}
