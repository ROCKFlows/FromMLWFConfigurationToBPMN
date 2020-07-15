package com.ml2wf.conventions.enums.fm;

/**
 * This {@code enum} contains handled attributes' names according to the
 * <a href="https://featureide.github.io/">FeatureIDE
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public enum FMAttributes {

	NAME("name"), ABSTRACT("abstract"), KEY("key"), VALUE("value"), TYPE("type");

	/**
	 * Tag name.
	 */
	private String name;

	/**
	 * {@code FeatureModelAttributes}'s constructor.
	 *
	 * @param name name of the tag
	 */
	private FMAttributes(String name) {
		this.name = name;
	}

	/**
	 * Returns the current tag's {@code name}.
	 *
	 * @return the current tag's {@code name}
	 */
	public String getName() {
		return this.name;
	}
}
