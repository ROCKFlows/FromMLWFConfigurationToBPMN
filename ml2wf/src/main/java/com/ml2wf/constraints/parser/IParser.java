package com.ml2wf.constraints.parser;

import java.util.List;

import com.ml2wf.constraints.tree.BinaryTree;
import com.ml2wf.constraints.util.OperAssociation;

/**
 * This interface provides a method to extract constraints from a given text.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
public interface IParser {

	/**
	 * Parses {@code content} and returns a {@code List} of {@code BinaryTree}
	 * representing the constraints contained in {@code content}.
	 *
	 * <p>
	 *
	 * This method allows the parsing of multiple constraint parts in a single text.
	 *
	 * <p>
	 *
	 * Each returned {@code BinaryTree} is representing a constraint.
	 *
	 * @param content text to parse
	 * @return a {@code List} of {@code BinaryTree} representing the constraints
	 *         contained in {@code content}
	 */
	public List<BinaryTree<String>> parseContent(String content);

	/**
	 * Parsed an expression and returns a {@code List} of {@code OperAssociation}.
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * [In] 	"B | C | D"
	 * [Out]	[
	 * 		OperAssociation("|", ["B","C"]),
	 * 		OperAssociation("|", ["","D"])
	 * 	]
	 * </code>
	 * </pre>
	 *
	 * <p>
	 *
	 * <b>Note</b> that operandLeft or operandRight can be blank.
	 *
	 * @param expression text to parse
	 * @return a {@code List} of {@code OperAssociation}
	 *
	 * @since 1.0
	 * @see OperAssociation
	 */
	public List<OperAssociation> parseExpression(String expression);

	/**
	 * Returns whether the given {@code constraintText} is an order constraint or
	 * not.
	 *
	 * @param constraintText text to parse
	 * @return whether the given {@code constraintText} is an order constraint or
	 *         not
	 * 
	 * @since 1.0
	 */
	public boolean isOrderConstraint(String constraintText);
}
