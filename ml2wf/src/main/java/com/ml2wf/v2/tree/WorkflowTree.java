package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.WorkflowTask;

import com.ml2wf.v2.task.WorkflowTaskFactory;
import com.ml2wf.v2.util.NodeIterator;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class WorkflowTree extends AbstractTree<WorkflowTask> {

    private int nbTasks;

    public WorkflowTree(Document document) {
        super(document, new WorkflowTaskFactory());
    }

    public static WorkflowTree fromDocument(Document document) {
        return new WorkflowTree(document);
    }

    @Override
    public List<WorkflowTask> initializeTasks() {
        // TODO: add isInit attribute if we keep this init method
        NodeIterator nodeIterator = new NodeIterator(getDocument(), "bpmn2:task");
        while (nodeIterator.hasNext()) {
            tasks.add(taskFactory.createTask(nodeIterator.next()));
        }
        return new ArrayList<>(tasks);
    }

    public void instantiate() {
        for (WorkflowTask task : tasks) {
            task.instantiate(nbTasks++);
        }
    }
}
