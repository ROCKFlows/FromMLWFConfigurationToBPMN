package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public abstract class AbstractMerger extends XMLManager implements WFMerger {

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
	 * @param filePath the XML filepath.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public AbstractMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
		this.constraintFactory = new ConstraintFactoryImpl();
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

	@Override
	public void mergeWithWF(boolean backUp, String... filesPath) throws Exception {
		for (String path : filesPath) {
			this.mergeWithWF(backUp, path);
		}
	}

	/**
	 * Returns the workflow's document and name with the given {@code filePath}
	 * using the {@link Pair} class. If the
	 * {@code filePath} is not a valid path or the workflow's name is already in the
	 * feature model, return null.
	 *
	 * @param filePath file path of the workflow
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
	protected Pair<String, Document> getWFDocInfoFromPath(String filePath)
			throws ParserConfigurationException, SAXException, IOException {
		File file = new File(filePath);
		String wfTaskName;
		String logMsg;
		Document wfDocument;
		if (file.exists()) {
			wfDocument = XMLManager.preprocess(file);
			wfTaskName = getWorkflowName(wfDocument).replace(" ", "_");
			logMsg = String.format("WF's name is %s.", wfTaskName);
			logger.debug(logMsg);
			// file.getName().split(Pattern.quote(XMLManager.getExtensionSeparator()))[0];
			if (this.isDuplicated(wfTaskName)) {
				logger.warn("This workflow is already in the FeatureModel");
				logger.warn("Skipping...");
				return new Pair<>();
			}
		} else {
			logMsg = String.format("Invalid filepath : %s", filePath);
			logger.fatal(logMsg);
			return new Pair<>();
		}
		return new Pair<>(wfTaskName, wfDocument);
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
	 * Creates the <b>constraints</b> {@code Node} if it doesn't exist.
	 *
	 * @return the constraints {@code Node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node createConstraintTag() {
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
	 *
	 * @since 1.0
	 */
	protected Node getSuitableParent(Node task) {
		String debugMsg = String.format("Getting location for task : %s", task.getNodeName());
		logger.debug(debugMsg);
		// retrieving the references parent
		Node docNode = ((Element) task).getElementsByTagName(BPMNNodesNames.DOCUMENTATION.getName()).item(0);
		if (docNode != null) {
			// if contains a documentation node that can refer to a generic task
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
		}
		debugMsg = String.format("No suitable parent was found for task %s.", task.getTextContent());
		logger.warn(debugMsg);
		logger.warn("Putting task at default location.");
		return this.createUnmanagedAbstractTask();
	}

	/**
	 * Creates if needed and returns the Unmanaged abstract task {@code Node}.
	 *
	 * @return the Unmanaged abstract task {@code Node}
	 *
	 * @since 1.0
	 */
	protected Node createUnmanagedAbstractTask() {
		Node parent = super.getDocument().getElementsByTagName(FeatureModelNames.AND.getName()).item(1);

		// TODO: factorize with #isDuplicated
		List<Node> andNodes = new ArrayList<>();
		andNodes.addAll(
				XMLManager.nodeListAsList(this.getDocument().getElementsByTagName(FeatureModelNames.AND.getName())));
		for (Node andNode : andNodes) {
			if (andNode.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName()).getNodeValue()
					.equals(DEFAULT_PARENT_NAME)) {
				return andNode;
			}
		}
		Element child = super.getDocument().createElement(FeatureModelNames.AND.getName());
		child.setAttribute(FeatureModelAttributes.ABSTRACT.getName(), String.valueOf(true));
		child.setAttribute(FeatureModelAttributes.NAME.getName(), DEFAULT_PARENT_NAME);
		return parent.appendChild(child);
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
	 * @since 1.0
	 * @see Node
	 */
	protected void insertNewTask(Node parentNode, Node task) {
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
		parentNode.appendChild(newFeature);
		logger.debug("Task inserted.");
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
		List<Node> tasks = XMLManager.getTasksList(this.getDocument(), FeatureModelNames.SELECTOR);
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
				XMLManager.nodeListAsList(this.getDocument().getElementsByTagName(FeatureModelNames.RULE.getName())));
		for (Node rule : rules) {
			if (constraints.stream().noneMatch(rule::isEqualNode)) {
				// if it is not duplicated constraint
				this.getDocument().adoptNode(rule);
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
		Element feature = this.getDocument().createElement(FeatureModelNames.FEATURE.getName());
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
	 * For each {@code task} in {@code wfTasks},
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>retrieves a suitable {@code parentNode} using the
	 * {@link #getSuitableParent(Node)}
	 * method,</li>
	 * <li>inserts the current {@code task} under the {@code parentNode} using the
	 * {@link #insertNewTask(Node, Node)} method.
	 * </ul>
	 *
	 * @param wfTasks tasks to process
	 *
	 * @since 1.0
	 */
	protected void processTasks(List<Node> wfTasks) {
		String currentTaskName;
		String debugMsg;
		// retrieving all existing FM's tasks names
		// TODO: remove the following comment if #isDuplicated is kept :
		/*-
		 * List<Node> existingTasks = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		 * List<String> existingTasksNames = XMLManager.getNodesNames(existingTasks);
		 */
		// iterating for each task
		for (Node task : wfTasks) {
			// for each task
			for (Node nestedTask : this.getNestedNodes(task)) {
				// for each subtask
				currentTaskName = XMLManager.getNodeName(nestedTask);
				debugMsg = String.format("Processing task : %s", currentTaskName);
				logger.debug(debugMsg);
				if (this.isDuplicated(currentTaskName)) {
					logger.debug("Task already in FeatureModel");
					logger.debug("Skipping...");
					continue;
				}
				// retrieving a suitable parent
				Node parentNode = this.getSuitableParent(nestedTask);
				// inserting the new task
				this.insertNewTask(parentNode, nestedTask);
			}

		}
	}

	/**
	 * Returns a {@code List} of nested {@code Element} referred by the given
	 * {@code node}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that nested nodes are creating using the given {@code node}'s
	 * data.
	 *
	 * @param node node to extract nested ones
	 * @return a {@code List} of nested {@code Node} referred by the given
	 *         {@code node}
	 *
	 * @since 1.0
	 * @see Element
	 */
	protected List<Node> getNestedNodes(Node node) {
		List<Node> result = new ArrayList<>();
		// retrieving all nested nodes' names
		String[] nodeName = XMLManager.getNodeName(node).split(Notation.getGeneratedPrefixVoc());
		String lastReferred = "";
		Element nestedNode;
		for (String taskName : nodeName) {
			// for each nested subtasks to create
			// cloning source node
			nestedNode = (Element) node.cloneNode(false);
			this.getDocument().adoptNode(nestedNode);
			// updating nested node's name
			nestedNode.setAttribute(BPMNNodesAttributes.NAME.getName(), taskName);
			if (!lastReferred.isBlank()) {
				// TODO: change node to abstract ?
				// updating reference
				this.addDocumentationNode(nestedNode, lastReferred);
			} else {
				// not a nested node
				Node docNode = ((Element) node).getElementsByTagName(BPMNNodesNames.DOCUMENTATION.getName()).item(0);
				if (docNode == null) {
					String logMsg = String.format("Can't read documentation for node : %s.", taskName);
					logger.error(logMsg);
					logger.error("Skipping...");
					continue;
				}
				this.getDocument().adoptNode(docNode);
				nestedNode.appendChild(docNode);
			}
			result.add(nestedNode);
			lastReferred = taskName;
		}
		return result;
	}

}
