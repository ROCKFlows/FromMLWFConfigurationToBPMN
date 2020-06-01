package com.ml2wf.constraints.parser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.ml2wf.constraints.config.ConfigImpl;
import com.ml2wf.constraints.tree.BinaryTree;
import com.ml2wf.constraints.util.OperAssociation;

/**
 * This class is a constraint parser.
 *
 * <p>
 *
 * More precisely, it provides methods to extract constraints from a given text
 * based on the given {@code Config}'s instance and to return them into a
 * {@code BinaryTree} structure.
 *
 * <p>
 *
 * It is an implementation of the {@link Parser} interface.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Parser
 * @see ConfigImpl
 * @see BinaryTree
 */
public class ConstraintParser implements Parser {

	/**
	 * {@code Config}'s instance used to define delimiters and regex patterns.
	 *
	 * @see ConfigImpl
	 */
	private ConfigImpl config;

	/**
	 * {@code ConstraintParser}'s default instance.
	 *
	 * @param cfg the {@code Config} instance
	 *
	 * @see ConfigImpl
	 */
	public ConstraintParser(ConfigImpl cfg) {
		this.setConfig(cfg);
	}

	/**
	 * Returns the {@code Config} instance.
	 *
	 * @return the {@code Config} instance
	 *
	 * @see ConfigImpl
	 */
	public ConfigImpl getConfig() {
		return this.config;
	}

	/**
	 * Sets the {@code Config} instance.
	 *
	 * @see ConfigImpl
	 */
	public void setConfig(ConfigImpl cfg) {
		this.config = cfg;
	}

	@Override
	public List<BinaryTree<String>> parseContent(String content) {
		List<BinaryTree<String>> result = new ArrayList<>();
		content = this.encloseUnary(content);
		for (String constraint : this.getConstraintParts(content)) {
			result.add(this.constraintMapToTree(this.computeDepth(this.getSplittedConstraints(constraint))));
		}
		return result;
	}

	@Override
	public List<OperAssociation> parseExpression(String expression) {
		List<OperAssociation> result = new ArrayList<>();
		List<String> operators = this.config.getOperatorsList();
		operators = operators.stream().map(Pattern::quote).collect(Collectors.toList());
		// regex part
		String regex = String.format("(\\w*)(%s| +)(\\w*)", String.join("|", operators));
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(expression.replace(" ", ""));
		while (matcher.find()) {
			if (matcher.groupCount() == 3) {
				String operator = matcher.group(2);
				List<String> operands = Arrays.asList(matcher.group(1), matcher.group(3));
				result.add(new OperAssociation(operator, operands));
			}
		}
		return result;
	}

	/**
	 * Converts a {@code Map} containing the depth representation of a constraint
	 * obtained with the {@link #computeDepth(List)} method to a {@code BinaryTree}.
	 *
	 * <p>
	 *
	 * TODO: explain algorithm
	 *
	 * @param depthMap depth representation of a constraint
	 * @return a {@code BinaryTree} representing the {@code depthMap}
	 */
	private BinaryTree<String> constraintMapToTree(Map<Integer, Deque<String>> depthMap) {
		BinaryTree<String> result = new BinaryTree<>();
		Deque<String> operList; // list of operands/operators
		for (Map.Entry<Integer, Deque<String>> entry : depthMap.entrySet()) {
			// foreach constraint
			operList = entry.getValue();
			for (String brutOper : operList) {
				List<OperAssociation> parsed = this.parseExpression(brutOper);
				BinaryTree<String> tmpTree = this.getTreeFromParsedCons(parsed);
				result.insertWhenPossible(tmpTree);
			}
		}
		return result;
	}

	/**
	 * Converts a {@code List} of parsed constraints obtained with the
	 * {@link #parseExpression(String)} method to a {@code BinaryTree}.
	 *
	 * <p>
	 *
	 * TODO: explain algorithm
	 *
	 * @param parsedOper {@code List} of {@code OperAssociation}
	 * @return a {@code BinaryTree} representing the {@code parsedOper}
	 *
	 * @since 1.0
	 * @see BinaryTree
	 * @see OperAssociation
	 */
	private BinaryTree<String> getTreeFromParsedCons(List<OperAssociation> parsedOper) {
		// TODO: decompose this method and improve performances
		boolean lastInsertionLeft = false;
		String buffer = ""; // buffer that will contains buffered operands
		BinaryTree<String> tree = new BinaryTree<>();
		BinaryTree<String> created;
		for (OperAssociation operAssociation : parsedOper) {
			// foreach operator-operands associations
			String operator = operAssociation.getOperator();
			created = tree.addRightChild(operator); // add operator
			if (this.config.isUnaryOperator(operator)) {
				// if operator is unary
				created.blockLeftChild(true); // block left child due to unary
				if (operAssociation.hasRightOperand()) {
					// if unaryoperand is directly associated to an operand
					tree.addRightChild(operAssociation.getRightOperand()); // add this operand
					lastInsertionLeft = false;
				}
			} else {
				// it is a binary operator
				if (operAssociation.hasLeftOperand()) {
					// if has a left operand
					// add this operand
					tree.setLeftChild(operAssociation.getLeftOperand());
					tree.blockLeftChild(true); // block left child due to operand
					lastInsertionLeft = true;
				}
				if (operAssociation.hasRightOperand()) {
					// if has a right operand
					// save it in the buffer
					if (!buffer.isBlank()) {
						tree.setLeftChild(buffer);
						tree.blockLeftChild(true); // block left child due to operand
						lastInsertionLeft = true;
					}
					buffer = operAssociation.getRightOperand();
				}
			}
		}
		if (!buffer.isEmpty()) {
			if (lastInsertionLeft) {
				tree.addRightChild(buffer);
				tree.blockRightChild(true); // block left child due to operand
			} else {
				tree.addLeftChild(buffer);
				tree.blockLeftChild(true); // block left child due to operand
			}

		}
		return tree;
	}

