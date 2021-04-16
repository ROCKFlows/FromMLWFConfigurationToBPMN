package com.ml2wf.v2.task;

import org.w3c.dom.Node;

@FunctionalInterface
public interface ITaskFactory<T extends AbstractTask<T>> {

    T createTask(T parent, Node node);

    default T createTask(Node node) {
        return createTask(null, node);
    }
}
