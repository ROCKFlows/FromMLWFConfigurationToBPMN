package com.ml2wf.tasks.factory;

import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.BPMNTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.util.XMLManager;

/**
 * This class is a factory for the {@code Task} class.
 *
 * <p>
 *
 * It is an implementation of the {@link TaskFactory} interface.
 *
 * @author Nicolas Lacroix
 *
 * @see TaskFactory
 * @see Task
 *
 * @since 1.0.0
 */
@NoArgsConstructor
@Log4j2
public class TaskFactoryImpl implements TaskFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Task<?>> T createTask(Node node) throws UnresolvedConflict {
        String tagName = node.getNodeName();
        String nodeName = XMLManager.getNodeName(node);
        nodeName = XMLManager.sanitizeName(nodeName);
        return (FMNames.SELECTOR.isFMTask(tagName)) ? (T) createFMTask(nodeName, node) :
                (T) createWFTask(nodeName, node);
    }

    @Override
    public FMTask convertWFtoFMTask(WFTask<?> task) throws UnresolvedConflict {
        FMTask createdFMTask = new FMTask(task, false);
        createdFMTask.setNode(AbstractMerger.createFeatureWithAbstract(createdFMTask.getName(), task.isAbstract()));
        createdFMTask.addAllSpecs(task.getSpecs());
        createdFMTask.applySpecs();
        return createdFMTask;
    }

    /**
     * Creates and returns a new {@code FMTask} considering the given arguments.
     *
     * @param name name of the new task
     * @param node node of the new task
     *
     * @return a new {@code FMTask} considering the given arguments
     *
     * @throws UnresolvedConflict
     *
     * @see FMTask
     */
    private static FMTask createFMTask(String name, Node node) throws UnresolvedConflict {
        return new FMTask(name, node, isFMAbstract(node));
    }

    /**
     * Creates and returns a new {@code WFTask} considering the given arguments.
     *
     * @param name name of the new task
     * @param node node of the new task
     *
     * @return a new {@code WFTask} considering the given arguments
     *
     * @throws UnresolvedConflict
     *
     * @see WFTask
     */
    private static WFTask<?> createWFTask(String name, Node node) throws UnresolvedConflict {
        // TODO: change created type considering user convention (e.g. BPMN)
        Optional<String> optRefName = XMLManager.getReferredTask(XMLManager.getAllBPMNDocContent((Element) node));
        boolean isAbstract = AbstractMerger.getProperty((Element) node, BPMNNames.PROPERTY.getName()).isEmpty();
        BPMNTask createdFMTask = new BPMNTask(name, node, isAbstract,
                optRefName.orElse((isAbstract) ? WFMetaMerger.STEP_TASK : BaseMergerImpl.UNMANAGED_TASKS));
        createdFMTask.applySpecs();
        return createdFMTask;
    }

    /**
     * Returns whether the given {@code node} is abstract or not.
     *
     * <p>
     *
     * This helps setting the {@link FMTask#isAbstract()} attribute.
     *
     * @param node node containing the abstract information
     *
     * @return whether the given {@code node} is abstract or not
     *
     * @see FMTask
     */
    private static boolean isFMAbstract(Node node) {
        Node abstractAttr = node.getAttributes().getNamedItem(FMAttributes.ABSTRACT.getName());
        if (abstractAttr == null) {
            log.debug("Can't get the abstract status for the node : {}. Considering it as a concrete one.",
                    XMLManager.getNodeName(node));
            return false;
        }
        return Boolean.parseBoolean(abstractAttr.getNodeValue());
    }
}
