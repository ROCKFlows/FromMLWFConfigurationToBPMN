package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactoryImpl;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.util.XMLManager;

/**
 * This class merges a given Workflow's instance into a FeatureModel xml file.
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
public class WFInstanceMerger extends AbstractMerger {

	// TODO: make logMsg static

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFInstanceMerger.class);
	/**
	 * Instances default task tag name.
	 */
	private static final String INSTANCES_TASK = "Instances";

	/**
	 * {@code WFInstanceMerger}'s default constructor.
	 *
	 * @param filePath the XML filepath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public WFInstanceMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	@Override
	public void mergeWithWF(String filePath, boolean backUp)
			throws TransformerException, ParserConfigurationException, SAXException, IOException,
			InvalidConstraintException {
		if (backUp) {
			super.backUp();
		}
		logger.info("Starting the save action...");
		File file = new File(filePath);
		String wfTaskName;
		String logMsg;
		if (file.exists()) {
			wfTaskName = file.getName().split(Pattern.quote(XMLManager.getExtensionSeparator()))[0];
			if (this.isDuplicated(wfTaskName)) {
				logger.warn("This workflow is already in the FeatureModel");
				logger.warn("Skipping...");
				return;
			}
		} else {
			logMsg = String.format("Invalid filepath : %s", filePath);
			logger.fatal(logMsg);
			return;
		}
		Document wfDocument = XMLManager.preprocess(file);
		// create instances node
		logger.debug("Getting instances node...");
		Element instanceNode = wfDocument.createElement(FeatureModelNames.FEATURE.getName());
		instanceNode.setAttribute(FeatureModelAttributes.NAME.getName(), wfTaskName);
		this.insertNewTask(this.getInstancesTask(), instanceNode);
		// TODO: add result to WF document
		// ---
		logger.debug("Retrieving all FM document tasks...");
		List<Node> tasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		List<String> tasksNames = XMLManager.getTasksNames(tasks);
		logger.debug("Getting the constraint association...");
		String associationConstraint = ((ConstraintFactoryImpl) super.getConstraintFactory())
				.getAssociationConstraint(wfTaskName, tasksNames);
		logMsg = String.format("Generated constraint association : %s", associationConstraint);
		logger.info(logMsg);
		// add the new constraint
		logger.info("Adding association constraint...");
		this.adoptRules(this.getConstraintFactory().getRuleNodes(associationConstraint));
		// TODO: add rules to instanceNode
	}

	/**
	 * Returns the <b>instances</b> {@code Node} task.
	 *
	 * <p>
	 *
	 * Creates it if not exist.
	 *
	 * <p>
	 *
	 * @return the instances {@code Node} task
	 *
	 * @since 1.0
	 * @see Node
	 */
	private Node getInstancesTask() {
		String logMsg;
		// TODO: factorize with a similar method in WFTasksMerger
		List<Node> tasksNodes = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		for (Node taskNode : tasksNodes) {
			Node namedItem = taskNode.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName());
			if ((namedItem != null) && namedItem.getNodeValue().equals(INSTANCES_TASK)) {
				// aldready exists
				logger.debug("Instances node found.");
				return taskNode;
			}
		}
		// create the node
		// TODO: create method in XMLManager and factorize with
		// AbstractMerger#createConstraintTag()
		logger.debug("Instances node not found.");
		logger.debug("Starting creation...");
		Element instancesNode = this.getDocument().createElement(FeatureModelNames.AND.getName());
		instancesNode.setAttribute(FeatureModelAttributes.NAME.getName(), INSTANCES_TASK);
		logMsg = String.format("Instances node created : %s", instancesNode.getNodeName());
		logger.debug(logMsg);
		logger.debug("Getting suitable parent...");
		Node parent = this.getSuitableParent(instancesNode);
		logMsg = String.format("Suitable parent found : %s", parent.getNodeName());
		logger.debug(logMsg);
		return parent.appendChild(instancesNode);
	}

}
