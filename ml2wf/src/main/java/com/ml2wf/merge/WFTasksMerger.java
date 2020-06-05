package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactory;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.util.XMLManager;

/**
 * This class merges all given Workflow's tasks into a FeatureModel xml file.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractMerger} base class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see AbstractMerger
 *
 */
public class WFTasksMerger extends AbstractMerger {

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFTasksMerger.class);

	/**
	 * {@code FeatureModelMerger}'s default constructor.
	 *
	 * @param filePath the XML filepath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public WFTasksMerger(String filePath)
			throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	/**
	 * Merges the given <b>instantiated</b> Workflow with the {@link #fmDocument
	 * FeatureModel};
	 *
	 * <p>
	 *
	 * If {@code backUp} parameter is {@code true}, then the current source file
	 * will be backed up by the {@link #backUp()} method.
	 *
	 * <p>
	 *
	 * More precisely,
	 *
	 * <ul>
	 * <li>retrieves a suitable parent from the FeatureModel for each task using the
	 * {@link #getSuitableParent(Node)} method,</li>
	 * <li>inserts the task under this suitable parent using the
	 * {@link #insertNewTask(Node, Node)} method.</li>
	 * <li>saves the modifications using the {@link #save()} method.</li>
	 * </uL>
	 *
	 * @param filePath the WF file path.
	 * @param backUp   backs up the current {@link #getSourceFile()} or not
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see Node
	 * @see InstanceFactory
	 *
	 */
	@Override
	public void mergeWithWF(boolean backUp, String filePath)
			throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		// TODO: improve method readability
		// backing up if required
		if (backUp) {
			super.backUp();
		}
		logger.info("Starting the importation...");
		// preprocessing the document
		Document wfDocument = XMLManager.preprocess(new File(filePath));
		// retrieving workflow's tasks
		List<Node> wfTasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		// retrieving all existing FM's tasks names
		List<Node> existingTasks = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		List<String> existingTasksNames = XMLManager.getNodesNames(existingTasks);
		String currentTaskName;
		String debugMsg;
		// iterating for each task
		for (Node task : wfTasks) {
			currentTaskName = XMLManager.getNodeName(task);
			debugMsg = String.format("Processing task : %s", currentTaskName);
			logger.debug(debugMsg);
			// splitting task's name
			String[] tName = currentTaskName.split(Notation.getGeneratedPrefixVoc());
			if (tName.length < 3) {
				// TODO: factorize in a method isValidInstTaskName
				debugMsg = String.format("Bad task name for task : %s.", currentTaskName);
				logger.warn(debugMsg);
				logger.warn("Skipping...");
				continue;
			}
			if (existingTasksNames.contains(tName[2])) {
				// TODO: check factorization with method
				// AbstractMerger#isDuplicated(instTaskName)
				logger.debug("Task already in FeatureModel");
				logger.debug("Skipping...");
				continue;
			}
			// retrieving a suitable parent
			Node parentNode = this.getSuitableParent(task);
			// inserting the new task
			this.insertNewTask(parentNode, task);
		}
		this.processAnnotations(wfDocument);
		// saving result
		super.save(super.getPath());
	}

	/**
	 * Calls the {@link #mergeWithWF(String, boolean)} with {@code backUp} parameter
	 * as {@code false}.
	 *
	 * @param filePath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws InvalidConstraintException
	 */
	public void mergeWithWF(String filePath)
			throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		this.mergeWithWF(false, filePath);
	}

	/**
	 * Processes {@code document}'s annotations and adds constraints.
	 *
	 * @param wfDocument Workflow {@code Document}'s instance containing
	 *                   annotations.
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see ConstraintFactory
	 */
	private void processAnnotations(Document wfDocument) throws InvalidConstraintException {
		logger.info("Processing annotations...");

		List<Node> annotations = XMLManager
				.nodeListAsList(wfDocument.getElementsByTagName(BPMNNodesNames.ANNOTATION.getName()));
		for (Node annotation : annotations) {
			// TODO: improve performances (check annotation.getChildNodes().item(1)
			// sufficient ?)
			for (Node commentNode : XMLManager.nodeListAsList(annotation.getChildNodes())) {
				String comment = commentNode.getTextContent();
				List<Node> newRules = this.getConstraintFactory().getRuleNodes(comment);
				this.adoptRules(newRules);
			}
		}
		logger.info("Annotations processing ended...");
	}

	// TODO: manage these methods
	@Override
	protected Node getRootParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void processSpecificNeeds(Document wfDocument, String wfName) throws Exception {
		// TODO Auto-generated method stub

	}

}
