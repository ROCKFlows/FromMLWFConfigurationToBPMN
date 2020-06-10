package com.ml2wf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.w3c.dom.Document;

import com.ml2wf.util.XMLManager;

/**
 * This class contains all required field and method to efficiently test
 * instances of the {@code XMLManager} class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see XMLManager
 *
 */
public abstract class AbstractXMLTest {

	/**
	 * Instance of the tested class.
	 *
	 * @see XMLManager
	 */
	protected XMLManager testedClass;
	/**
	 * XML source document.
	 *
	 * @see Document
	 */
	protected Document sourceDocument;
	/**
	 * XML result document.
	 *
	 * @see Document
	 */
	protected Document resultDocument;
	/**
	 * {@code ClassLoader}'s instance used to get resources.
	 *
	 * @see ClassLoader
	 */
	protected static ClassLoader classLoader = AbstractXMLTest.class.getClassLoader();
	/**
	 * The BPMN extension used to filter files.
	 */
	protected static final String BPMN_EXTENSION = ".bpmn";
	/**
	 * Instance workflows' directory.
	 */
	protected static final String INSTANCES_DIRECTORY = "./wf_instances/";

	/**
	 * Returns all {@code Path}'s instances in the {@code INSTANCES_DIRECTORY}
	 * directory.
	 *
	 * @return all {@code Path}'s instances in the {@code INSTANCES_DIRECTORY}
	 *         directory
	 * @throws IOException
	 * @throws URISyntaxException
	 *
	 * @since 1.0
	 */
	protected static Stream<Path> instanceFiles() throws IOException, URISyntaxException {
		URI uri = classLoader.getResource(INSTANCES_DIRECTORY).toURI();
		Path myPath = Paths.get(uri);
		return Files.walk(myPath, 1).filter(p -> p.toString().endsWith(BPMN_EXTENSION));
	}
}
