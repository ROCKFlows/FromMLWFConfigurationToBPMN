package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.FeatureModelTask;
import com.ml2wf.v2.task.FeatureModelTaskFactory;
import com.ml2wf.v2.task.constraints.Constraint;

import com.ml2wf.v2.util.NodeDescriber;
import com.ml2wf.v2.util.NodeListIterator;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FeatureModel extends AbstractTree<FeatureModelTask> {

    // TODO: store the root UNMANAGED node
    private final List<Constraint> constraints;

    public FeatureModel(Document document, List<Constraint> constraints) {
        super(document, new FeatureModelTaskFactory());
        this.constraints = new ArrayList<>(constraints);
    }

    public static FeatureModel fromDocument(Document document) {
        return new FeatureModel(document, new ArrayList<>());
    }

    @Override
    public List<FeatureModelTask> initializeTasks() {
        // TODO: add isInit attribute if we keep this init method
        // TODO: init constraints too once validated/supported
        Node currentNode = null; // TODO: check default null value
        NodeListIterator andNodeListIterator = new NodeListIterator(getDocument(), "and");
        while (andNodeListIterator.hasNext()) {
            try {
                currentNode = andNodeListIterator.next();
                tasks.add(taskFactory.createTask(currentNode));
            } catch (Exception exception) {
                // TODO: create custom exceptions
                log.error("Can't create FeatureModelTask from node [{}]",
                        NodeDescriber.getNodeDescription(currentNode));
            }
        }
        NodeListIterator featureNodeListIterator = new NodeListIterator(getDocument(), "feature");
        while (featureNodeListIterator.hasNext()) {
            try {
                currentNode = featureNodeListIterator.next();
                tasks.add(taskFactory.createTask(currentNode));
            } catch (Exception exception) {
                // TODO: create custom exceptions
                log.error("Can't create task from node [{}]", NodeDescriber.getNodeDescription(currentNode));
            }
        }
        return new ArrayList<>(tasks);
    }
}
