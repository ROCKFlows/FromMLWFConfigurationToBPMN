package com.ml2wf.samples;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;

public class TestSamplesInstantiation {

	/**
	 * Basic WF XML file path.
	 */
	private static final String SIMPLE_WF_PATH = "./wf_generic_samples/simple_wf.bpmn";
	
	private static final String SIMPLE_WF_PATH2 = "./wf_generic_samples/simple_wf2.bpmn";
	
	/**
	 * Basic WF XML file path.
	 */
	private static final String RESULT_DIRECTORY = "src/test/resources/results/";
	/**
	 * {@code InstanceFactoryImpl}'s instance.
	 */
	private InstanceFactoryImpl factory;
	/**
	 * {@code ClassLoader}'s instance.
	 *
	 * <p>
	 *
	 * This class is used to get resources.
	 *
	 * @see ClassLoader
	 */
	private ClassLoader classLoader;
	/**
	 * {@code URL}'s instance.
	 *
	 * <p>
	 *
	 * This class is used to get a resource's URL.
	 *
	 * @see URL
	 */
	private URL url;

	private static final Logger logger = LogManager.getLogger(TestSamplesInstantiation.class);

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException {
		logger.info("Hello World!");
		this.classLoader = this.getClass().getClassLoader();
		this.url = this.classLoader.getResource(SIMPLE_WF_PATH);
		String fDirectory = this.url.getPath().replace("%20", " "); // TODO: improve sanitization
		this.factory = new InstanceFactoryImpl(fDirectory);
		
		this.url = this.classLoader.getResource(SIMPLE_WF_PATH2);
		fDirectory = this.url.getPath().replace("%20", " "); // TODO: improve sanitization
		this.factory = new InstanceFactoryImpl(fDirectory);
	}

	@AfterEach
	public void clean() {
		this.classLoader = null;
		this.url = null;
		this.factory = null;
	}

	@Test
	@DisplayName("Test with a basic workflow")
	public void testSamples() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		this.factory.getWFInstance(RESULT_DIRECTORY);
	}
}
