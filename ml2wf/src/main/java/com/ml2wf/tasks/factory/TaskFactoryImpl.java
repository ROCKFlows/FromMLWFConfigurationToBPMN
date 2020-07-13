package com.ml2wf.tasks.factory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.AbstractMerger;
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
 * @version 1.0
 *
 * @see TaskFactory
 * @see Task
 *
 */
public class TaskFactoryImpl implements TaskFactory {

	/**
	 * {@code TaskFactoryImpl}'s empty constructor.
	 */
	public TaskFactoryImpl() {
		// empty constructor
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Task<?>> Set<T> createTasks(Node node) {
		Set<T> createdTasks = new HashSet<>();
		String tagName;
		String nodeName;
		T createdTask;
		for (Node child : AbstractMerger.getNestedNodes(node)) {
			tagName = child.getNodeName();
			nodeName = XMLManager.getNodeName(child);
			nodeName = XMLManager.sanitizeName(nodeName);
			// TODO: change this if-elseif statement
			if (FMNames.SELECTOR.isFMTask(tagName)) {
				createdTask = (T) this.createFMTask(nodeName, child);
			} else if (BPMNNames.SELECTOR.isBPMNTask(tagName)) {
				createdTask = (T) this.createWFTask(nodeName, child);
			} else {
				continue; // TODO: throw error
			}
			createdTasks.add(createdTask);
		}
		return createdTasks;
	}

	@Override
	public FMTask convertWFtoFMTask(WFTask<?> task) {
		FMTask createdFMTask = new FMTask(task, false);
		createdFMTask.setNode(AbstractMerger.createFeatureNode(createdFMTask.getName(), createdFMTask.isAbstract()));
		return createdFMTask;
	}

	/**
	 * Creates and returns a new {@code FMTask} considering given arguments.
	 *
	 * @param name name of the new task
	 * @param node node of the new task
	 * @return a new {@code FMTask} considering given arguments
	 *
	 * @since 1.0
	 * @see FMTask
	 */
	private FMTask createFMTask(String name, Node node) {
		return new FMTask(name, node, this.isAbstract(node));
	}

	/**
	 * Creates and returns a new {@code WFTask} considering given arguments.
	 *
	 * @param name name of the new task
	 * @param node node of the new task
	 * @return a new {@code WFTask} considering given arguments
	 *
	 * @since 1.0
	 * @see WFTask
	 */
	private WFTask<?> createWFTask(String name, Node node) {
		// TODO: change created type considering user convention (e.g. BPMN)
		Optional<String> optRef = XMLManager.getReferredTask(XMLManager.getAllBPMNDocContent((Element) node));
		return new BPMNTask(name, node, XMLManager.isMetaTask((Element) node),
				optRef.orElse(""));
	}

	/**
	 * Returns whether the given {@code node} is abstract or not.
	 *
	 * <p>
	 *
	 * This helps setting the {@link FMTask#isAbstract} attribute.
	 *
	 * @param node node containing the abstract information
	 * @return whether the given {@code node} is abstract or not
	 *
	 * @since 1.0
	 * @see FMTask
	 */
	private boolean isAbstract(Node node) {
		Node abstractAttr = node.getAttributes().getNamedItem(FMAttributes.ABSTRACT.getName());
		return (abstractAttr != null) && (abstractAttr.getNodeValue().equals(String.valueOf(true)));
	}

}