	/**
	 * Splits the given {@code constraint} and returns a {@code List} of elements
	 * composing the constraint (parentheses, operators/operands).
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * [In] 	"!(A & (B | C | D) => E)"
	 * [Out]	["!", "(", "A &", "(", "B | C | D", ")", "=> E", ")"]
	 * </code>
	 * </pre>
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method uses the class {@code StringTokenizer} for
	 * parsing.
	 *
	 * @param constraint text containing constraints
	 * @return a {@code List} of elements composing the constraint (parentheses,
	 *         operators/operands)
	 *
	 * @since 1.0
	 * @see StringTokenizer
	 */
	private List<String> getSplittedConstraints(String constraint) {
		List<String> result = new ArrayList<>();
		StringTokenizer tokenizer;
		tokenizer = new StringTokenizer(constraint, "(.*)", true);
		String nextToken;
		while (tokenizer.hasMoreTokens()) {
			nextToken = tokenizer.nextToken();
			result.add(nextToken);
		}
		return result;
	}

	/**
	 * Returns a {@code Map} with : {Key=depth,Value=[Operator,Operands} for each
	 * constraint in {@code constraints}.
	 *
	 * <p>
	 *
	 * The depth corresponds of the nested levels of parentheses.
	 *
	 * <p>
	 *
	 * For instance,
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 * [In]	["!", "(", "A &", "(", "B | C | D", ")", "=> E", ")"]
	 * [Out]	{
	 * 		0: ["!"],
	 * 		1: ["A &", "=> E"],
	 * 		2: ["B | C | D"]
	 * 	}
	 * </code>
	 * </pre>
	 *
	 * @param constraints a {@code List} of constraints
	 * @return a {@code Map} with : {Key=depth,Value=[Operator,Operands}
	 *
	 * @since 1.0
	 */
	private Map<Integer, Deque<String>> computeDepth(List<String> constraints) {
		Map<Integer, Deque<String>> depth = new TreeMap<>();
		int separatorCounter = 0;
		for (String element : constraints) {
			if (element.equals("(")) {
				separatorCounter++;
			} else if (element.equals(")")) {
				separatorCounter--;
			} else {
				if (!depth.containsKey(separatorCounter)) {
					depth.put(separatorCounter, new ArrayDeque<>());
				}
				depth.get(separatorCounter).add(element.replace(" ", "")); // TODO: factorize in method
			}
		}
		return depth;
	}

	/**
	 * Encloses each unary operator and its operand in brackets and returns the
	 * modified expression.
	 *
	 * <p>
	 *
	 * This ensures that the tree generated by the method
	 * {@link #getTreeFromParsedCons(List)} respect the unary priority.
	 *
	 * <p>
	 *
	 * An example of result after treatment is the following :
	 *
	 * <pre>
	 * <code>
	 * [In] 	"!(A & !(B | C) => D)"
	 * [Out]	"(!(A & (!(B | C)) => D))"
	 * </code>
	 * </pre>
	 *
	 *
	 * @param expression expression containing operators/operands
	 * @return the modified expression
	 *
	 * @since 1.0
	 */
	private String encloseUnary(String expression) {
		// TODO: replace by config file (get(0))
		String unaryOp = this.config.getUnaryOperators().get(0);
		StringBuilder builder = new StringBuilder();
		int bracketCounter = 0;
		int unaryCounter = 0;
		for (int i = 0; i < expression.length(); i++) {
			// foreach character in the expression
			String currentChar = String.valueOf(expression.charAt(i));
			if (currentChar.equals(unaryOp)) {
				// if it is an unary operator
				unaryCounter++;
				builder.append("("); // open the unary operator linked bracket
			} else if (currentChar.equals("(")) {
				// if it is an opening bracket
				bracketCounter++; // increment the nested level
			} else if (currentChar.equals(")")) {
				// if it is a closing bracket
				bracketCounter--; // decrement the nested level
				if (unaryCounter > bracketCounter) {
					// if an unary operator was operating
					// on this bracket
					unaryCounter--; // decrease the number of active unary linked bracket
					builder.append(")"); // close the unary operator linked bracket
				}
			} else if ((this.config.getOperatorsList().contains(currentChar) || currentChar.equals(")"))
					&& (unaryCounter > bracketCounter)) {
				// if it is an operator or a closing bracket and there is no nested bracket
				unaryCounter--;
				builder.append(")"); // close the unary operator linked bracket
			}
			builder.append(currentChar);
		}
		while (bracketCounter > 0) {
			// while still opened brackets
			builder.append(")");
			bracketCounter--;
		}
		while (unaryCounter > 0) {
			// while still unary operator linked opened bracket
			builder.append(")");
			unaryCounter--;
		}
		return builder.toString();
	}

	/**
	 * Returns all constraint parts in the given {@code textContent}.
	 *
	 * <p>
	 *
	 * A constraint part is for instance :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>[[(!(A & B) | C)]]</code>
	 * </pre>
	 *
	 * where <code>[[</code> and <code>]]</code> are delimiters.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method uses <b>regex</b> for the extraction.
	 *
	 * @param textContent text containing constraint parts
	 * @return all constraint parts in the given {@code textContent}
	 *
	 * @since 1.0
	 * @see Pattern
	 * @see Matcher
	 */
	private List<String> getConstraintParts(String textContent) {
		String regex = String.format("%s(.*?)%s", Pattern.quote("[["), Pattern.quote("]]"));
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(textContent);
		List<String> constraintParts = new ArrayList<>();
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				constraintParts.add(matcher.group(i));
			}
		}
		return constraintParts;
	}

}
