package com.ml2wf.conventions.enums.fm;

public enum FeatureModelNames {

	FEATUREMODEL("extendedFeatureModel"), PROPERTIES("properties"), STRUCT("struct"), AND("and"), GRAPHICS("graphics"),
	FEATURE("feature"), ALT("alt"), DESCRIPTION("description"), CONSTRAINTS("constraints"), RULE("rule");

	private String name;

	private FeatureModelNames(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
