package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

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
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

/**
 * This class contains useful methods for merging a Workflow element into a
 * FeatureModel xml file.
 *
 * <p>
 *
 * It is an extension of the {@link XMLManager} base class.
 *
 * <p>
 *
 * It aims at the application of <b>metalearning</b> for workflow automation as
 * part of the <b>ml2wf project</b>.
 *
 * <p>
 *
 * It is an implementation of the {@link WFMerger} interface.
 *
 * <p>
 *
 * Please refer to the <a href="https://featureide.github.io/">FeatureIDE
 * framework</a> for further information about a FeatureModel.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see XMLManager
 *
 */
public abstract class AbstractMerger extends XMLManager {

	/**
	 * Default parent's name.
	 *
	 * <p>
	 *
	 * Unmanaged nodes will be placed under this parent.
	 */
	private static String DEFAULT_PARENT_NAME = "Unmanaged";
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
	private static final Logger logger = LogManager.getLogger(AbstractMerger.class);

	/**
	 * {@code AbstractMerger}'s default constructor.
	 *
	 * @param file the XML {@code File}.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public AbstractMerger(File file) throws ParserConfigurationException, SAXException, IOException {
		super(file);
		this.constraintFactory = new ConstraintFactoryImpl(getDocument());
	}

	/**
	 * Returns the {@code ConstraintFactory}'s instance.
	 *
	 * @return the {@code ConstraintFactory}'s instance
	 *
	 * @see ConstraintFactory
	 */
	public ConstraintFactory getConstraintFactory() {
		return this.constraintFactory;
	}

