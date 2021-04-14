package com.ml2wf.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.AbstractXMLTest;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.util.XMLManager;

/**
 * This class tests the {@link InstanceFactoryImpl} class.
 *
 * <p>
 *
 * It is an extension of the {@code AbstractXMLTest} base class.
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
 * @see AbstractXMLTest
 * @see InstanceFactoryImpl
 */
@TestInstance(Lifecycle.PER_CLASS)
@DisplayName("Test of InstanceFactoryImpl")
class TestInstanceFactoryImpl extends AbstractXMLTest {

	@AfterEach
	public void clean() {
		testedClass = null;
		sourceDocument = null;
		resultDocument = null;
		TasksManager.clear();
		XMLManager.removeDocument();
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
	 * time</b></li>
	 * </ol>
	 *
	 * <p>
	 *
	 * <b>Note</b> that this is a {@link ParameterizedTest}.
	 *
	 * @param file the meta-wf file
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 *
	 * @see #metaFiles()
	 */
	@ParameterizedTest
	@MethodSource("metaFiles")
	@DisplayName("Verification of the global structure")
	void testGlobalStructure(File file) throws ParserConfigurationException, SAXException, IOException {
		// getting documents (source+result)
		testedClass = new InstanceFactoryImpl(file);
		sourceDocument = (Document) XMLManager.getDocument().cloneNode(true);
		((InstanceFactoryImpl) testedClass).getWFInstance();
		resultDocument = XMLManager.getDocument();
		// TODO: to complete/improve considering the wished number of instantiated tasks
		List<Node> sourceNodes = XMLManager.getTasksList(sourceDocument, BPMNNames.SELECTOR);
		List<Node> resultNodes = XMLManager.getTasksList(resultDocument, BPMNNames.SELECTOR);
		assertTrue(sourceNodes.size() <= resultNodes.size()); // #1
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
	 *
	 * <p>
	 *
	 * <b>Note</b> that this is a {@link ParameterizedTest}.
	 *
	 * @param file the meta-wf file
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 *
	 * @see #metaFiles()
	 */
	@ParameterizedTest
	@MethodSource("metaFiles")
	@DisplayName("Verification of the nodes structure")
	void testNodesStructures(File file) throws ParserConfigurationException, SAXException, IOException {
		// getting documents (source+result)
		testedClass = new InstanceFactoryImpl(file);
		sourceDocument = (Document) XMLManager.getDocument().cloneNode(true);
		((InstanceFactoryImpl) testedClass).getWFInstance();
		resultDocument = XMLManager.getDocument();
		List<Node> resultNodes = XMLManager.getTasksList(resultDocument, BPMNNames.SELECTOR);
		for (Node resultNode : resultNodes) {
			NodeList extensionChildren = ((Element) resultNode)
					.getElementsByTagName(BPMNNames.EXTENSION.getName());
			assertTrue(extensionChildren.getLength() > 0);
			NodeList docChildren = ((Element) resultNode).getElementsByTagName(BPMNNames.DOCUMENTATION.getName());
			assertTrue(docChildren.getLength() > 0);
			Node docNode = docChildren.item(0);
			assertTrue(Pattern.matches(Notation.DOCUMENTATION_VOC + "\\d+",
					docNode.getAttributes().getNamedItem(BPMNAttributes.ID.getName()).getNodeValue()));
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
	 * <li>meta-tasks' names are <b>not blank</b>,</li>
	 * <li>instances' references are <b>not blank</b>,</li>
	 * <li>reference nodes are referencing <b>all generic tasks</b>.</li>
	 * </ol>
	 *
	 * <p>
	 *
	 * <b>Note</b> that this is a {@link ParameterizedTest}.
	 *
	 * @param file the meta-wf file
	 *
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 *
	 * @see #metaFiles()
	 */
	@ParameterizedTest
	@MethodSource("metaFiles")
	@DisplayName("Verification of references")
	void testReferences(File file) throws ParserConfigurationException, SAXException, IOException {
		// getting documents (source+result)
		testedClass = new InstanceFactoryImpl(file);
		sourceDocument = (Document) XMLManager.getDocument().cloneNode(true);
		((InstanceFactoryImpl) testedClass).getWFInstance();
		resultDocument = XMLManager.getDocument();
		// retrieving task nodes
		List<Node> sourceNodes = XMLManager.getTasksList(sourceDocument, BPMNNames.SELECTOR);
		List<Node> resultNodes = XMLManager.getTasksList(resultDocument, BPMNNames.SELECTOR);
		// retrieving meta-tasks' names
		List<String> metaTasksNames = getNames(sourceNodes, BPMNAttributes.NAME.getName(), false);
		assertFalse(metaTasksNames.stream().anyMatch(String::isBlank)); // #1
		// retrieving instances' references
		List<String> references = getReferences(resultNodes);
		assertFalse(references.stream().anyMatch(String::isBlank)); // #2
		// comparing references with meta-tasks' names
		assertTrue(references.containsAll(metaTasksNames)); // #3
	}
}
