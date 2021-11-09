package com.ml2wf.constraints.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This class tests the {@link OperAssociation} class.
 *
 * <p>
 *
 * Tests are executed with the <a href="https://junit.org/junit5/">JUnit
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 *
 * @see OperAssociation
 */
class TestOperAssociation {

	/**
	 * An operator.
	 */
	private static final String operator = "&";
	/**
	 * A left operand.
	 */
	private static final String leftOperand = "A";
	/**
	 * A right operand.
	 */
	private static final String rightOperand = "B";

	/**
	 * An empty {@code OperAssociation}.
	 */
	private OperAssociation emptyAssociation;
	/**
	 * An {@code OperAssociation} only containing an operator.
	 */
	private OperAssociation soloOperatorAssociation;
	/**
	 * An {@code OperAssociation} containing an operator and a left operand.
	 */
	private OperAssociation leftOperandOperatorAssociation;
	/**
	 * An {@code OperAssociation} containing an operator and a right operand.
	 */
	private OperAssociation rightOperandOperatorAssociation;
	/**
	 * A full {@code OperAssociation}.
	 */
	private OperAssociation fullOperatorAssociation;

	@BeforeEach
	public void setUp() {
		emptyAssociation = new OperAssociation();
		soloOperatorAssociation = new OperAssociation(operator);
		leftOperandOperatorAssociation = new OperAssociation(operator, Arrays.asList(leftOperand, ""));
		rightOperandOperatorAssociation = new OperAssociation(operator, Arrays.asList("", rightOperand));
		fullOperatorAssociation = new OperAssociation(operator,
				Arrays.asList(leftOperand, rightOperand));

	}

	@AfterEach
	public void clean() {
		emptyAssociation = null;
		soloOperatorAssociation = null;
		leftOperandOperatorAssociation = null;
		rightOperandOperatorAssociation = null;
	}

	/**
	 * Tests class' getters.
	 */
	@Test
	@DisplayName("Test of getters")
	void testGetters() {
		assertNull(emptyAssociation.getOperator());
		assertNull(emptyAssociation.getLeftOperand());
		assertNull(emptyAssociation.getRightOperand());
		assertTrue(emptyAssociation.getOperands().isEmpty());
		assertEquals(operator, soloOperatorAssociation.getOperator());
		assertEquals(leftOperand, leftOperandOperatorAssociation.getLeftOperand());
		assertEquals(rightOperand, rightOperandOperatorAssociation.getRightOperand());
		assertEquals(2, fullOperatorAssociation.getOperands().size());
	}

	/**
	 * Tests class' setters.
	 */
	@Test
	@DisplayName("Test of setters")
	void testSetters() {
		emptyAssociation.setOperator(operator);
		assertEquals(operator, emptyAssociation.getOperator());
		emptyAssociation.addOperand(leftOperand);
		emptyAssociation.addOperand(rightOperand);
		assertEquals(2, emptyAssociation.getOperands().size());
		assertEquals(leftOperand, emptyAssociation.getLeftOperand());
		assertEquals(rightOperand, emptyAssociation.getRightOperand());
		assertFalse(emptyAssociation.addOperand(leftOperand));
	}

	/**
	 * Tests {@link OperAssociation#hasLeftOperand() hasLeftOperand} and
	 * {@link OperAssociation#hasRightOperand() hasRightOperand} methods.
	 */
	@Test
	@DisplayName("Test of hasLeft() and hasRight() methods")
	void hasLeftRigthOperand() {
		assertTrue(fullOperatorAssociation.hasLeftOperand());
		assertTrue(fullOperatorAssociation.hasRightOperand());
	}

}
