package com.ml2wf.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Node;

import com.ml2wf.tasks.manager.TasksManager;

public class FMTask extends Task {

	private Node node;
	private boolean isAbstract;

	public FMTask(String name, Task parent, Node node, boolean isAbstract) {
		super(name, parent);
		this.node = node;
		this.isAbstract = isAbstract;
	}

	public FMTask(String name, Node node, boolean isAbstract) {
		this(name, null, node, isAbstract);
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public FMTask appendChild(FMTask child) {
		child.setParent(this);
		this.node.appendChild(child.getNode());
		return child;
	}

	public Optional<FMTask> removeChild(FMTask oldChild) {
		Node oldNode = oldChild.getNode();
		Optional<FMTask> optTask = TasksManager.getFMTaskWithNode(oldNode);
		if (optTask.isPresent()) {
			this.node.removeChild(oldNode);
			return optTask;
		}
		return Optional.empty();
	}

	private static Optional<FMTask> getChildWithName(Task parent, String childName) {
		List<FMTask> childrenTasks = TasksManager.getFMTaskWithParent(parent);
		Optional<FMTask> optChild;
		for (FMTask child : childrenTasks) {
			if (child.getName().equals(childName)) {
				return Optional.of(child);
			} else {
				optChild = getChildWithName(child, childName);
				if (optChild.isPresent()) {
					return optChild;
				}
			}
		}
		return Optional.empty();
	}

	public Optional<FMTask> getChildWithName(String childName) {
		return getChildWithName(this, childName);
	}

	public static FMTask getLCA(List<FMTask> tasks) {
		FMTask parent;
		List<FMTask> commonParents = new ArrayList<>();
		List<FMTask> nodeParents = new ArrayList<>();
		for (FMTask task : tasks) {
			// for each node
			parent = task;
			while ((parent = (FMTask) parent.getParent()) != null) {
				// get all parents
				nodeParents.add(parent);
			}
			if (commonParents.isEmpty()) {
				commonParents.addAll(nodeParents);
			}
			// retaining common parents
			commonParents.retainAll(nodeParents);
		}
		return commonParents.get(0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + (this.isAbstract ? 1231 : 1237);
		result = (prime * result) + ((this.node == null) ? 0 : this.node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FMTask)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		FMTask other = (FMTask) obj;
		return this.node.equals(other.getNode()) && (this.isAbstract == other.isAbstract());
	}
}
