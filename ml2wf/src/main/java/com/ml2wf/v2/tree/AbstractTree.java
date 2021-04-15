package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.AbstractTask;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTree {

    private final List<AbstractTask> tasks;

    /**
     * {@code AbstractTree}'s default constructor.
     */
    protected AbstractTree() {
        tasks = new ArrayList<>();
    }
}
