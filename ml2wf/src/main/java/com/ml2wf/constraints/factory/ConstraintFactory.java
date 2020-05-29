package com.ml2wf.constraints.factory;

import org.w3c.dom.Node;

/**
 * This interface provides a method for the <b>generation of constraint
 * nodes</b>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public interface ConstraintFactory {

	/**
	 * Generates a rule {@code Node} containing all constraints nodes from the given
	 * {@code constraintText}.
	 *
	 * @param constraintText text containing constraints
	 * @return a {@code List} of generated constraint nodes
	 *
	 * @since 1.0
	 * @see Node
	 */
	public Node generateRuleNode(String constraintText);
}
