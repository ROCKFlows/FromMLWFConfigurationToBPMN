package com.ml2wf.v2.task;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.v2.util.ElementBuilder;
import lombok.EqualsAndHashCode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
public class WorkflowTask extends AbstractTask<WorkflowTask> {

    private final List<String> references;
    private int propertyCounter;

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
    WorkflowTask(Node node, WorkflowTask parent) {
        super(node, parent);
        references = new ArrayList<>();
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
    WorkflowTask(Node node) {
        super(node);
        references = new ArrayList<>();
    }

    public List<String> getReferences() {
        if (isAbstract()) {
            throw new IllegalStateException("What should we do ?");
        }
        return new ArrayList<>(references);
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
        // TODO: String currentRef = getLastReference(getName());
        // adding a documentation node for the current node
        // TODO: Node docNode = addDocumentationNode(node);
        // TODO: mergeNodesTextContent(docNode, BPMNTaskSpecs.OPTIONAL.formatSpec(content));
        // TODO: mergeNodesTextContent(docNode, getReferenceDocumentation(currentRef));
        // property/extension part
        addProperty(Notation.BPMN_PROPERTY_INSTANCE);
        // TODO: addExtensionNode(node);
        // node renaming part
        // TODO: String nodeName = sanitizeName(currentRef) + "_" + taskNumber;
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
