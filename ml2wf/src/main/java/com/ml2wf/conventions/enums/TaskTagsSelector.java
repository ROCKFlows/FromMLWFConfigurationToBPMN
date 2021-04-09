package com.ml2wf.conventions.enums;

import java.util.List;

/**
 * The {@code TaskTagsSelector} interface provides a method for selecting xml
 * task tags.
 *
 * <p>
 *
 * {@code Node} task tags can change considering the type of xml standard
 * (<b>FeatureModel</b> or <b>BPMN</b> for instance).
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 *
 * @see <a href="https://featureide.github.io/">FeatureIDE framework</a>
 * @see <a href="https://www.bpmn.org/">BPMN</a>
 *
 */
@FunctionalInterface
public interface TaskTagsSelector {

	/**
	 * Returns a {@code List<String>} containing all task tags considering the type
	 * of xml file.
	 *
	 * @return a {@code List<String>} containing all task tags considering the type
	 *         of xml file
	 */
	List<String> getTaskTags();
}
