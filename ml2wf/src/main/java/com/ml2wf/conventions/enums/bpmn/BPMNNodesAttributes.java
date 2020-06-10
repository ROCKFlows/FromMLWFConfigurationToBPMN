package com.ml2wf.conventions.enums.bpmn;

/**
 * This {@code enum} contains handled attributes' names according to the
 * <a href="https://www.bpmn.org/">BPMN standard</a>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public enum BPMNNodesAttributes {

	BACKGROUND("ext:shapeBackground"), ID("id"), NAME("name");

	/**
	 * Tag name.
	 */
	private String name;

	/**
	 * {@code BPMNNodesAttributes}'s constructor.
	 *
	 * @param name name of the tag
	 */
	private BPMNNodesAttributes(String name) {
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