	/**
	 * Returns the workflow's document and name with the given {@code filePath}
	 * using the {@link Pair} class. If the {@code filePath} is not a valid path or
	 * the workflow's name is already in the feature model, return an empty
	 * {@code Pair}.
	 *
	 * @param file {@code File} instance of the workflow
	 * @return a {@code Pair} association between the workflow's name and its
	 *         {@code Document} instance or an empty {@code Pair} if something is
	 *         wrong.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 *
	 * @since 1.0
	 * @see Pair
	 * @see Document
	 */
	protected Pair<String, Document> getWFDocInfoFromPath(File file)
			throws ParserConfigurationException, SAXException, IOException {
		String wfTaskName;
		String logMsg;
		Document wfDocument;
		if (file.exists()) {
			wfDocument = XMLManager.preprocess(file);
			wfTaskName = getWorkflowName(wfDocument).replace(" ", "_");
			logMsg = String.format("WF's name is %s.", wfTaskName);
			logger.debug(logMsg);
			if (this.isDuplicated(wfTaskName)) {
				logger.warn("This workflow is already in the FeatureModel");
				logger.warn("Skipping...");
				return new Pair<>();
			}
		} else {
			logMsg = String.format("Invalid filepath : %s", file.getAbsolutePath());
			logger.fatal(logMsg);
			return new Pair<>();
		}
		return new Pair<>(wfTaskName, wfDocument);
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
	protected void processAnnotations(Document wfDocument) throws InvalidConstraintException {
		logger.info("Processing annotations...");
		List<Node> annotations = XMLManager
				.nodeListAsList(wfDocument.getElementsByTagName(BPMNNodesNames.ANNOTATION.getName()));
		List<Pair<Node, Node>> orderPairs; // pair that will contain order constraint data
		for (Node annotation : annotations) {
			// TODO: improve performances (check annotation.getChildNodes().item(1)
			// sufficient ?)
			for (Node commentNode : XMLManager.nodeListAsList(annotation.getChildNodes())) {
				String comment = commentNode.getTextContent();
				orderPairs = this.getConstraintFactory().getOrderNodes(comment);
				if (!orderPairs.isEmpty()) {
					// this is an order constraint
					this.processOrderConstraint(orderPairs);
				} else {
					// this is a "classic" constraint
					List<Node> newRules = this.getConstraintFactory().getRuleNodes(comment);
					this.adoptRules(newRules);
				}
			}
		}
		logger.info("Annotations processing ended...");
	}

	/**
	 * Processes the order constraints.
	 *
	 * *
	 * <p>
	 *
	 * An association constraint is an order implication between two features.
	 *
	 * <p>
	 *
	 * e.g. A before B, C after D
	 *
	 * @param orderPairs {@code List} of {@code Pair} containing the LCA
	 *                   {@code Node} as key and the descriptive {@code Node} as
	 *                   value
	 *
	 * @since 1.0
	 *
	 * @see Node
	 * @see Pair
	 */
	protected void processOrderConstraint(List<Pair<Node, Node>> orderPairs) {
		for (Pair<Node, Node> pair : orderPairs) {
			Node docNode = this.createTag(((Element) pair.getKey()), FeatureModelNames.DESCRIPTION);
			if (!XMLManager.mergeNodesTextContent(docNode, pair.getValue().getTextContent())) {
				logger.error("The merge operation for description nodes failed.");
			}
		}
	}

	/**
	 * Processes the association constraints.
	 *
	 * <p>
	 *
	 * An association constraint is an implication between the {@code wfName} and
	 * the {@code wfDocument}'s tasks.
	 *
	 * @param wfDocument workflow's document
	 * @param wfName     workflow's name
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 */
	protected void processAssocConstraints(Document wfDocument, String wfName) throws InvalidConstraintException {
		String logMsg;
		logger.debug("Retrieving all FM document tasks...");
		List<Node> tasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		List<String> tasksNames = this.getTasksNames(tasks);
		logger.debug("Getting the constraint association...");
		String associationConstraint = ((ConstraintFactoryImpl) this.getConstraintFactory())
				.getAssociationConstraint(wfName, tasksNames);
		logMsg = String.format("Generated constraint association : %s", associationConstraint);
		logger.info(logMsg);
		// add the new constraint
		logger.info("Adding association constraint...");
		this.adoptRules(this.getConstraintFactory().getRuleNodes(associationConstraint));
	}

	/**
	 * Creates the <b>tagName</b> {@code Node} and append it to the given
	 * {@code parent} if it doesn't exist.
	 *
	 * @param parent  {@code parent} of {@code tagName Node}
	 * @param tagName name of tag to be created
	 * @return the created tag
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node createTag(Element parent, FeatureModelNames tagName) {
		NodeList nodeList = parent.getElementsByTagName(tagName.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		}
		Node newTag = getDocument().createElement(tagName.getName());
		return parent.appendChild(newTag);
	}

	/**
	 * Creates the <b>constraints</b> {@code Node} if it doesn't exist.
	 *
	 * @return the constraints {@code Node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node createConstraintTag() {
		NodeList nodeList = getDocument().getElementsByTagName(FeatureModelNames.CONSTRAINTS.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		} else {
			Node consTag = getDocument().createElement(FeatureModelNames.CONSTRAINTS.getName());
			NodeList fmTagList = getDocument().getElementsByTagName(FeatureModelNames.FEATUREMODEL.getName());
			if (fmTagList.getLength() == 0) {
				// if it is an ExtendedFeatureModel
				fmTagList = getDocument().getElementsByTagName(FeatureModelNames.EXTENDEDFEATUREMODEL.getName());
			}
			Node rootNode = fmTagList.item(0);
			return rootNode.appendChild(consTag);
		}
	}

	/**
	 * Creates if needed and returns the Unmanaged abstract task {@code Node}.
	 *
	 * @return the Unmanaged abstract task {@code Node}
	 *
	 * @since 1.0
	 */
	protected Node createUnmanagedAbstractTask() {
		Node parent = getDocument().getElementsByTagName(FeatureModelNames.AND.getName()).item(1);

		// TODO: factorize with #isDuplicated
		List<Node> andNodes = new ArrayList<>();
		andNodes.addAll(
				XMLManager.nodeListAsList(getDocument().getElementsByTagName(FeatureModelNames.AND.getName())));
		for (Node andNode : andNodes) {
			if (andNode.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName()).getNodeValue()
					.equals(DEFAULT_PARENT_NAME)) {
				return andNode;
			}
		}
		Element child = getDocument().createElement(FeatureModelNames.AND.getName());
		child.setAttribute(FeatureModelAttributes.ABSTRACT.getName(), String.valueOf(true));
		child.setAttribute(FeatureModelAttributes.NAME.getName(), DEFAULT_PARENT_NAME);
		return parent.appendChild(child);
	}

	/**
	 * Inserts and returns the new task corresponding of the given {@code Node task}
	 * under the
	 * given {@code Node parentNode}.
	 *
	 * <p>
	 *
	 * The new task is converted to match the FeatureModel format.
	 *
	 * @param parentNode Parent node of the new task
	 * @param task       task to insert
	 * @return the added child
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node insertNewTask(Node parentNode, Node task) {
		// TODO: recurse for nested tasks
		String debugMsg = String.format("Inserting task : %s", task.getTextContent());
		logger.debug(debugMsg);
		// retrieving task name content
		String taskName = XMLManager.getNodeName(task);
		taskName = XMLManager.sanitizeName(taskName);
		debugMsg = String.format("task's name : %s", taskName);
		logger.debug(debugMsg);
		// inserting the new node
		Node newFeature = this.createFeatureWithName(taskName);
		return parentNode.appendChild(newFeature);
	}

	/**
	 * Returns whether the given instantiated task's name is duplicated or not.
	 *
	 * <p>
	 *
	 * An instantiated task's name that is already in the document
	 * is considered as duplicated.
	 *
	 * <p>
	 *
	 * <b>Note</b> that an instantiated task's name is in the form :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>Notation.getGeneratedPrefixVoc() + genericTaskName + Notation.getGeneratedPrefixVoc() + newTaskName</code>
	 * </pre>
	 *
	 * @param task the instantiated task's name
	 * @return whether the given {@code task} is duplicated or not
	 *
	 * @since 1.0
	 */
	protected boolean isDuplicated(String instTaskName) {
		// TODO: remove if not necessary (due to the instance tasks' naming changing)
		// get all tasks
		List<Node> tasks = XMLManager.getTasksList(getDocument(), FeatureModelNames.SELECTOR);
		// get task name
		String target = XMLManager.sanitizeName(instTaskName);
		return XMLManager.getNodesNames(tasks).contains(target);
	}

