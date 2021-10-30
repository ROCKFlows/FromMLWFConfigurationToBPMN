package com.ml2wf.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ml2wf.constraints.config.ConfigImpl;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.IParser;

public class TestConstraintsParser {

	private static String emptyExpression = "";
	private static String blankExpression = "   ";
	private static String simpleExpression = "A & B";
	private static String complexExpression = "A => !(B | (C & !(D & E) | F) & G) & H";

	private IParser parser;

	@BeforeEach
	public void setUp() {
		this.parser = new ConstraintParser(ConfigImpl.getInstance());
	}

	@AfterEach
	public void clean() {
		this.parser = null;
	}

	@Test
	public void testParseExpression() {
		assertTrue(this.parser.parseExpression(emptyExpression).isEmpty());
		assertTrue(this.parser.parseExpression(blankExpression).isEmpty());
		assertEquals(1, this.parser.parseExpression(simpleExpression).size());
		assertEquals(9, this.parser.parseExpression(complexExpression).size());
	}

}
