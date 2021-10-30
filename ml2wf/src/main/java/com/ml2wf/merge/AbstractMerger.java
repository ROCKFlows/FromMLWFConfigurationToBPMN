package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
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
 * It aims at the application of <b>meta-learning</b> for workflow automation as
 * part of the <b>ml2wf project</b>.
 *
 * <p>
 *
 * Please refer to the <a href="https://featureide.github.io/">FeatureIDE
 * framework</a> for further information about a FeatureModel.
 *
 * @author Nicolas Lacroix
 *
 * @see XMLManager
 *
 * @since 1.0
 */
@Log4j2
@Getter
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
     * {@code AbstractMerger}'s default constructor.
     *
     * @param file the XML {@code File}.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    protected AbstractMerger(File file) throws ParserConfigurationException, SAXException, IOException {
        super(file);
        constraintFactory = new ConstraintFactoryImpl(getDocument());
        setTaskFactory(new TaskFactoryImpl());
    }

    /**
     * Sets the {@link TaskFactory} instance.
     *
     * @param taskFactory   the new {@link TaskFactory} instance
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
     * Adopts and adds new {@code rules} to the FM document's constraint tag.
     *
     * @param rules {@code List} of {@code Node} rules to add to the FM document
     *
     * @see Node
     */
    protected void adoptRules(List<Node> rules) {
        Node constraintsNode = createConstraintTag();
        // getting constraints
        List<Node> constraints = new ArrayList<>(
                nodeListAsList(getDocument().getElementsByTagName(FMNames.RULE.getName())));
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
     * @see Node
     */
    protected static Node createConstraintTag() {
        NodeList nodeList = getDocument().getElementsByTagName(FMNames.CONSTRAINTS.getName());
        if (nodeList.getLength() > 0) {
            // already exists
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
     *
     * @return a new feature attribute {@code Element} with the given {@code name}
     *
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
     *
     * @return a new feature attribute {@code Element} with the given {@code name}
     *
     * @see Element
     */
    public static Element createFeatureAttribute(String name, Object value) {
        // TODO: factorize with createFeatureNode
        Element feature = getDocument().createElement(FMNames.ATTRIBUTE.getName());
        feature.setAttribute(FMAttributes.NAME.getName(), name);
        feature.setAttribute(FMAttributes.TYPE.getName(), value.getClass().getSimpleName().toLowerCase(Locale.US));
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
     *
     * @return a new feature {@code Element} with the given {@code name}
     *
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
     *
     * @return a new feature ({@code FMTask}) with the given {@code name}
     *
     * @throws UnresolvedConflict
     *
     * @see FMTask
     */
    protected static FMTask createFMTaskWithName(String name, boolean isAbstract) throws UnresolvedConflict {
        log.debug("AbstractMerger createFMTaskWithName {} ==> isAbstract : {}", name, isAbstract);
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
     *
     * @return the nested {@code Element} according to the {@code parent} data
     */
    protected static Element createNestedNode(Element parent, String name) {
        Element created = parent.getOwnerDocument().createElement(parent.getNodeName());
        created.setAttribute(BPMNAttributes.NAME.getName(), name);
        String refContent = getReferenceDocumentation(getNodeName(parent));
        Optional<Node> optProperty = getProperty(parent, BPMNNames.PROPERTY.getName());
        optProperty.ifPresent(node -> created.appendChild(node.cloneNode(true)));
        mergeNodesTextContent(addDocumentationNode(created), refContent);
        return created;
    }

    /**
     * Returns an {@code Optional} that contains the given {@code element}'s
     * property with name {@code propertyName}.
     *
     * @param element      element to retrieve the property
     * @param propertyName the property name
     *
     * @return an {@code Optional} that contains the given {@code element}'s
     *         property with name {@code propertyName}
     */
    public static Optional<Node> getProperty(Element element, String propertyName) {
        NodeList properties = element.getElementsByTagName(propertyName);
        return (properties.getLength() > 0) ? Optional.of(properties.item(0)) : Optional.empty();
    }

    /**
     * Creates the <b>tagName</b> {@code Node} and append it to the given
     * {@code parent} if it doesn't exist.
     *
     * @param parentTask    {@code parent} of {@code tagName Node}
     * @param tagName       name of tag to be created
     *
     * @return the created tag
     *
     * @see Node
     */
    protected Node createTag(FMTask parentTask, FMNames tagName) {
        Element parentElement = (Element) parentTask.getNode();
        NodeList nodeList = parentElement.getElementsByTagName(tagName.getName());
        if (nodeList.getLength() > 0) {
            // already exists
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
     *
     * @return a {@code List} of nested {@code Node} referred by the given
     *         {@code node}
     */
    public static List<Node> getNestedNodes(Node node) {
        // retrieving attributes values
        String rawName = getNodeName(node);
        String attributesDoc = getAttributesDoc(rawName);
        // retrieving all nested nodes' names
        String[] nodeName = rawName.split(Notation.GENERATED_PREFIX_VOC);
        List<String> names = new ArrayList<>(Arrays.asList(nodeName));
        // sanitizing names
        names = names.stream().filter(n -> !n.isBlank()).map(XMLManager::sanitizeName).collect(Collectors.toList());
        if (names.size() == 1) {
            // if there is no nested node
            // add the attributesDoc to the docNode text content
            addAttributeDoc((Element) node, attributesDoc);
            // return the current node as a list
            return Collections.singletonList(node);
        }

        // Manage the parentNode
        Element parentNode = (Element) node.cloneNode(true);
        String parentName = names.remove(0);
        parentNode.setAttribute(BPMNAttributes.NAME.getName(), parentName);
        // add the attributesDoc to the docNode text content
        addAttributeDoc(parentNode, attributesDoc);
        List<Node> result = new ArrayList<>();
        result.add(parentNode);

        // foreach nested node's name
        for (String name : names) {
            parentNode = createNestedNode(parentNode, name);
            // add the attributesDoc to the docNode text content
            addAttributeDoc(parentNode, attributesDoc);
            result.add(parentNode);
        }

        return result;
    }

    /**
     * Returns a documentation containing all BPMN feature attributes values (e.g.
     * optionality, category, ...) for the given {@code rawName}.
     *
     * @param rawName raw name containing data about feature attributes values
     *
     * @return a documentation containing all feature attributes values for the
     *         given {@code rawName}
     *
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
            mergeNodesTextContent(docNode, attributesDoc);
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
     *
     * @return the given {@code task}'s name
     */
    protected String getTaskName(Node task) {
        log.debug("Retrieving name for task : {}...", task);
        String taskName = getNodeName(task);
        String[] split = taskName.split(Notation.GENERATED_PREFIX_VOC);
        taskName = (split.length > 0) ? split[split.length - 1] : taskName;
        log.debug("Task's name is : {}", taskName);
        return taskName;
    }

    /**
     * Returns a {@code List} containing all {@code nodes}' tasks names.
     *
     * @param taskNodes nodes to get the names
     *
     * @return a {@code List} containing all {@code nodes}' tasks names
     *
     * @see NodeList
     */
    protected List<String> getTasksNames(List<Node> taskNodes) {
        return taskNodes.stream().map(this::getTaskName)
                .collect(Collectors.toList());
    }

    /**
     * Returns the workflow's document and name with the given {@code filePath}
     * using the {@link Pair} class. If the {@code filePath} is not a valid path or
     * the workflow's name is already in the feature model, return an empty
     * {@code Pair}.
     *
     * @param file {@code File} instance of the workflow
     *
     * @return a {@code Pair} association between the workflow's name and its
     *         {@code Document} instance or an empty {@code Pair} if something is
     *         wrong.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     *
     * @see Pair
     * @see Document
     */
    protected Pair<String, Document> getWFDocInfoFromFile(File file)
            throws ParserConfigurationException, SAXException, IOException {
        String wfTaskName;
        Document wfDocument;
        if (file.exists()) {
            wfDocument = FileHandler.preprocess(file);
            wfTaskName = getWorkflowName(wfDocument).replace(" ", "_");
            log.debug("WF's name is {}.", wfTaskName);
            if (TasksManager.existsInFM(wfTaskName)) {
                log.warn("This workflow is already in the FeatureModel");
                log.warn("Skipping...");
                return new Pair<>();
            }
        } else {
            log.fatal("Invalid filepath : {}", file.getAbsolutePath());
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
     *
     * @return the added child
     *
     * @throws UnresolvedConflict
     *
     * @see Task
     * @see FMTask
     */
    public static <T extends Task<?>> FMTask insertNewTask(FMTask parentTask, T task) throws UnresolvedConflict {
        log.debug("Inserting task : {}", task.getName());
        //FIX
        log.debug("******* AbstractMerger : Inserting task : {} with parent {}", task.getName(),parentTask) ;
        // inserting the new node
        FMTask childTask = (task instanceof FMTask) ? (FMTask) task : taskFactory.convertWFtoFMTask((WFTask<?>) task);
        return parentTask.appendChild(childTask);
    }


    /**
     * Returns a {@code List<Node>} containing all annotations of the
     * {@code wfDocument}.
     *
     * @param wfDocument {@code Document} containing annotations
     *
     * @return a {@code List<Node>} containing all annotations of the
     *         {@code wfDocument}
     *
     * @see Document
     */
    protected List<Node> getAnnotations(Document wfDocument) {
        return nodeListAsList(wfDocument.getElementsByTagName(BPMNNames.ANNOTATION.getName()));
    }

    /**
     * Processes the given annotation nodes and adds constraints.
     *
     * @param annotations   the annotation nodes to process
     *
     * @throws InvalidConstraintException
     * @throws UnresolvedConflict
     *
     * @see ConstraintFactory
     */
    protected void processAnnotations(List<Node> annotations) throws InvalidConstraintException, UnresolvedConflict {
        log.info("Processing annotations...");
        // pair that will contain order constraint data
        List<Pair<FMTask, Node>> orderPairs;
        for (Node annotation : annotations) {
            // TODO: improve performances (check annotation.getChildNodes().item(1)
            // sufficient ?)
            for (Node commentNode : nodeListAsList(annotation.getChildNodes())) {
                String comment = commentNode.getTextContent();
                orderPairs = constraintFactory.getOrderNodes(comment);
                if (!orderPairs.isEmpty()) {
                    // this is an order constraint
                    processOrderConstraint(orderPairs);
                } else {
                    // this is a "classic" constraint
                    List<Node> newRules = constraintFactory.getRuleNodes(comment);
                    adoptRules(newRules);
                }
            }
        }
        log.info("Annotations processing ended...");
    }

    /**
     * Processes the association constraints.
     *
     * <p>
     *
     * An association constraint is an implication between the {@code wfName} and
     * the {@code Set<BPMNTask>}.
     *
     * @param wfName    workflow's name
     * @param tasks     {@code Set<Task>} containing all workflow's tasks
     *
     * @throws InvalidConstraintException
     * @throws UnresolvedConflict
     */
    protected void processAssocConstraints(String wfName, List<WFTask<?>> tasks)
            throws InvalidConstraintException, UnresolvedConflict {
        log.debug("Filtering mandatory tasks...");
        // filtering mandatory tasks
        List<String> mandatoriesNames = tasks.stream()
                .filter(t -> !isOptional(t)).map(WFTask::getName)
                .collect(Collectors.toList());
        if (mandatoriesNames.isEmpty()) {
            log.warn("All {}'s tasks are optional. Skipping...", wfName);
            return;
        }
        log.debug("Getting the constraint association...");
        String associationConstraint = constraintFactory.getAssociationConstraint(wfName, mandatoriesNames);
        log.info("Generated constraint association : {}", associationConstraint);
        // add the new constraint
        log.info("Adding association constraint...");
        adoptRules(constraintFactory.getRuleNodes(associationConstraint));
    }

    /**
     * Returns whether the given task is optional or not.
     *
     * @param task task to be tested
     *
     * @return whether the given task is optional or not
     *
     * @see BPMNTaskSpecs
     */
    private static boolean isOptional(WFTask<?> task) {
        // TODO: change BPMN by user's used convention
        // TODO: change or factorize with another method ?
        Optional<String> opt = BPMNTaskSpecs.OPTIONAL.getSpecValue(task);
        return opt.isPresent() && Boolean.parseBoolean(opt.get());
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
     * @see FMTask
     * @see Pair
     */
    protected void processOrderConstraint(List<Pair<FMTask, Node>> orderPairs) {
        FMTask key;
        for (Pair<FMTask, Node> pair : orderPairs) {
            key = pair.getKey();
            if (key != null) {
                // the order association has been parsed
                Node docNode = createTag(key, FMNames.DESCRIPTION);
                if (!mergeNodesTextContent(docNode, pair.getValue().getTextContent())) {
                    // if there is a merge failure for description nodes
                    log.error("The merge operation for description nodes failed.");
                }
            } else {
                // the operation failed
                log.error("Can't merge the order constraint : {}", pair.getValue().getTextContent());
                log.warn("Maybe one of the involved task is not in the current workflow ?");
            }
        }
    }

}
