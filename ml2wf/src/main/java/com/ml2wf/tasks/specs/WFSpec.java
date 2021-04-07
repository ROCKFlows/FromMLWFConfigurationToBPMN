package com.ml2wf.tasks.specs;

public interface WFSpec<T> extends Spec<T> {

	/**
	 * Returns the specification contained in the given {@code content} in its
	 * documenting format.
	 *
	 * @param content content to format the specification
	 * @return the specification contained in the given {@code content} in its
	 *         documenting format
	 */
	public default String formatSpec(String content) {
		return "";
	}
}