	/**
	 * Adopts and adds new {@code rules} to the FM document's constraint tag.
	 *
	 * @param rules {@code List} of {@code Node} rules to add to the FM document
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	protected void adoptRules(List<Node> rules) {
		Node constraintsNode = this.createConstraintTag();
		// getting constraints
		List<Node> constraints = new ArrayList<>(
				XMLManager.nodeListAsList(getDocument().getElementsByTagName(FeatureModelNames.RULE.getName())));
		for (Node rule : rules) {
			if (constraints.stream().noneMatch(rule::isEqualNode)) {
				// if it is not duplicated constraint
				getDocument().adoptNode(rule);
				constraintsNode.appendChild(rule);
			}
		}
	}

	/**
	 * Creates and returns a new feature with the given {@code name}.
	 *
	 * @param name name of the feature
	 * @return a new feature with the given {@code name}
	 *
	 * @since 1.0
	 */
	protected Element createFeatureWithName(String name) {
		Element feature = getDocument().createElement(FeatureModelNames.FEATURE.getName());
		feature.setAttribute(FeatureModelAttributes.NAME.getName(), name);
		return feature;
	}

	/**
	 * Returns the given {@code task}'s name.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method is different from the
	 * {@link XMLManager#getNodeName(Node)} because it only returns the deepest
	 * subtask's name.
	 *
	 * @param task task to get name
	 * @return the given {@code task}'s name
	 *
	 * @since 1.0
	 */
	protected String getTaskName(Node task) {
		String logMsg = String.format("Retrieving name for task : %s...", task);
		logger.debug(logMsg);
		String taskName = XMLManager.getNodeName(task);
		String[] splitted = taskName.split(Notation.getGeneratedPrefixVoc());
		taskName = (splitted.length > 0) ? splitted[splitted.length - 1] : taskName;
		logMsg = String.format("Task's name is : %s", taskName);
		logger.debug(logMsg);
		return taskName;
	}

	/**
	 * Returns a {@code List} containing all {@code nodes}' tasks names.
	 *
	 * @param nodes nodes to get the names
	 * @return a {@code List} containing all {@code nodes}' tasks names
	 *
	 * @since 1.0
	 * @see NodeList
	 */
	protected List<String> getTasksNames(List<Node> tasks) {
		return tasks.stream().map(this::getTaskName)
				.collect(Collectors.toList());
	}

	/**
	 * Returns a {@code List} of nested {@code Element} referred by the given
	 * {@code node}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that nested nodes are created using the given {@code node}'s
	 * data.
	 *
	 * @param node node to extract nested ones
	 * @return a {@code List} of nested {@code Node} referred by the given
	 *         {@code node}
	 *
	 * @since 1.0
	 */
	protected List<Node> getNestedNodes(Node node) {
		List<Node> result = new ArrayList<>();
		// retrieving all nested nodes' names
		String[] nodeName = XMLManager.getNodeName(node).split(Notation.getGeneratedPrefixVoc());
		List<String> names = new ArrayList<>(Arrays.asList(nodeName));
		names.removeIf(String::isBlank); // removing blanks
		// Manage the parentNode
		Element parentNode = (Element) node.cloneNode(true);
		parentNode.setAttribute(BPMNNodesAttributes.NAME.getName(), names.remove(0));
		result.add(parentNode);
		// foreach nested node's name
		for (String name : names) {
			parentNode = this.createNestedNode(parentNode, name);
			result.add(parentNode);
		}
		return result;
	}

	/**
	 * Creates and returns the nested {@code Node} according to the {@code parent}
	 * data.
	 *
	 * <p>
	 *
	 * <b>Note</b> that the created {@code Node} is not added to the
	 * {@code parent}'s children.
	 *
	 * @param parent parent of the created nested node
	 * @param name   name of the nested node
	 * @return the nested {@code Node} according to the {@code parent} data
	 *
	 * @since 1.0
	 */
	protected Element createNestedNode(Element parent, String name) {
		Element created = getDocument().createElement(parent.getNodeName());
		created.setAttribute(FeatureModelAttributes.NAME.getName(), name);
		if (!this.isMetaTask(parent)) {
			this.addDocumentationNode(created, parent.getAttribute(BPMNNodesAttributes.NAME.getName()));
		}
		return created;
	}

}
