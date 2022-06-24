package com.ml2wf.v2.app.business.storage.graph;

import com.ml2wf.v2.app.business.storage.graph.dto.ArangoFeatureModelTask;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;

import java.util.List;

public interface IArangoFeatureModelConverter {

    FeatureModel toRawFeatureModel(ArangoFeatureModelTask arangoFeatureModelTask);

    FeatureModelTask toRawFeatureModelTask(ArangoFeatureModelTask arangoFeatureModelTask);

    List<ArangoFeatureModelTask> fromRawFeatureModel(FeatureModel featureModel);

    List<ArangoFeatureModelTask> fromRawFeatureModelTask(FeatureModelTask featureModelTask);
}
