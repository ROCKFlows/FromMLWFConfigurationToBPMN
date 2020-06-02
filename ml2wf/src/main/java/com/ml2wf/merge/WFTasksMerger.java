package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactory;
import com.ml2wf.constraints.factory.ConstraintFactoryImpl;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
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
	 * {@code ConstraintFactory}'s instance that will generate constraint nodes.
	 *
	 * @see ConstraintFactory
	 */
	private ConstraintFactory constraintFactory;
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
	 * @param filePath the XML filepath.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public WFTasksMerger(String filePath)
			throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
		this.constraintFactory = new ConstraintFactoryImpl();
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
	 * @param filePath the WF filepath.
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
	public void mergeWithWF(String filePath, boolean backUp)
			throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		// backing up if required
		if (backUp) {
			super.backUp();
		}
		logger.info("Starting the importation...");
		// preprocessing the document
		Document wfDocument = XMLManager.preprocess(new File(filePath));
		// retrieving workflow's tasks
		List<Node> tasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		// retrieving all existing FM's tasks names
		List<Node> existingTasks = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		List<String> existingTasksNames = existingTasks.stream().map(XMLManager::getNodeName)
				.collect(Collectors.toList());
		String currentTaskName;
		String debugMsg;
		// iterating for each task
		for (Node task : tasks) {
			debugMsg = String.format("Processing task : %s", task);
			logger.debug(debugMsg);
			currentTaskName = XMLManager.getNodeName(task);
			currentTaskName = currentTaskName.replaceFirst(Notation.getGeneratedPrefixVoc(), "");
			// splitting task's name
			String[] tName = currentTaskName.split(Notation.getGeneratedPrefixVoc());
			// TODO: check the tName size
			if (existingTasksNames.contains(tName[1] + "_" + tName[0])) {
				// if already contained, continue
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
		this.mergeWithWF(filePath, false);
	}

	/**
	 * Creates the <b>constraints</b> {@code Node} if it doesn't exist.
	 *
	 * @return the constraints {@code Node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	private Node createConstraintTag() {
		NodeList nodeList = this.getDocument().getElementsByTagName(FeatureModelNames.CONSTRAINTS.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		} else {
			Node consTag = this.getDocument().createElement(FeatureModelNames.CONSTRAINTS.getName());
			NodeList fmTagList = this.getDocument().getElementsByTagName(FeatureModelNames.FEATUREMODEL.getName());
			if (fmTagList.getLength() == 0) {
				// if it is an ExtendedFeatureModel
				fmTagList = this.getDocument().getElementsByTagName(FeatureModelNames.EXTENDEDFEATUREMODEL.getName());
			}
			Node rootNode = fmTagList.item(0);
			return rootNode.appendChild(consTag);
		}
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
		Node constraintsNode = this.createConstraintTag();
		List<Node> annotations = XMLManager
				.nodeListAsList(wfDocument.getElementsByTagName(BPMNNodesNames.ANNOTATION.getName()));
		for (Node annotation : annotations) {
			// TODO: improve performances (check annotation.getChildNodes().item(1)
			// sufficient ?)
			for (Node commentNode : XMLManager.nodeListAsList(annotation.getChildNodes())) {
				String comment = commentNode.getTextContent();
				List<Node> newRules = this.constraintFactory.getRuleNodes(comment);
				for (Node rule : newRules) {
					this.getDocument().adoptNode(rule);
					constraintsNode.appendChild(rule);
				}
			}
		}
		logger.info("Annotations processing ended...");
	}

	/**
	 * Returns a suitable parent for the {@code Node task} according to its
	 * specified <b>reference</b>.
	 *
	 * <p>
	 *
	 * If there isn't any valid referenced parent, returns the first document node.
	 *
	 * <p>
	 *
	 * <b>Note</b> that each instantiated task <b>refers to a generic one presents
	 * in the FeatureModel</B>.
	 *
	 * @param task task to get a suitable parent
	 * @return a suitable parent for the {@code Node task} according to its
	 *         specified reference
	 */
	private Node getSuitableParent(Node task) {
		String debugMsg = String.format("Getting location for task : %s", task.getNodeName());
		logger.debug(debugMsg);
		// retrieving the references parent
		Node docNode = ((Element) task).getElementsByTagName(BPMNNodesNames.DOCUMENTATION.getName()).item(0);
		// retrieving all candidates
		List<Node> candidates = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		// electing the good candidate
		String candidateName;
		for (Node candidate : candidates) {
			debugMsg = String.format("	Processing candidate %s...", candidate.getTextContent());
			logger.debug(debugMsg);
			candidateName = XMLManager.getNodeName(candidate);
			if (candidateName.equals(docNode.getTextContent().replace(Notation.getReferenceVoc(), ""))) {
				return candidate;
			}
		}
		debugMsg = String.format("No suitable parent was found for task %s.", task.getTextContent());
		logger.warn(debugMsg);
		logger.warn("Putting task at default location.");
		return super.getDocument().getElementsByTagName(FeatureModelNames.AND.getName()).item(0);
	}

	/**
	 * Inserts the new task corresponding of the given {@code Node task} under the
	 * given {@code Node parentNode}.
	 *
	 * <p>
	 *
	 * The new task is converted to match the FeatureModel format.
	 *
	 * @param parentNode Parent node of the new task
	 * @param task       task to insert
	 *
	 * @see Node
	 */
	private void insertNewTask(Node parentNode, Node task) {
		String debugMsg = String.format("Inserting task : %s", task.getTextContent());
		logger.debug(debugMsg);
		// retrieving task name content
		String[] taskName = XMLManager.getNodeName(task).replaceFirst(Notation.getGeneratedPrefixVoc(), "")
				.split(Notation.getGeneratedPrefixVoc());
		// converting task name to the new node name
		String newNodeName = taskName[1];
		debugMsg = String.format("task's name : %s", newNodeName);
		logger.debug(debugMsg);
		// inserting the new node
		Element newNode = super.getDocument().createElement(FeatureModelNames.FEATURE.getName());
		newNode.setAttribute(FeatureModelAttributes.NAME.getName(), newNodeName);
		parentNode.appendChild(newNode);
		logger.debug("Task inserted.");
	}
}
