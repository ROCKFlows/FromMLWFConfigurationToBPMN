package com.ml2wf.conventions.enums.bpmn;

public enum BPMNNodesNames {

	TASK("bpmn2:task"), USERTASK("bpmn2:userTask"), INCOMING("bpmn2:incoming"), OUTGOING("bpmn2:outgoing"),
	EXTENSION("bpmn2:extensionElements"), STYLE("ext:style"), DOCUMENTATION("bpmn2:documentation");

	private String name;

	private BPMNNodesNames(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
