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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((this.reference == null) ? 0 : this.reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BPMNTask)) {
			return false;
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return super.toString() + "[\n\tBPMNTask [reference=" + this.reference + "]]";
	}
}
