package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.AbstractTask;
import com.ml2wf.v2.task.ITaskFactory;
import lombok.Getter;

import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTree<T extends AbstractTask<T>> {

    // TODO: initialize tasks

    protected ITaskFactory<T> taskFactory;
    @Getter private final Document document;
    protected final List<T> tasks;

    /**
     * {@code AbstractTree}'s default constructor.
     */
    protected AbstractTree(Document document, ITaskFactory<T> taskFactory) {
        this.document = document;
        this.tasks = new ArrayList<>();
        this.taskFactory = taskFactory;
    }

    /**
     * Initializes the current tree's tasks.
     *
     * @return the initialized tasks
     */
    protected abstract List<T> initializeTasks();

    /**
     * Normalizes the current tree by calling {@link T#normalize()} for each task
     * in {@link #tasks};
     */
    public void normalize() {
        tasks.forEach(T::normalize);
    }
}
