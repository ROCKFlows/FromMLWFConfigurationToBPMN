package com.ml2wf.tasks.factory;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.ml2wf.conventions.Notation;
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
	public <T extends Task<?>> T createTasks(Node node) {
		String tagName = node.getNodeName();
		String nodeName = XMLManager.getNodeName(node);
		nodeName = XMLManager.sanitizeName(nodeName);
		return (FMNames.SELECTOR.isFMTask(tagName)) ? (T) this.createFMTask(nodeName, node)
				: (T) this.createWFTask(nodeName, node);
	}

	@Override
	public FMTask convertWFtoFMTask(WFTask<?> task) {
		FMTask createdFMTask = new FMTask(task, false);
		createdFMTask.setNode(AbstractMerger.createFeatureNode(createdFMTask.getName(), task.isAbstract()));
		createdFMTask.addAllSpecs(task.getSpecs());
		createdFMTask.applySpecs();
		return createdFMTask;
	}

	/**
	 * Creates and returns a new {@code FMTask} considering the given arguments.
	 *
	 * @param name name of the new task
	 * @param node node of the new task
	 * @return a new {@code FMTask} considering the given arguments
	 *
	 * @since 1.0
	 * @see FMTask
	 */
	private FMTask createFMTask(String name, Node node) {
		// TODO: improve abstract definition
		return new FMTask(name, node, this.isAbstract(node));
	}

	/**
	 * Creates and returns a new {@code WFTask} considering the given arguments.
	 *
	 * @param name name of the new task
	 * @param node node of the new task
	 * @return a new {@code WFTask} considering the given arguments
	 * @throws TaskFactoryException
	 *
	 * @since 1.0
	 * @see WFTask
	 */
	private WFTask<?> createWFTask(String name, Node node) {
		// TODO: change created type considering user convention (e.g. BPMN)
		Optional<String> optRefName = XMLManager.getReferredTask(XMLManager.getAllBPMNDocContent((Element) node));
		boolean isAbstract = optRefName.isEmpty() || optRefName.get().endsWith(Notation.getReferenceSpecialEndchar());
		String reference = optRefName.orElse("");
		if (reference.endsWith(Notation.getReferenceSpecialEndchar())) {
			reference = reference.substring(0, reference.length() - 1);
		}
		BPMNTask createdFMTask = new BPMNTask(name, node, isAbstract, reference);
		createdFMTask.applySpecs();
		return createdFMTask;
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
		// TODO: bad method to be removed or corrected
		Node abstractAttr = node.getAttributes().getNamedItem(FMAttributes.ABSTRACT.getName());
		return (abstractAttr != null) && (abstractAttr.getNodeValue().equals(String.valueOf(true)));
	}

}
