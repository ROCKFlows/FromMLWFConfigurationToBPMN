package com.ml2wf.tasks;

public class BPMNTask extends Task {

	private String reference;

	public BPMNTask(String name, Task parent, String reference) {
		super(name, parent);
		this.reference = reference;
	}

	public BPMNTask(String name, String reference) {
		super(name);
		this.reference = reference;
	}

	public String getReference() {
		return this.reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
}
