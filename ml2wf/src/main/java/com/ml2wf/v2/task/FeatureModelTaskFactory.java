package com.ml2wf.v2.task;

import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

@NoArgsConstructor
public final class FeatureModelTaskFactory implements ITaskFactory<FeatureModelTask> {

    @Override
    public FeatureModelTask createTask(Node node, FeatureModelTask parent) {
        return new FeatureModelTask(node, parent);
    }
}
