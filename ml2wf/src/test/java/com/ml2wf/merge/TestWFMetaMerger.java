package com.ml2wf.merge;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.AbstractXMLTest;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.util.FileHandler;
import com.ml2wf.util.XMLManager;

/**
 * This class tests the {@link WFTasksMerger} class.
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
 * @version 1.0
 *
 * @see AbstractXMLTest
 * @see WFTasksMerger
 *
 */
@DisplayName("Test of WFTasksMerger")
public class TestWFMetaMerger extends AbstractXMLTest {

	/**
	 * Default XML filename.
	 */
	private static final String FM_FILE_PATH = "./feature_models/model.xml";
	/**
	 * {@code ClassLoader}'s instance used to get resources.
	 *
	 * @see ClassLoader
	 */
	protected static ClassLoader classLoader = TestWFMetaMerger.class.getClassLoader();

	@BeforeEach
	public void setUp() throws TransformerException, SAXException, IOException, ParserConfigurationException,
			InvalidConstraintException, URISyntaxException {
		// loading FM file
		this.testedClass = new WFMetaMerger(new File(classLoader.getResource(FM_FILE_PATH).toURI()));
	}

	@AfterEach
	public void clean() throws IOException, URISyntaxException {
		this.testedClass = null;
		this.sourceDocument = null;
		this.resultDocument = null;
		TasksManager.clear();
		XMLManager.removeDocument();
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
	 * <p>
	 *
	 * <b>Note</b> that this is a {@link ParameterizedTest}.
	 *
	 * @param file the wf file
	 * @throws Exception
	 *
	 * @since 1.0
	 * @see {@link #metaFiles()}
	 */
	@ParameterizedTest
	@MethodSource("metaFiles")
	@DisplayName("Test of merging feature")
	public void testMergingStructure(File file)
			throws Exception {
		((WFMetaMerger) this.testedClass).mergeWithWF(false, true, file);
		this.sourceDocument = FileHandler.preprocess(file);
		this.resultDocument = XMLManager.getDocument();
		// getting WF's source task nodes
		List<Node> sourceNodes = XMLManager.getTasksList(this.sourceDocument, BPMNNames.SELECTOR);
		// getting FM tasks
		List<Node> resultNodes = XMLManager.getTasksList(this.resultDocument, FMNames.SELECTOR);
		// getting tasks' names

		List<String> sourceNodesNames = sourceNodes.stream()
				.flatMap(n -> ((AbstractMerger) this.testedClass).getNestedNodes(n).stream()) // flattening
				.map(Node::getAttributes) // getting attributes
				.map(a -> a.getNamedItem(BPMNAttributes.NAME.getName())) // getting Name attribute
				.map(Node::getNodeValue) // getting name value
				.map((v) -> XMLManager.sanitizeName(v))
				.collect(Collectors.toList());
		List<String> resultNodesNames = resultNodes.stream().map(Node::getAttributes)
				.map(a -> a.getNamedItem(FMAttributes.NAME.getName())).map(Node::getNodeValue)
				.map((v) -> XMLManager.sanitizeName(v)).collect(Collectors.toList());

		// testing
		assertTrue(resultNodesNames.containsAll(sourceNodesNames)); // #1
	}

	// TODO: add a test checking that generated nodes are children of the right
	// parents
}
