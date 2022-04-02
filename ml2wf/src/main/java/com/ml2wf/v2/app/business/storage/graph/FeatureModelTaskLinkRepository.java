package com.ml2wf.v2.app.business.storage.graph;

import com.arangodb.springframework.repository.ArangoRepository;
import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTaskLink;

public interface FeatureModelTaskLinkRepository extends ArangoRepository<ArangoFeatureModelTaskLink, String> {

}
