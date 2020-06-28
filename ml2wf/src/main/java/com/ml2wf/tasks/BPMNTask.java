package com.ml2wf.tasks;

import org.w3c.dom.Node;

public class BPMNTask extends Task {

	public BPMNTask(String name, Node parent) {
		super(name, parent);
	}

	public BPMNTask(String name) {
		super(name);
	}
}
