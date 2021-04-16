package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.FeatureModelTask;
import com.ml2wf.v2.task.constraints.Constraint;

import java.util.List;

public class FeatureModelTree extends AbstractTree<FeatureModelTask> {

    private final List<Constraint> constraints;


    public FeatureModelTree(List<Constraint> constraints) {
        super();
        this.constraints = constraints;
    }
}
