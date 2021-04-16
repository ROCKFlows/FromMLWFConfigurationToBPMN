package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.WorkflowTask;

public class WorkflowTree extends AbstractTree<WorkflowTask> {

    private int nbTasks;

    public WorkflowTree() {
        super();
    }

    public void instantiate() {
        for (WorkflowTask task : tasks) {
            task.instantiate(nbTasks++);
        }
    }
}
