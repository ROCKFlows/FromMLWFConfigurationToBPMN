package com.ml2wf.samples;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

import com.ml2wf.generation.InstanceFactoryImpl;

public class TestSamplesInstantiation {

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
	/**
	 * Basic WF XML file path.
	 */
	private static final String SIMPLE_WF_PATH = "./wf_meta/simple_wf.bpmn";

	/**
	 * Basic WF XML file path.
	 */
	private static final String RESULT_DIRECTORY = ".src/test/resources/results/";
	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesInstantiation.class);

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello World!");
		this.classLoader = this.getClass().getClassLoader();
		this.url = this.classLoader.getResource(SIMPLE_WF_PATH);
		this.factory = new InstanceFactoryImpl(new File(this.url.toURI()));
	}

	@AfterEach
	public void clean() {
		this.classLoader = null;
		this.url = null;
		this.factory = null;
	}

}
