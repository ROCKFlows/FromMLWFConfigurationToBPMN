package com.ml2wf.v2.task;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.v2.util.ElementBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Optional;

public class WorkflowTask extends AbstractTask<WorkflowTask> {

    private int propertyCounter;

    /**
     * {@code WorkflowTask} constructor with a {@link Node}, a parent {@link AbstractTask} and
     * an abstract status.
     *
     * @param node          the node
     * @param parent        the parent task
     * @param isAbstract    whether the task is abstract or not
     */
    protected WorkflowTask(Node node, WorkflowTask parent, boolean isAbstract) {
        super(node, parent, isAbstract);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node} and a parent {@link AbstractTask}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract).
     *
     * @param node      the node
     * @param parent    the parent task
     */
    protected WorkflowTask(Node node, WorkflowTask parent) {
        super(node, parent);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node} and an abstract status.
     *
     * <p>
     *
     * <b>Note</b> that the task is set with a {@code null} {@link #getParent()}.
     *
     * @param node          the node
     * @param isAbstract    whether the task is abstract or not
     */
    protected WorkflowTask(Node node, boolean isAbstract) {
        super(node, isAbstract);
    }

    /**
     * {@code WorkflowTask} constructor with a {@link Node}.
     *
     * <p>
     *
     * <b>Note</b> that the task is set as concrete (non-abstract) with a
     * {@code null} {@link #getParent()}.
     *
     * @param node      the node
     */
    protected WorkflowTask(Node node) {
        super(node);
    }

    @Override
    public WorkflowTask appendChild(WorkflowTask childTask) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Optional<WorkflowTask> removeChild(WorkflowTask childTask) {
        throw new UnsupportedOperationException("TODO");
    }

    public void instantiate(int taskNumber) {
        // getting the node's reference
        // TODO: String currentRef = getLastReference(name);
        // adding a documentation node for the current node
        // TODO: Node docNode = addDocumentationNode(node);
        // TODO: mergeNodesTextContent(docNode, BPMNTaskSpecs.OPTIONAL.formatSpec(content));
        // TODO: mergeNodesTextContent(docNode, getReferenceDocumentation(currentRef));
        // property/extension part
        addProperty(Notation.BPMN_PROPERTY_INSTANCE);
        // TODO: addExtensionNode(node);
        // node renaming part
        // TODO: String nodeName = XMLManager.sanitizeName(currentRef) + "_" + taskNumber;
        // setName(nodeName);
        setAbstract(false);
    }

    public void addProperty(String propertyValue) {
        Element propertyAttribute = ElementBuilder.builder(getDocument(), BPMNNames.PROPERTY.getName())
                .setAttribute(BPMNAttributes.ID.getName(), Notation.BPMN_PROPERTY_PREFIX + propertyCounter++)
                .setIdAttribute(BPMNAttributes.ID.getName(), true)
                .setAttribute(BPMNAttributes.NAME.getName(), propertyValue)
                .build();
        node.appendChild(propertyAttribute);
    }
}
