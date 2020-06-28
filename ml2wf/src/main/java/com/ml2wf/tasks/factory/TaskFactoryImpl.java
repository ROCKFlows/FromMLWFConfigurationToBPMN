package com.ml2wf.tasks.factory;

import java.util.HashSet;
import java.util.Set;

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

	public TaskFactoryImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Task> createTasks(Node node) {
		Set<Task> createdTasks = new HashSet<>();
		String tagName;
		String nodeName;
		for (Node child : AbstractMerger.getNestedNodes(node)) {
			tagName = child.getNodeName();
			nodeName = XMLManager.getNodeName(child);
			nodeName = XMLManager.sanitizeName(nodeName);
			if (FMNames.SELECTOR.isFMTask(tagName)) { // TODO: to check
				createdTasks.add(TasksManager.addTask(new FMTask(nodeName, this.isAbstract(child))));
			} else if (BPMNNames.SELECTOR.isBPMNTask(tagName)) {
				createdTasks.add(TasksManager.addTask(new BPMNTask(nodeName)));
			}
		}
		return createdTasks;
	}

	private boolean isAbstract(Node node) {
		Node abstractAttr = node.getAttributes().getNamedItem(FMAttributes.ABSTRACT.getName());
		return (abstractAttr != null) && (abstractAttr.getNodeValue().equals(String.valueOf(true)));
	}
}
