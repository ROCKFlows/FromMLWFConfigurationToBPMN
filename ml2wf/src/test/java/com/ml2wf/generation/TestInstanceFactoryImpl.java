package com.ml2wf.generation;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the {@link InstanceFactoryImpl} class.
 *
 * <p>
 *
 * Tests are executed with the <a href="https://junit.org/junit5/">JUnit
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @see InstanceFactoryImpl
 *
 */
public class TestInstanceFactoryImpl {
	/**
	 * Instance of the class to be tested.
	 *
	 * @see InstanceFactoryImpl
	 */
	private InstanceFactoryImpl factory;
	/**
	 * Default path to the XML file's directory.
	 */
	private static final String PATH = "../BPMN - Models/";
	/**
	 * Default XML filename.
	 */
	private static final String FILE_NAME = "MetaModel_AD.bpmn2";

	@BeforeEach
	public void setUp() throws IOException {
		this.factory = new InstanceFactoryImpl(PATH, FILE_NAME);
	}

	@AfterEach
	public void clean() {
		this.factory = null;
	}

	@Test
	public void testGetInstance() {
		// this.factory.getWFInstance();
	}
}
