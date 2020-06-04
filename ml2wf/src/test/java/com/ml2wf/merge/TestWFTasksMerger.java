package com.ml2wf.merge;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.util.XMLManager;

/**
 * This class tests the {@link WFTasksMerger} class.
 *
 * <p>
 *
 * Tests are executed with the <a href="https://junit.org/junit5/">JUnit
 * framework</a>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see WFTasksMerger
 *
 */
@DisplayName("Test of WFTasksMerger")
public class TestWFTasksMerger {

	/**
	 * Instance of the class to be tested.
	 *
	 * @see WFTasksMerger
	 */
	private WFTasksMerger merger;
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
	private static final String FM_SOURCE_FILE_PATH = "model_A.xml";
	private static final String FM_SOURCE_FILE_PATH2 = "model_example.xml";
	/**
	 * Default XML FM result filename.
	 */
	private static final String FM_RESULT_FILE_PATH = "model_A_result.xml";
	private static final String FM_RESULT_FILE_PATH2 = "model_example_result.xml";
	/**
	 * Default XML WF filename.
	 */
	private static final String WF_SOURCE_FILE_PATH = "./wf_generic_samples/generic_WF_A_instance.bpmn2";
	private static final String WF_SOURCE_FILE_PATH2 = "./wf_generic_samples/simple_wf2_instance.bpmn2";
	/**
	 * Back up path.
	 *
	 * @see XMLManager
	 */
	private String backUpPath;

	/**
	 * Updates the {@link #backUpPath}.
	 *
	 * @return the back up path.
	 */

	private String updateBackUpPath(String fm_source_file_path) {
		SimpleDateFormat dateFormater = null;
		Date backUpDate = new Date();
		dateFormater = new SimpleDateFormat("_dd_MM_yy_hh_mm");
		String[] splittedPath = fm_source_file_path.split("\\.");
		this.backUpPath = splittedPath[0] + Notation.getBackupVoc() + dateFormater.format(backUpDate) + "."
				+ splittedPath[1];
		return this.backUpPath;
	}

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		// TODO: factorize with TestInstanceFactoryImpl#setUp
		// --- retrieving FM resource
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL url = classLoader.getResource(FM_SOURCE_FILE_PATH);
		String fDirectory = url.getPath().replace("%20", " ");
		this.merger = new WFTasksMerger(fDirectory);
		this.sourceWFDocument = XMLManager.getDocumentFromURL(url);
		// --- retrieving instatiated WF resource
		url = classLoader.getResource(WF_SOURCE_FILE_PATH);
		fDirectory = url.getPath().replace("%20", " ");
		this.merger.mergeWithWF(true, fDirectory); // backing up
		// --- retrieving FM result resource
		url = classLoader.getResource(this.updateBackUpPath(FM_SOURCE_FILE_PATH));
		this.resultFMDocument = XMLManager.getDocumentFromURL(url);
	}

	@AfterEach
	public void clean() throws IOException, URISyntaxException {
		this.merger = null;
		this.sourceWFDocument = null;
		this.resultFMDocument = null;
		this.cleanTestDir();
		this.backUpPath = null;
	}

	/**
	 * Cleans the src/test/resources directory.
	 *
	 * <p>
	 *
	 * It allows other tests to be performed independently.
	 *
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void cleanTestDir() throws IOException, URISyntaxException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		// saving the modified FM file
		URL url = classLoader.getResource(FM_SOURCE_FILE_PATH);
		Path path = Paths.get(url.toURI());
		Files.copy(path, path.resolveSibling(FM_RESULT_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
		// replacing the FM source file by the backed up one
		url = classLoader.getResource(this.backUpPath);
		path = Paths.get(url.toURI());
		Files.move(path, path.resolveSibling(FM_SOURCE_FILE_PATH), StandardCopyOption.REPLACE_EXISTING);
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
				XMLManager
						.nodeListAsList(this.sourceWFDocument.getElementsByTagName(BPMNNodesNames.USERTASK.getName())));
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
