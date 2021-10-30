package com.ml2wf.constraints.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent an association between an operator and operands.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
public class OperAssociation {

    /**
     * An operator.
     */
    @Getter @Setter
    private String operator;
    /**
     * A {@code List} of operands.
     */
    @Getter private final List<String> operands;

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
        this.operands = new ArrayList<>(operands);
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
     * Adds an operand to the operands {@code List}.
     *
     * @param operand a new operand
     * @return whether the operation succeed or not
     */
    public boolean addOperand(String operand) {
        if (operands.size() < 2) {
            return operands.add(operand);
        }
        return false;
    }

    /**
     * Returns the left operand associated to the operator.
     *
     * @return the left operand associated to the operator.
     */
    public String getLeftOperand() {
        if (hasLeftOperand()) {
            return operands.get(0);
        }
        return null;
    }

    /**
     * Returns the right operand associated to the operator.
     *
     * @return the right operand associated to the operator.
     */
    public String getRightOperand() {
        if (hasRightOperand()) {
            return operands.get(1);
        }
        return null;
    }

    /**
     * Returns whether the current operator is associated to a left operand or not.
     *
     * @return whether the current operator is associated to a left operand or not.
     */
    public boolean hasLeftOperand() {
        return (operands.size() == 2) && !operands.get(0).isBlank();
    }

    /**
     * Returns whether the current operator is associated to a right operand or not.
     *
     * @return whether the current operator is associated to a right operand or not.
     */
    public boolean hasRightOperand() {
        return (operands.size() == 2) && !operands.get(1).isBlank();
    }

}
