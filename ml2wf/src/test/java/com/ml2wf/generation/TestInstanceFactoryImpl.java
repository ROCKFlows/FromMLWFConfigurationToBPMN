package com.ml2wf.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.util.XMLManager;

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
@DisplayName("Test of InstanceFactoryImpl")
public class TestInstanceFactoryImpl {

	/**
	 * Instance of the class to be tested.
	 *
	 * @see InstanceFactoryImpl
	 */
	private InstanceFactoryImpl factory;
	/**
	 * XML source document.
	 *
	 * @see Document
	 */
	private Document sourceDocument;
	/**
	 * XML result document.
	 *
	 * @see Document
	 */
	private Document resultDocument;
	/**
	 * Default XML filename.
	 */
	private static final String FILE_PATH = "generic_WF_A.bpmn2";
	/**
	 * Result filename according to {@code FILE_NAME}.
	 */
	private static final String RESULT_FILE_NAME = FILE_PATH.split("\\.")[0] + Notation.getInstanceVoc() + "."
			+ FILE_PATH.split("\\.")[1];

	@BeforeEach
	public void setUp() throws TransformerException, SAXException, IOException, ParserConfigurationException,
			InvalidConstraintException {
		// loading xml test file
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(FILE_PATH);
		String fDirectory = url.getPath().replace("%20", " "); // TODO: improve sanitization
		// initializing factory
		this.factory = new InstanceFactoryImpl(fDirectory);
		// instantializing generic WF
		this.factory.getWFInstance();
		// getting xml files as documents
		this.sourceDocument = XMLManager.getDocumentFromURL(url);
		url = classLoader.getResource(RESULT_FILE_NAME);
		this.resultDocument = XMLManager.getDocumentFromURL(url);
	}

	@AfterEach
	public void clean() {
		this.factory = null;
		this.sourceDocument = null;
		this.resultDocument = null;
	}

	/**
	 * Tests that the instantiated XML file <b>contains all source file's tasks</b>.
	 *
	 * <p>
	 *
	 * More precisely, this method tests if :
	 *
	 * <ol>
	 * <li>all generic <b>tasks</b> have been instantiated <b>at least one
	 * time</b>,</li>
	 * <li>all generic <b>usertasks</b> have been instantiated <b>at least one
	 * time</b></li>
	 * </ol>
	 *
	 */
	@Test
	@DisplayName("Verification of the global structure")
	public void testGlobalStructure() {
		// TODO: to complete/improve considering the wished number of instantiated tasks
		NodeList sourceNodes = this.sourceDocument.getElementsByTagName(BPMNNodesNames.TASK.getName());
		NodeList resultNodes = this.resultDocument.getElementsByTagName(BPMNNodesNames.TASK.getName());
		assertTrue(sourceNodes.getLength() <= resultNodes.getLength()); // #1
		sourceNodes = this.sourceDocument.getElementsByTagName(BPMNNodesNames.USERTASK.getName());
		resultNodes = this.resultDocument.getElementsByTagName(BPMNNodesNames.USERTASK.getName());
		assertTrue(sourceNodes.getLength() <= resultNodes.getLength()); // #2
	}

	/**
	 * Tests that instantiated nodes have the <b>right structure</b>.
	 *
	 * <p>
	 *
	 * More precisely, this method tests if :
	 *
	 * <ol>
	 * <li>each node has an <b>extension child node</b>,</li>
	 * <li>each node has a <b>documentation child node</b>,</li>
	 * <li>each documentation node's value <b>matchs the defined regex</b></li>
	 * </ol>
	 */
	@Test
	@DisplayName("Verification of the nodes' structures")
	public void testNodesStructures() {
		// TODO: tests for usertask
		NodeList resultNodes = this.resultDocument.getElementsByTagName(BPMNNodesNames.TASK.getName());
		for (int i = 0; i < resultNodes.getLength(); i++) {
			Node node = resultNodes.item(i);
			NodeList children = node.getChildNodes();
			Node expectedExtensionNode = children.item(0);
			assertEquals(BPMNNodesNames.EXTENSION.getName(), expectedExtensionNode.getNodeName()); // #1
			// TODO: tests task color ?
			Node expectedDocNode = children.item(1);
			assertEquals(BPMNNodesNames.DOCUMENTATION.getName(), expectedDocNode.getNodeName()); // #2
			assertTrue(Pattern.matches(Notation.getDocumentationVoc() + "\\d+",
					expectedDocNode.getAttributes().getNamedItem(BPMNNodesAttributes.ID.getName()).getNodeValue())); // #3
		}
	}

	/**
	 * Tests that instantiated tasks are <b>refering the right generic tasks</b>.
	 *
	 * <p>
	 *
	 * More precisely, this method tests if :
	 *
	 * <ol>
	 * <li>reference nodes are referencing <b>all generic tasks</b>,</li>
	 * <li>each reference is referencing the <b>right generic task</b>.</li>
	 * </ol>
	 */
	@Test
	@DisplayName("Verification of references")
	public void testReferences() {
		// TODO: test for usertask
		// TODO: improve readability
		// retrieving nodes as a List
		List<Node> sourceNodes = XMLManager
				.nodeListAsList(this.sourceDocument.getElementsByTagName(BPMNNodesNames.TASK.getName()));
		List<Node> resultNodes = XMLManager
				.nodeListAsList(this.resultDocument.getElementsByTagName(BPMNNodesNames.TASK.getName()));
		// retrieving generic tasks and instantiated references
		List<String> references = resultNodes.stream().map(Node::getAttributes)
				.map(a -> a.getNamedItem(BPMNNodesAttributes.NAME.getName())).map(Node::getNodeValue)
				.map((v) -> XMLManager.sanitizeName(v)).collect(Collectors.toList());
		List<String> genericTasksNames = sourceNodes.stream().map(Node::getAttributes)
				.map(a -> a.getNamedItem(BPMNNodesAttributes.NAME.getName())).map(Node::getNodeValue)
				.map((v) -> XMLManager.sanitizeName(v)).collect(Collectors.toList());
		// comparing
		assertTrue(references.containsAll(genericTasksNames)); // #1
		resultNodes.forEach((n) -> {
			assertEquals(
					XMLManager.sanitizeName(
							n.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName()).getNodeValue()),
					XMLManager.sanitizeName(n.getChildNodes().item(1).getTextContent())); // #2
		});
	}
}
