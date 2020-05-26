package com.ml2wf.conventions.enums.fm;

public enum FeatureModelAttributes {

	NAME("name"), ABSTRACT("abstract"), KEY("key"), VALUE("value");

	private String name;

	private FeatureModelAttributes(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
