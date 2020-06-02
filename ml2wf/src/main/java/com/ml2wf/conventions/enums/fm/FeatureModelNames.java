package com.ml2wf.conventions.enums.fm;

import java.util.ArrayList;
import java.util.List;

import com.ml2wf.conventions.enums.TaskTagsSelector;

public enum FeatureModelNames implements TaskTagsSelector {

	// general tags
	FEATUREMODEL("featureModel"), EXTENDEDFEATUREMODEL("extendedFeatureModel"), PROPERTIES("properties"),
	STRUCT("struct"), GRAPHICS("graphics"),
	DESCRIPTION("description"),
	// task tags
	FEATURE("feature"), ALT("alt"), AND("and"),
	// constraint tags
	CONSTRAINTS("constraints"), RULE("rule"), IMPLIES("imp"), NOT("not"), EQUIVALENT("equ"), CONJ("conj"), DISJ("disj"),
	VAR("var"),
	// reserved tags
	SELECTOR("");

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
