package com.ml2wf.v2.task;

import org.w3c.dom.Node;

public class WorkflowTaskFactory implements ITaskFactory<WorkflowTask> {

    @Override
    public WorkflowTask createTask(Node node, WorkflowTask parent) {
        return new WorkflowTask(node, parent);
    }
}
