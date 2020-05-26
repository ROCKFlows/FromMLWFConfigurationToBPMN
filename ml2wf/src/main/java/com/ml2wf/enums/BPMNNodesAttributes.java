package com.ml2wf.enums;

public enum BPMNNodesAttributes {
	BACKGROUND("ext:shapeBackground"), ID("id"), NAME("name");

	private String name;

	private BPMNNodesAttributes(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
