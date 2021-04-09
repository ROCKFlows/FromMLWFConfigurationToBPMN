package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conflicts.exceptions.UnresolvedConflict;
import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactory;
import com.ml2wf.constraints.factory.ConstraintFactoryImpl;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.tasks.base.Task;
import com.ml2wf.tasks.base.WFTask;
import com.ml2wf.tasks.concretes.FMTask;
import com.ml2wf.tasks.factory.TaskFactory;
import com.ml2wf.tasks.factory.TaskFactoryImpl;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.tasks.specs.BPMNTaskSpecs;
import com.ml2wf.util.FileHandler;
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
	 * {@code TaskFactory}'s instance that will generate {@code Task} instances.
	 *
	 * @see TaskFactory
	 */
	protected static TaskFactory taskFactory;
	/**
	 * {@code ConstraintFactory}'s instance that will generate constraint nodes.
	 *
	 * @see ConstraintFactory
	 */
	protected ConstraintFactory constraintFactory;
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
		setTaskFactory(new TaskFactoryImpl());
	}

	/**
	 * Returns the {@code TaskFactory}'s instance.
	 *
	 * @return the {@code TaskFactory}'s instance
	 *
	 * @see TaskFactory
	 */
	private static void setTaskFactory(TaskFactory taskFactory) {
		AbstractMerger.taskFactory = taskFactory;
	}

	/**
	 * Returns the {@code TaskFactory}'s instance.
	 *
	 * @return the {@code TaskFactory}'s instance
	 *
	 * @see TaskFactory
	 */
	public static TaskFactory getTaskFactory() {
		return taskFactory;
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
	 * Adopts and adds new {@code rules} to the FM document's constraint tag.
	 *
	 * @param rules {@code List} of {@code Node} rules to add to the FM document
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	protected void adoptRules(List<Node> rules) {
		Node constraintsNode = createConstraintTag();
		// getting constraints
		List<Node> constraints = new ArrayList<>(
				XMLManager.nodeListAsList(getDocument().getElementsByTagName(FMNames.RULE.getName())));
		for (Node rule : rules) {
			if (constraints.stream().noneMatch(rule::isEqualNode)) {
				// if it is not duplicated constraint
				getDocument().adoptNode(rule);
				constraintsNode.appendChild(rule);
			}
		}
	}

	/**
	 * Creates the <b>constraints</b> {@code Node} if it doesn't exist.
	 *
	 * @return the constraints {@code Node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected static Node createConstraintTag() {
		NodeList nodeList = getDocument().getElementsByTagName(FMNames.CONSTRAINTS.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		} else {
			Node consTag = getDocument().createElement(FMNames.CONSTRAINTS.getName());
			NodeList fmTagList = getDocument().getElementsByTagName(FMNames.FEATURE_MODEL.getName());
			if (fmTagList.getLength() == 0) {
				// if it is an ExtendedFeatureModel
				fmTagList = getDocument().getElementsByTagName(FMNames.EXTENDED_FEATURE_MODEL.getName());
			}
			Node rootNode = fmTagList.item(0);
			return rootNode.appendChild(consTag);
		}
	}

	/**
	 * Creates and returns a new feature with the given {@code name}.
	 *
	 * @param name name of the feature attribute
	 * @return a new feature attribute {@code Element} with the given {@code name}
	 *
	 * @since 1.0
	 * @see Element
	 */
	public static Element createFeature(String name) {
		Element feature = getDocument().createElement(FMNames.FEATURE.getName());
		feature.setAttribute(FMAttributes.NAME.getName(), name);
		return feature;
	}

	/**
	 * Creates and returns a new feature attribute {@code Element} with the given
	 * {@code name}.
	 *
	 * @param name  name of the feature attribute
	 * @param value value of the feature attribute
	 * @return a new feature attribute {@code Element} with the given {@code name}
	 *
	 * @since 1.0
	 * @see Element
	 */
	public static Element createFeatureAttribute(String name, Object value) {
		// TODO: factorize with createFeatureNode
		Element feature = getDocument().createElement(FMNames.ATTRIBUTE.getName());
		feature.setAttribute(FMAttributes.NAME.getName(), name);
		feature.setAttribute(FMAttributes.TYPE.getName(), value.getClass().getSimpleName().toLowerCase());
		feature.setAttribute(FMAttributes.VALUE.getName(), String.valueOf(value));
		return feature;
	}

	/**
	 * Creates and returns a new feature {@code Element} with the given
	 * {@code name} and the given {@code isAbstract} status.
	 *
	 * @param name       name of the feature
	 * @param isAbstract whether the wished created feature {@code Element} must be
	 *                   abstract or not
	 * @return a new feature {@code Element} with the given {@code name}
	 *
	 * @since 1.0
	 * @see Element
	 */
	public static Element createFeatureWithAbstract(String name, boolean isAbstract) {
		Element feature = createFeature(name);
		feature.setAttribute(FMAttributes.ABSTRACT.getName(), String.valueOf(isAbstract));
		return feature;
	}

	/**
	 * Creates and returns a new feature ({@code FMTask}) with the given
	 * {@code name}.
	 *
	 * @param name       name of the feature
	 * @param isAbstract whether the wished created feature must be abstract or not
	 * @return a new feature ({@code FMTask}) with the given {@code name}
	 *
	 * @throws UnresolvedConflict
	 *
	 * @see FMTask
	 */
	protected static FMTask createFMTaskWithName(String name, boolean isAbstract) throws UnresolvedConflict {
		logger.debug("AbstractMerger createFMTaskWithName %s ==> isAbstract : %s", name, isAbstract);
		return taskFactory.createTask(createFeatureWithAbstract(name, isAbstract));
	}

	/**
	 * Creates and returns the nested {@code Element} according to the
	 * {@code parent} data.
	 *
	 * <p>
	 *
	 * <b>Note</b> that the created {@code Element} is not added to the
	 * {@code parent}'s children.
	 *
	 * @param parent parent of the created nested node
	 * @param name   name of the nested node
	 * @return the nested {@code Element} according to the {@code parent} data
	 *
	 * @since 1.0
	 */
	protected static Element createNestedNode(Element parent, String name) {
		Element created = parent.getOwnerDocument().createElement(parent.getNodeName());
		created.setAttribute(BPMNAttributes.NAME.getName(), name);
		String refContent = getReferenceDocumentation(XMLManager.getNodeName(parent));
		Optional<Node> optProperty = getProperty(parent, BPMNNames.PROPERTY.getName());
		if (optProperty.isPresent()) {
			created.appendChild(optProperty.get().cloneNode(true));
		}
		mergeNodesTextContent(addDocumentationNode(created), refContent);
		return created;
	}

	/**
	 * Returns an {@code Optional} that contains the given {@code element}'s
	 * property with name {@code propertyName}.
	 *
	 * @param element      element to retrieve the property
	 * @param propertyName the property name
	 * @return an {@code Optional} that contains the given {@code element}'s
	 *         property with name {@code propertyName}
	 *
	 * @since 1.0
	 */
	public static Optional<Node> getProperty(Element element, String propertyName) {
		NodeList properties = element.getElementsByTagName(propertyName);
		return (properties.getLength() > 0) ? Optional.of(properties.item(0)) : Optional.empty();
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
	protected Node createTag(FMTask parentTask, FMNames tagName) {
		Element parentElement = (Element) parentTask.getNode();
		NodeList nodeList = parentElement.getElementsByTagName(tagName.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		}
		Node newTag = getDocument().createElement(tagName.getName());
		return parentElement.appendChild(newTag);
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
	public static List<Node> getNestedNodes(Node node) {

		List<Node> result = new ArrayList<>();
		// retrieving attributes values
		String rawName = XMLManager.getNodeName(node);
		String attributesDoc = getAttributesDoc(rawName);
		// retrieving all nested nodes' names
		String[] nodeName = rawName.split(Notation.GENERATED_PREFIX_VOC);
		List<String> names = new ArrayList<>(Arrays.asList(nodeName));
		// sanitizing names
		names = names.stream().filter(n -> !n.isBlank()).map(XMLManager::sanitizeName).collect(Collectors.toList());
		if (names.size() == 1) {
			// if there is no nested node
			addAttributeDoc((Element) node, attributesDoc); // add the attributesDoc to the docNode text content
			return Arrays.asList(node); // return the current node as a list
		}
		
		// Manage the parentNode
		Element parentNode = (Element) node.cloneNode(true);
		String parentName = names.remove(0);
		parentNode.setAttribute(BPMNAttributes.NAME.getName(), parentName);
		addAttributeDoc(parentNode, attributesDoc); // add the attributesDoc to the docNode text content
		result.add(parentNode);
		
		// foreach nested node's name
		for (String name : names) {
			parentNode = createNestedNode(parentNode, name);
			addAttributeDoc(parentNode, attributesDoc); // add the attributesDoc to the docNode text content
			result.add(parentNode);
		}

		return result;
	}

	/**
	 * Returns a documentation containing all BPMN feature attributes values (e.g.
	 * optionality, category, ...) for the given {@code rawName}.
	 *
	 * @param rawName raw name containing data about feature attributes values
	 * @return a documentation containing all feature attributes values for the
	 *         given {@code rawName}
	 *
	 * @since 1.0
	 * @see BPMNTaskSpecs
	 */
	private static String getAttributesDoc(String rawName) {
		StringBuilder attributesDoc = new StringBuilder();
		for (BPMNTaskSpecs spec : BPMNTaskSpecs.values()) {
			attributesDoc.append(spec.formatSpec(rawName));
		}
		return attributesDoc.toString();
	}

	/**
	 * Adds the given {@code attributesDoc} to the given {@code element}'s
	 * documentation {@code Node}.
	 *
	 * @param element       element to add the given {@code attributesDoc}
	 * @param attributesDoc documentation containing some attributes values.
	 *
	 * @since 1.0
	 * @see Node
	 */
	private static void addAttributeDoc(Element element, String attributesDoc) {
		if (attributesDoc.isBlank()) {
			return;
		}
		NodeList docNodes = element.getElementsByTagName(BPMNNames.DOCUMENTATION.getName());
		if (docNodes.getLength() == 0) {
			mergeNodesTextContent(addDocumentationNode(element), attributesDoc);
		} else {
			Node docNode = docNodes.item(0);
			XMLManager.mergeNodesTextContent(docNode, attributesDoc);
		}
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
		String[] splitted = taskName.split(Notation.GENERATED_PREFIX_VOC);
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
	protected Pair<String, Document> getWFDocInfoFromFile(File file)
			throws ParserConfigurationException, SAXException, IOException {
		String wfTaskName;
		String logMsg;
		Document wfDocument;
		if (file.exists()) {
			wfDocument = FileHandler.preprocess(file);
			wfTaskName = getWorkflowName(wfDocument).replace(" ", "_");
			logMsg = String.format("WF's name is %s.", wfTaskName);
			logger.debug(logMsg);
			if (TasksManager.existsinFM(wfTaskName)) {
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
	 * Inserts and returns the new task corresponding of the given {@code task}
	 * under the given {@code parentTask}.
	 *
	 * <p>
	 *
	 * The result task matchs the FeatureModel format.
	 *
	 * @param <T>        Any {@code class} extending the {@code Task class}
	 * @param parentTask Parent task
	 * @param task       task to insert
	 * @return the added child
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see Task
	 * @see FMTask
	 */
	public static <T extends Task<?>> FMTask insertNewTask(FMTask parentTask, T task) throws UnresolvedConflict {
		logger.debug("Inserting task : {}", task.getName());
		//FIX
		logger.debug("******* AbstractMerger : Inserting task :%s with parent %s", task.getName(),parentTask) ;
		// inserting the new node
		FMTask childTask = (task instanceof FMTask) ? (FMTask) task
				: taskFactory.convertWFtoFMTask((WFTask<?>) task);
		return parentTask.appendChild(childTask);
	}

	
	/**
	 * Returns a {@code List<Node>} containing all annotations of the
	 * {@code wfDocument}.
	 *
	 * @param wfDocument {@code Document} containing annotations
	 * @return a {@code List<Node>} containing all annotations of the
	 *         {@code wfDocument}
	 *
	 * @since 1.0
	 * @see Document
	 */
	protected List<Node> getAnnotations(Document wfDocument) {
		return XMLManager
				.nodeListAsList(wfDocument.getElementsByTagName(BPMNNames.ANNOTATION.getName()));
	}

	/**
	 * Processes {@code document}'s annotations and adds constraints.
	 *
	 * @param wfDocument Workflow {@code Document}'s instance containing
	 *                   annotations.
	 * @throws InvalidConstraintException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 * @see ConstraintFactory
	 */
	protected void processAnnotations(List<Node> annotations)
			throws InvalidConstraintException, UnresolvedConflict {
		logger.info("Processing annotations...");
		List<Pair<FMTask, Node>> orderPairs; // pair that will contain order constraint data
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
	 * Processes the association constraints.
	 *
	 * <p>
	 *
	 * An association constraint is an implication between the {@code wfName} and
	 * the {@code Set<BPMNTask>}.
	 *
	 * @param <T>    Any {@code class} extending the {@code Task class}
	 * @param wfName workflow's name
	 * @param tasks  {@code Set<Task>} containing all workflow's tasks
	 * @throws InvalidConstraintException
	 * @throws UnresolvedConflict
	 *
	 * @since 1.0
	 */
	protected void processAssocConstraints(String wfName, List<WFTask<?>> tasks)
			throws InvalidConstraintException, UnresolvedConflict {
		String logMsg;
		logger.debug("Filtering mandatories tasks...");
		// filtering mandatories tasks
		List<String> mandatoriesNames = tasks.stream()
				.filter(t -> !isOptional(t)).map(WFTask::getName)
				.collect(Collectors.toList());
		if (mandatoriesNames.isEmpty()) {
			logger.warn("All {}'s tasks are optional. Skipping...", wfName);
			return;
		}
		logger.debug("Getting the constraint association...");
		String associationConstraint = ((ConstraintFactoryImpl) this.getConstraintFactory())
				.getAssociationConstraint(wfName, mandatoriesNames);
		logMsg = String.format("Generated constraint association : %s", associationConstraint);
		logger.info(logMsg);
		// add the new constraint
		logger.info("Adding association constraint...");
		this.adoptRules(this.getConstraintFactory().getRuleNodes(associationConstraint));
	}

	/**
	 * Returns whether the given task is optional or not.
	 *
	 * @param task task to be tested
	 * @return whether the given task is optional or not
	 *
	 * @since 1.0
	 * @see BPMNTaskSpecs
	 */
	private static boolean isOptional(WFTask<?> task) {
		// TODO: change BPMN by user's used convention
		// TODO: change or factorize with another method ?
		Optional<String> opt = BPMNTaskSpecs.OPTIONAL.getSpecValue(task);
		if (opt.isEmpty()) {
			return false;
		}
		return Boolean.valueOf(opt.get());
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
	 *                   {@code FMTask} as key and the descriptive {@code Node} as
	 *                   value
	 *
	 * @since 1.0
	 *
	 * @see FMTask
	 * @see Pair
	 */
	protected void processOrderConstraint(List<Pair<FMTask, Node>> orderPairs) {
		for (Pair<FMTask, Node> pair : orderPairs) {
			FMTask key;
			if ((key = pair.getKey()) != null) {
				// the order association has been parsed
				Node docNode = this.createTag(key, FMNames.DESCRIPTION);
				if (!XMLManager.mergeNodesTextContent(docNode, pair.getValue().getTextContent())) {
					// if there is a merge failure for description nodes
					logger.error("The merge operation for description nodes failed.");
				}
			} else {
				// the operation failed
				logger.error("Can't merge the order constraint : {}", pair.getValue().getTextContent());
				logger.warn("Maybe one of the involved task is not in the current workflow ?");
			}
		}
	}

}
