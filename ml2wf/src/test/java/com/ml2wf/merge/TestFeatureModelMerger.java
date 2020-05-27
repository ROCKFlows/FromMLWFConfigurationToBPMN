package com.ml2wf.merge;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.util.XMLManager;

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
	 * XML WF source document.
	 *
	 * @see Document
	 */
	private Document sourceWFDocument;
	/**
	 * XML FM result document.
	 *
	 * @see Document
	 */
	private Document resultFMDocument;
	/**
	 * Default XML FM filename.
	 */
	private static final String FM_SOURCE_FILE_NAME = "model_A.xml";
	/**
	 * Default result XML FM filename.
	 */
	private static final String FM_RESULT_FILE_NAME = "model_A_result.xml";
	/**
	 * Default XML WF filename.
	 */
	private static final String WF_SOURCE_FILE_NAME = "generic_WF_A_instance.bpmn2";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// ---
		// TODO: factorize with TestInstanceFactoryImpl#setUp
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(WF_SOURCE_FILE_NAME);
		String fDirectory = url.getPath().replace("%20", " ").replace(WF_SOURCE_FILE_NAME, "");
		// ---
		this.merger = new FeatureModelMerger(fDirectory, FM_SOURCE_FILE_NAME);
		this.sourceWFDocument = XMLManager.getDocumentFromURL(url);
		// ---
		// TODO: factorize with TestInstanceFactoryImpl#setUp
		url = classLoader.getResource(WF_SOURCE_FILE_NAME);
		fDirectory = url.getPath().replace("%20", " ").replace(WF_SOURCE_FILE_NAME, "");
		// ---
		this.merger.mergeWithWF(fDirectory, WF_SOURCE_FILE_NAME);
		url = classLoader.getResource(FM_RESULT_FILE_NAME);
		this.resultFMDocument = XMLManager.getDocumentFromURL(url);
	}

	@AfterEach
	public void clean() {
		this.merger = null;
		this.sourceWFDocument = null;
		this.resultFMDocument = null;
	}

	/**
	 * Tests that the merged XML file <b>contains all new WF's tasks</b>.
	 *
	 * <p>
	 *
	 * More precisely, this method tests if :
	 *
	 * <ol>
	 * <li>all WF's tasks are present in the result XML FM file.</li>
	 * </ol>
	 *
	 */
	@Test
	@DisplayName("Test of merging feature")
	public void testMergingStructure() {
		// TODO: to complete/improve
		// getting WF's source task nodes
		List<Node> sourceNodes = XMLManager
				.nodeListAsList(this.sourceWFDocument.getElementsByTagName(BPMNNodesNames.TASK.getName()));
		sourceNodes.addAll(
				XMLManager.nodeListAsList(this.sourceWFDocument.getElementsByTagName(BPMNNodesNames.USERTASK.getName())));
		// getting FM tasks
		List<Node> resultNodes = XMLManager
				.nodeListAsList(this.resultFMDocument.getElementsByTagName(FeatureModelNames.AND.getName()));
		resultNodes.addAll(
				XMLManager.nodeListAsList(this.resultFMDocument.getElementsByTagName(FeatureModelNames.ALT.getName())));
		resultNodes.addAll(XMLManager
				.nodeListAsList(this.resultFMDocument.getElementsByTagName(FeatureModelNames.FEATURE.getName())));
		// getting tasks' names
		List<String> sourceNodesNames = sourceNodes.stream().map(Node::getAttributes)
				.map(a -> a.getNamedItem(BPMNNodesAttributes.NAME.getName())).map(Node::getNodeValue)
				.map((v) -> XMLManager.sanitizeName(v)).collect(Collectors.toList());
		List<String> resultNodesNames = resultNodes.stream().map(Node::getAttributes)
				.map(a -> a.getNamedItem(FeatureModelAttributes.NAME.getName())).map(Node::getNodeValue)
				.map((v) -> XMLManager.sanitizeName(v)).collect(Collectors.toList());
		// testing
		assertTrue(resultNodesNames.containsAll(sourceNodesNames)); // #1
	}
}
