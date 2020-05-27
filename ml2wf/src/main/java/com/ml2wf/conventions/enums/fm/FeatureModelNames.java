package com.ml2wf.conventions.enums.fm;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conventions.enums.TaskTagsSelector;

public enum FeatureModelNames implements TaskTagsSelector {

	FEATUREMODEL("extendedFeatureModel"), PROPERTIES("properties"), STRUCT("struct"), AND("and"), GRAPHICS("graphics"),
	FEATURE("feature"), ALT("alt"), DESCRIPTION("description"), CONSTRAINTS("constraints"), RULE("rule"), SELECTOR("");

	private String name;

	private FeatureModelNames(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public List<String> getTaskTags() {
		List<String> tags = new ArrayList<>();
		tags.add(FEATURE.getName());
		tags.add(AND.getName());
		tags.add(ALT.getName());
		return tags;
	}
}
