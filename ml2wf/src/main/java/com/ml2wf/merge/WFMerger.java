package com.ml2wf.merge;

/**
 * This interface provides a method for the <b>merging</b> of an instantiated
 * workflow into a FeatureModel.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see <a href="https://en.wikipedia.org/wiki/Feature_model">FeatureModel</a>
 *
 */
public interface WFMerger {

	/**
	 * Merges the workflow at {@code filePath} into the WF document.
	 *
	 * <p>
	 *
	 * Saves the current FeatureModel if {@code backup} is {@code true}.
	 *
	 * @param filePath path of the workflow's path.
	 * @param backUp   indicates if a backup is needed
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void mergeWithWF(String filePath, boolean backUp) throws Exception;
}
