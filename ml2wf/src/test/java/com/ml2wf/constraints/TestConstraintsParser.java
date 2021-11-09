package com.ml2wf.constraints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ml2wf.constraints.config.ConfigImpl;
import com.ml2wf.constraints.parser.ConstraintParser;
import com.ml2wf.constraints.parser.IParser;

class TestConstraintsParser {

	private static final String emptyExpression = "";
	private static final String blankExpression = "   ";
	private static final String simpleExpression = "A & B";
	private static final String complexExpression = "A => !(B | (C & !(D & E) | F) & G) & H";

	private IParser parser;

	@BeforeEach
	public void setUp() {
		parser = new ConstraintParser(ConfigImpl.getInstance());
	}

	@AfterEach
	public void clean() {
		parser = null;
	}

	@Test
	void testParseExpression() {
		assertTrue(parser.parseExpression(emptyExpression).isEmpty());
		assertTrue(parser.parseExpression(blankExpression).isEmpty());
		assertEquals(1, parser.parseExpression(simpleExpression).size());
		assertEquals(9, parser.parseExpression(complexExpression).size());
	}

}
