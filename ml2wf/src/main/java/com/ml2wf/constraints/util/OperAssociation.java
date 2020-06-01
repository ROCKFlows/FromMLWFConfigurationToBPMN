package com.ml2wf.constraints.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an association between an operator and operands.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public class OperAssociation {

	/**
	 * An operator.
	 */
	private String operator;
	/**
	 * A {@code List} of operands.
	 */
	private List<String> operands;

	/**
	 * {@code OperAssociation}'s complete constructor.
	 *
	 * <p>
	 *
	 * This constructor initializes an {@code OperAssociation} instance with an
	 * {@code operator} associated to a {@code List} of operands.
	 *
	 * @param operator An operator
	 * @param operands A {@code List} of operands
	 */
	public OperAssociation(String operator, List<String> operands) {
		this.operator = operator;
		this.operands = operands;
	}

	/**
	 * {@code OperAssociation}'s partial constructor.
	 *
	 * <p>
	 *
	 * This constructor initializes an {@code OperAssociation} instance with an
	 * {@code operator} associated to an empty {@code List} of operands.
	 *
	 * @param operator An operator
	 */
	public OperAssociation(String operator) {
		this(operator, new ArrayList<>());
	}

	/**
	 * {@code OperAssociation}'s default constructor.
	 *
	 * <p>
	 *
	 * This constructor initializes an {@code OperAssociation}.
	 */
	public OperAssociation() {
		this(null);
	}

	/**
	 * Returns the {@code operator} of the current association.
	 *
	 * @return the {@code operator} of the current association.
	 */
	public String getOperator() {
		return this.operator;
	}

	/**
	 * Sets the current {@code operator}.
	 *
	 * @param operator new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Returns the {@code List} of operands the current association.
	 *
	 * @return the {@code List} of operands the current association.
	 */
	public List<String> getOperands() {
		return this.operands;
	}

	/**
	 * Adds an operand to the operands {@code List}.
	 *
	 * @param operand a new operand
	 * @return whether the operation succeed or not
	 */
	public boolean addOperand(String operand) {
		if (this.operands.size() < 2) {
			return this.operands.add(operand);
		}
		return false;
	}

	/**
	 * Returns the left operand associated to the operator.
	 *
	 * @return the left operand associated to the operator.
	 */
	public String getLeftOperand() {
		if (this.hasLeftOperand()) {
			return this.operands.get(0);
		}
		return null;
	}

	/**
	 * Returns the right operand associated to the operator.
	 *
	 * @return the right operand associated to the operator.
	 */
	public String getRightOperand() {
		if (this.hasRightOperand()) {
			return this.operands.get(1);
		}
		return null;
	}

	/**
	 * Returns whether the current operator is associated to a left operand or not.
	 *
	 * @return whether the current operator is associated to a left operand or not.
	 */
	public boolean hasLeftOperand() {
		return (this.operands.size() == 2) && !this.operands.get(0).isBlank();
	}

	/**
	 * Returns whether the current operator is associated to a right operand or not.
	 *
	 * @return whether the current operator is associated to a right operand or not.
	 */
	public boolean hasRightOperand() {
		return (this.operands.size() == 2) && !this.operands.get(1).isBlank();
	}

}
