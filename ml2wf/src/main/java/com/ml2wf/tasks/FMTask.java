package com.ml2wf.tasks;

import org.w3c.dom.Node;

public class FMTask extends Task {

	private boolean isAbstract;

	public FMTask(String name, Node parent, boolean isAbstract) {
		super(name, parent);
		this.isAbstract = isAbstract;
	}

	public FMTask(String name, boolean isAbstract) {
		this(name, null, isAbstract);
	}

	public boolean isAbstract() {
		return this.isAbstract;
	}
}
