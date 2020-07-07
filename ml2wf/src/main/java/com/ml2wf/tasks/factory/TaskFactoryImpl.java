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
import com.ml2wf.tasks.BPMNTask;
import com.ml2wf.tasks.FMTask;
import com.ml2wf.tasks.Task;
import com.ml2wf.tasks.manager.TasksManager;
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

	@Override
	public Set<Task> createTasks(Node node) {
		Set<Task> createdTasks = new HashSet<>();
		String tagName;
		String nodeName;
		Task createdTask;
		for (Node child : AbstractMerger.getNestedNodes(node)) {
			tagName = child.getNodeName();
			nodeName = XMLManager.getNodeName(child);
			nodeName = XMLManager.sanitizeName(nodeName);
			if (FMNames.SELECTOR.isFMTask(tagName)) {
				createdTask = new FMTask(nodeName, child, this.isAbstract(child));
			} else if (BPMNNames.SELECTOR.isBPMNTask(tagName)) {
				Optional<String> optRef = XMLManager.getReferredTask(XMLManager.getAllBPMNDocContent((Element) child));
				createdTask = new BPMNTask(nodeName, optRef.orElse(""));
			} else {
				continue; // TODO: throw error
			}
			createdTasks.add(TasksManager.addTask(createdTask));
		}
		return createdTasks;
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
