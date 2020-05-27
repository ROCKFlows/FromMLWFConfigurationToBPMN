package com.ml2wf.generation;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.ml2wf.save.FeatureModelMerger;

/**
 * This class tests the {@link FeatureModelMerger} class.
 *
 * <p>
 *
 * Tests are executed with the <a href="https://junit.org/junit5/">JUnit
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @see FeatureModelMerger
 *
 */
@DisplayName("Test of FeatureModelMerger")
public class TestFeatureModelMerger {

	/**
	 * Instance of the class to be tested.
	 *
	 * @see FeatureModelMerger
	 */
	private FeatureModelMerger merger;
	/**
	 * Default XML FM filename.
	 */
	private static final String FM_FILE_NAME = "model_A.xml";
	/**
	 * Default XML WF filename.
	 */
	private static final String WF_FILE_NAME = "generic_WF_A_instance.bpmn2";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// ---
		// TODO: factorize with TestInstanceFactoryImpl#setUp
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(FM_FILE_NAME);
		String fDirectory = url.getPath().replace("%20", " ").replace(FM_FILE_NAME, "");
		// ---
		this.merger = new FeatureModelMerger(fDirectory, FM_FILE_NAME);
		// ---
		// TODO: factorize with TestInstanceFactoryImpl#setUp
		url = classLoader.getResource(WF_FILE_NAME);
		fDirectory = url.getPath().replace("%20", " ").replace(WF_FILE_NAME, "");
		// ---
		this.merger.mergeWithWF(fDirectory, WF_FILE_NAME);
	}

	@AfterEach
	public void clean() {
		this.merger = null;
	}

	@Test
	public void testIgnored() {

	}
}
