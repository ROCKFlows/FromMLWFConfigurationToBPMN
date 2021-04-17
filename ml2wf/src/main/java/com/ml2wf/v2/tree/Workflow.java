package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.WorkflowTask;

import com.ml2wf.v2.task.WorkflowTaskFactory;
import com.ml2wf.v2.util.NodeListIterator;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class Workflow extends AbstractTree<WorkflowTask> {

    private int nbTasks;

    public Workflow(Document document) {
        super(document, new WorkflowTaskFactory());
    }

    public static Workflow fromDocument(Document document) {
        return new Workflow(document);
    }

    @Override
    public List<WorkflowTask> initializeTasks() {
        // TODO: add isInit attribute if we keep this init method
        new NodeListIterator(getDocument(), "bpmn2:task")
                // TODO: add logs if fails
                .forEachRemaining(n -> tasks.add(taskFactory.createTask(n)));
        return new ArrayList<>(tasks);
    }

    public void instantiate() {
        for (WorkflowTask task : tasks) {
            task.instantiate(nbTasks++);
        }
    }
}
