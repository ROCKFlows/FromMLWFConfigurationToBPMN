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
import lombok.Getter;
import lombok.Setter;

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
 * It is an implementation of the {@link IParser} interface.
 *
 * @author Nicolas Lacroix
 *
 * @see IParser
 * @see ConfigImpl
 * @see BinaryTree
 *
 * @since 1.0.0
 */
public class ConstraintParser implements IParser {

    private static final String EXPRESSION_REGEX = "([\\w\\s]*)(%s+)([\\w\\s]*)";
    /**
     * {@code Config}'s instance used to define delimiters and regex patterns.
     *
     * @see ConfigImpl
     */
    @Getter @Setter
    private ConfigImpl config;

    /**
     * {@code ConstraintParser}'s default instance.
     *
     * @param cfg the {@code Config} instance
     *
     * @see ConfigImpl
     */
    public ConstraintParser(ConfigImpl cfg) {
        setConfig(cfg);
    }

    @Override
    public List<BinaryTree<String>> parseContent(String content) {
        List<BinaryTree<String>> result = new ArrayList<>();
        content = encloseUnary(content);
        for (String constraint : getConstraintParts(content)) {
            result.add(constraintMapToTree(computeDepth(getSplittedConstraints(constraint))));
        }
        return result;
    }

    @Override
    public List<OperAssociation> parseExpression(String expression) {
        List<OperAssociation> result = new ArrayList<>();
        List<String> operators = config.getOperatorsList();
        operators = operators.stream().map(Pattern::quote).collect(Collectors.toList());
        // regex part
        String regex = String.format(EXPRESSION_REGEX, String.join("|", operators));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            if (matcher.groupCount() == 3) {
                String operator = matcher.group(2);
                List<String> operands = Arrays.asList(matcher.group(1).trim().replace(" ", "_"),
                        matcher.group(3).trim().replace(" ", "_"));
                result.add(new OperAssociation(operator, operands));
            }
        }
        return result;
    }

    @Override
    public boolean isOrderConstraint(String constraintText) {
        return config.getOrderOperator().stream().anyMatch(constraintText::contains);
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
        Deque<String> operandsOperatorsList;
        for (Map.Entry<Integer, Deque<String>> entry : depthMap.entrySet()) {
            // foreach constraint
            operandsOperatorsList = entry.getValue();
            for (String brutOperandOperator : operandsOperatorsList) {
                List<OperAssociation> parsed = parseExpression(brutOperandOperator);
                BinaryTree<String> tmpTree = getTreeFromParsedCons(parsed);
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
     * Pseudo-code :
     *
     * <p>
     *
     * <pre>
     * <code>
     * function getTreeFromParsedCons(parsedOperAssociations: List)
     *   lastInsertionLeft <- false
     *   buffer <- empty string
     *   tree <- empty tree
     *   foreach operAssociation in parsedOperAssociations do
     *     operator <- operAssociation.operator
     *     created <- tree.addRightChild(operator)
     *     if config.isUnaryOperator(operator) then
     *       created.blockLeftChild(true)
     *       if operAssociation.hasRightOperand() then
     *         tree.addRightChild(operAssocation.rightOperand)
     *         lastInsertionLeft <- false
     *       end
     *     else
     *       if operAssociation.hasLeftOperand() then
     *         tree.leftChild <- operAssociation.leftOperand
     *         tree.blockLeftChild(true)
     *         lastInsertionLeft <- true
     *       end
     *       if operAssociation.hasRightOperand() then
     *         if not buffer.isBlank() then
     *           created <- tree.addLeftChild(buffer)
     *           created.block(true)
     *           lastInsertionLeft <- true
     *         end
     *         buffer <- operAssociation.rightOperand
     *       end
     *     end
     *   end
     *   if not buffer.isEmpty() then
     *     if lastInsertionLeft is true then
     *       tree.addRightChild(buffer)
     *       tree.blockRightChild(false)
     *     else
     *       tree.addLeftChild(buffer)
     *       tree.blockLeftChild(true)
     *     end
     *   end
     *   return tree
     * end
     * </code>
     * </pre>
     *
     * @param parsedOperAssociations {@code List} of {@code OperAssociation}
     * @return a {@code BinaryTree} representing the {@code parsedOperAssociations}
     *
     * @since 1.0
     * @see BinaryTree
     * @see OperAssociation
     */
    private BinaryTree<String> getTreeFromParsedCons(List<OperAssociation> parsedOperAssociations) {
        // TODO: decompose this method and improve performances
        boolean lastInsertionLeft = false;
        String buffer = "";
        BinaryTree<String> tree = new BinaryTree<>();
        BinaryTree<String> created;
        for (OperAssociation operAssociation : parsedOperAssociations) {
            // foreach operator-operands associations
            String operator = operAssociation.getOperator();
            // add operator
            created = tree.addRightChild(operator);
            if (config.isUnaryOperator(operator)) {
                // if operator is unary
                // block left child
                created.blockLeftChild(true);
                if (operAssociation.hasRightOperand()) {
                    // if unary operand is directly associated to an operand
                    // add this operand
                    tree.addRightChild(operAssociation.getRightOperand());
                    lastInsertionLeft = false;
                }
            } else {
                // it is a binary operator
                if (operAssociation.hasLeftOperand()) {
                    // if has a left operand
                    // add this operand
                    tree.setLeftChild(operAssociation.getLeftOperand());
                    // block left child due to operand
                    tree.blockLeftChild(true);
                    lastInsertionLeft = true;
                }
                if (operAssociation.hasRightOperand()) {
                    // if has a right operand
                    // save it in the buffer
                    if (!buffer.isBlank()) {
                        created = tree.addLeftChild(buffer);
                        // block left child due to operand
                        created.block(true);
                        lastInsertionLeft = true;
                    }
                    buffer = operAssociation.getRightOperand();
                }
            }
        }
        if (!buffer.isEmpty()) {
            if (lastInsertionLeft) {
                tree.addRightChild(buffer);
                // block left child due to operand
                tree.blockRightChild(true);
            } else {
                tree.addLeftChild(buffer);
                // block left child due to operand
                tree.blockLeftChild(true);
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
    private static List<String> getSplittedConstraints(String constraint) {
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
    private static Map<Integer, Deque<String>> computeDepth(List<String> constraints) {
        Map<Integer, Deque<String>> depth = new TreeMap<>();
        int separatorCounter = 0;
        for (String element : constraints) {
            if (element.equals("(")) {
                separatorCounter++;
            } else if (element.equals(")")) {
                separatorCounter--;
            } else {
                depth.computeIfAbsent(separatorCounter, ArrayDeque::new);
                depth.get(separatorCounter).add(element);
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
        String unaryOp = config.getUnaryOperators().get(0);
        StringBuilder builder = new StringBuilder();
        int bracketCounter = 0;
        int unaryCounter = 0;
        for (int i = 0; i < expression.length(); i++) {
            // foreach character in the expression
            String currentChar = String.valueOf(expression.charAt(i));
            if (currentChar.equals(unaryOp)) {
                // if it is an unary operator
                unaryCounter++;
                // open the unary operator linked bracket
                builder.append("(");
            } else if (currentChar.equals("(")) {
                // if it is an opening bracket
                // increment the nested level
                bracketCounter++;
            } else if (currentChar.equals(")")) {
                // if it is a closing bracket
                // decrement the nested level
                bracketCounter--;
                if (unaryCounter > bracketCounter) {
                    // if an unary operator was operating on this bracket
                    // decrease the number of active unary linked bracket
                    unaryCounter--;
                    // close the unary operator linked bracket
                    builder.append(")");
                }
            } else if ((config.getOperatorsList().contains(currentChar) || ")".equals(currentChar))
                    && (unaryCounter > bracketCounter)) {
                // if it is an operator or a closing bracket and there is no nested bracket
                unaryCounter--;
                // close the unary operator linked bracket
                builder.append(")");
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
    private static List<String> getConstraintParts(String textContent) {
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
