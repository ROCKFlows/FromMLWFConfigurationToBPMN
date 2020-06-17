package com.ml2wf.merge.base;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.ml2wf.util.Pair;

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
public interface BaseMerger {

	/**
	 * Merges the workflow file at {@code filesPath} location into the WF document.
	 *
	 * <p>
	 *
	 * Saves the current FeatureModel before any modification if {@code backup} is
	 * {@code true}.
	 *
	 * @param filePath      path of the workflow file.
	 * @param backUp        indicates if a backup is needed
	 * @param completeMerge indicates whether the workflow has to be completely
	 *                      merged or not
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void mergeWithWF(boolean backUp, boolean completeMerge, String filePath) throws Exception;

	/**
	 * Merges the workflow files at {@code filesPath} location into the WF document.
	 *
	 * <p>
	 *
	 * Saves the current FeatureModel before any modification if {@code backup} is
	 * {@code true}.
	 *
	 * @param filesPath     paths of the workflow files.
	 * @param backUp        indicates if a backup is needed
	 * @param completeMerge indicates whether the workflow has to be completely
	 *                      merged or not
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void mergeWithWF(boolean backUp, boolean completeMerge, String... filesPath) throws Exception;

	/**
	 * Returns a suitable parent {@code Node} for the given {@code Node}.
	 *
	 * @return a suitable parent {@code Node} for the given {@code Node}
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public abstract Node getSuitableParent(Node child);

	/**
	 * Returns the root parent {@code Node} according to the workflow's type (meta
	 * or instance).
	 *
	 * <p>
	 *
	 * This root parent {@code Node} is used during the workflow's task insertion
	 * (complete merge).
	 *
	 * @return the root parent {@code Node} according to the workflow's type (meta
	 *         or instance)
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public abstract Node getRootParentNode();

	/**
	 * Processes specific needs to complete the merge operation.
	 *
	 * <p>
	 *
	 * e.g. add references
	 *
	 * @param wfInfo {@code Pair} containing the {@code Document} and its name
	 * @throws Exception
	 *
	 * @since 1.0
	 *
	 * @see Pair
	 * @see Document
	 */
	public void processSpecificNeeds(Pair<String, Document> wfInfo) throws Exception;
}
