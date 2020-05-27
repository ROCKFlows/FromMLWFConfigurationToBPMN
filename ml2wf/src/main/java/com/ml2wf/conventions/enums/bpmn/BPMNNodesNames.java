package com.ml2wf.conventions.enums.bpmn;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conventions.enums.TaskTagsSelector;

public enum BPMNNodesNames implements TaskTagsSelector {

	TASK("bpmn2:task"), USERTASK("bpmn2:userTask"), INCOMING("bpmn2:incoming"), OUTGOING("bpmn2:outgoing"),
	EXTENSION("bpmn2:extensionElements"), STYLE("ext:style"), DOCUMENTATION("bpmn2:documentation"), SELECTOR("");

	private String name;

	private BPMNNodesNames(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public List<String> getTaskTags() {
		List<String> tags = new ArrayList<>();
		tags.add(TASK.getName());
		tags.add(USERTASK.getName());
		return tags;
	}
}
