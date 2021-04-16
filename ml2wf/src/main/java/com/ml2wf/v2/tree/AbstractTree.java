package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.AbstractTask;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTree<T extends AbstractTask<T>> {

    protected final List<T> tasks;

    /**
     * {@code AbstractTree}'s default constructor.
     */
    protected AbstractTree() {
        tasks = new ArrayList<>();
    }

    /**
     * Normalizes the current tree by calling {@link T#normalize()} for each task
     * in {@link #tasks};
     */
    public void normalize() {
        tasks.forEach(T::normalize);
    }
}
