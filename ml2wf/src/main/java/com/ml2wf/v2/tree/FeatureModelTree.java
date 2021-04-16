package com.ml2wf.v2.tree;

import com.ml2wf.v2.task.FeatureModelTask;
import com.ml2wf.v2.task.FeatureModelTaskFactory;
import com.ml2wf.v2.task.constraints.Constraint;

import com.ml2wf.v2.util.NodeDescriber;
import com.ml2wf.v2.util.NodeIterator;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class FeatureModelTree extends AbstractTree<FeatureModelTask> {

    // TODO: store the root UNMANAGED node
    private final List<Constraint> constraints;

    public FeatureModelTree(Document document, List<Constraint> constraints) {
        super(document, new FeatureModelTaskFactory());
        this.constraints = new ArrayList<>(constraints);
    }

    public static FeatureModelTree fromDocument(Document document) {
        return new FeatureModelTree(document, new ArrayList<>());
    }

    @Override
    public List<FeatureModelTask> initializeTasks() {
        // TODO: add isInit attribute if we keep this init method
        // TODO: init constraints too once validated/supported
        // TODO: to fix
        Node currentNode = null; // TODO: check default null value
        NodeIterator andNodeIterator = new NodeIterator(getDocument(), "and");
        while (andNodeIterator.hasNext()) {
            try {
                currentNode = andNodeIterator.next();
                tasks.add(taskFactory.createTask(currentNode));
            } catch (Exception exception) {
                log.error("Can't create task from node [{}]", NodeDescriber.getNodeDescription(currentNode));
            }
        }
        NodeIterator featureNodeIterator = new NodeIterator(getDocument(), "feature");
        while (featureNodeIterator.hasNext()) {
            try {
                currentNode = featureNodeIterator.next();
                tasks.add(taskFactory.createTask(currentNode));
            } catch (Exception exception) {
                log.error("Can't create task from node [{}]", NodeDescriber.getNodeDescription(currentNode));
            }
        }
        // return new ArrayList<>(tasks);
        throw new UnsupportedOperationException("Not supported yet");
    }
}
