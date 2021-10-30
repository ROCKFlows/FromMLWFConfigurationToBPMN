package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.TaskTagsSelector;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.tasks.exceptions.InvalidTaskException;
import com.ml2wf.tasks.manager.TasksManager;

/**
 * This class is the base class for any XML managing class.
 *
 * <p>
 *
 * It also contains useful static methods for XML treatment.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@Log4j2
public abstract class XMLManager {

    /**
     * {@code Document} instance of the XML file.
     *
     * @see Document
     */
    private static Document document;
    /**
     * Documentation's counter.
     *
     * <p>
     *
     * This counter is used to number each documentation which is required for the
     * <a href="https://featureide.github.io/">FeatureIDE framework</a>.
     */
    private static int docCount;
    /**
     * Path to the XML file's directory.
     */
    @Getter @Setter private String path;
    /**
     * {@code File} instance of the XML file.
     *
     * @see File
     */
    @Getter @Setter private File sourceFile;

    /**
     * {@code XMLTool}'s default constructor.
     *
     * <p>
     *
     * <b>Note</b> that the {@link #normalizeDocument()} abstract method is called to initialize
     * {@link #document}.
     *
     * @param file the XML file
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    protected XMLManager(File file) throws ParserConfigurationException, SAXException, IOException {
        sourceFile = FileHandler.processFile(this, file);
        path = file.getAbsolutePath();
        XMLManager.updateDocument(sourceFile);
        normalizeDocument();
    }

    /**
     * Normalizes the document by applying the {@link Element#normalize()} method
     * and replacing all whitespaces by underscores.
     */
    protected abstract void normalizeDocument();

    /**
     * Returns the xml's {@code Document} instance
     *
     * @return the xml's {@code Document} instance
     *
     * @see Document
     */
    public static Document getDocument() {
        return XMLManager.document;
    }

    /**
     * Removes the xml's {@code Document} instance
     *
     * @see Document
     */
    public static void removeDocument() {
        XMLManager.document = null;
    }

    /**
     * Increments the {@code docCount} and returns its incremented value.
     *
     * @return its incremented value
     */
    protected static int incrementDocCount() {
        return ++docCount;
    }

    /**
     * Updates the xml's {@code Document} instance if the {@code document} is
     * {@code null} or the given {@code sourceFile} is different from the
     * {@code document}'s source file.
     *
     * @param sourceFile the {@code document}'s source file
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     *
     * @see Document
     */
    public static void updateDocument(File sourceFile) throws ParserConfigurationException, SAXException, IOException {
        if ((document == null)
                || ((document.getBaseURI() != null) && !document.getBaseURI().equals(sourceFile.toURI().toString()))) {
            document = FileHandler.preprocess(sourceFile);
            docCount = countDocumentation();
            TasksManager.clear();
        }
    }

    /**
     * Returns the number of <b>documentation ids</b> in the document.
     *
     * <p>
     *
     * This allow to define the {@link #docCount} initial value to avoid duplicated
     * id.
     *
     * @return the number of <b>documentation ids</b> in the document.
     */
    protected static int countDocumentation() {
        int count = 0;
        NodeList documentations = document.getElementsByTagName(BPMNNames.DOCUMENTATION.getName());
        Pattern pattern = RegexManager.getDigitPattern();
        for (int i = 0; i < documentations.getLength(); i++) {
            Node docNode = documentations.item(i);
            Node docIDNode = docNode.getAttributes().getNamedItem(BPMNAttributes.ID.getName());
            Matcher matcher = pattern.matcher(docIDNode.getNodeValue());
            if (matcher.find()) {
                int currentID = Integer.parseInt(matcher.group());
                count = Integer.max(count, currentID);
            }
        }
        return count;
    }

    /**
     * Saves the current {@code document} into the given {@code file} path.
     *
     * @param file the destination {@code File}
     *
     * @throws TransformerException
     * @throws IOException
     */
    public void save(File file) throws TransformerException, IOException {
        FileHandler.saveDocument(FileHandler.processFile(this, file), document);
    }

    /**
     * Saves the current {@code document} into the {@link #getSourceFile()} path.
     *
     * @throws TransformerException
     * @throws IOException
     */
    public void save() throws TransformerException, IOException {
        save(getSourceFile());
    }

    /**
     * Returns a formated documentation containing the reference declaration.
     *
     * @param content content containing the referred element
     *
     * @return a formatted documentation containing the reference declaration
     */
    public static String getReferenceDocumentation(String content) {
        return Notation.REFERENCE_VOC + XMLManager.sanitizeName(content);
    }

    /**
     * Adds the documentation part to the given {@code node}.
     *
     * <p>
     *
     * The documentation will contain informations about the task's ID, the referred
     * generic task and some attributes values.
     *
     * @param node Node to add the documentation
     *
     * @return the created documentation {@code Node}
     *
     * @see Node
     */
    public static Node addDocumentationNode(Node node) {
        Element documentation = node.getOwnerDocument().createElement(BPMNNames.DOCUMENTATION.getName());
        documentation.setAttribute(BPMNAttributes.ID.getName(), Notation.DOCUMENTATION_VOC + incrementDocCount());
        documentation.setIdAttribute(BPMNAttributes.ID.getName(), true);
        CDATASection refersTo = node.getOwnerDocument().createCDATASection("");
        documentation.appendChild(refersTo);
        log.debug("   Inserting node : {} before {}...", node, node.getFirstChild());
        return node.insertBefore(documentation, node.getFirstChild());
    }

    /**
     * Removes useless children of the given {@code node}.
     *
     * <p>
     *
     * <b>Note</b> that this notion is subjective and in this case, a useless
     * children has no attributes, no child and its text content is blank.
     *
     * @return the updated {@code Node}
     *
     * @see Node
     */
    public static Node cleanChildren(Node node) {
        NodeList children = node.getChildNodes();
        List<Node> toRemove = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (!child.hasAttributes() && !child.hasChildNodes() && child.getTextContent().isBlank()) {
                toRemove.add(child);
            }
        }
        toRemove.forEach(node::removeChild);
        return node;
    }

    /**
     * Creates the global annotation {@code Node}.
     *
     * @param wfDocument document to get the global annotation node
     *
     * @return the global annotation {@code Node}
     *
     * @see Node
     */
    public static Node createGlobalAnnotationNode(Document wfDocument) {
        // getting process node
        NodeList processNodeList = wfDocument.getElementsByTagName(BPMNNames.PROCESS.getName());
        Node processNode = processNodeList.item(0);
        // creating the annotation node
        Element annotationNode = wfDocument.createElement(BPMNNames.ANNOTATION.getName());
        // creating the id attribute
        annotationNode.setAttribute(BPMNAttributes.ID.getName(), Notation.GLOBAL_ANNOTATION_ID);
        annotationNode.setIdAttribute(BPMNAttributes.ID.getName(), true);
        // locating the annotation node
        createPositionalNode(wfDocument, Notation.GLOBAL_ANNOTATION_ID, 0., 0.);
        // adding to parent node
        return processNode.appendChild(annotationNode);
    }

    /**
     * Creates and returns a positional {@code Node}.
     *
     * <p>
     *
     * A "positional {@code Node}" is used to locate a BPMN element.
     *
     * <p>
     *
     * <b>Note</b> that all positional tags are considered as style tags contained
     * in the {@code BPMNNodesStyle enum}.
     *
     * @param wfDocument        document to add the position node
     * @param referredElement   the referred element's name
     * @param x                 the abscissa
     * @param y                 the ordinate
     *
     * @return the positional {@code Node}
     */
    public static Node createPositionalNode(Document wfDocument, String referredElement, double x, double y) {
        // TODO: improve this method (refactoring required)
        // TODO: refactor creating methods in enums
        log.debug("Creating positional Node...");
        Element shapeNode = wfDocument.createElement(BPMNNames.SHAPE.getName());
        // creating shape node's id attribute
        String idAttrName = Notation.BPMN_SHAPE_VOC + referredElement;
        Attr idAttr = wfDocument.createAttribute(BPMNAttributes.ID.getName());
        idAttr.setNodeValue(idAttrName);
        shapeNode.setAttributeNode(idAttr);
        // adding referred element attribute
        shapeNode.setAttribute(BPMNAttributes.ELEMENT.getName(), referredElement);
        // creating bounds child
        Element boundsNode = wfDocument.createElement(BPMNNames.BOUNDS.getName());
        // adding location attributes
        boundsNode.setAttribute(BPMNAttributes.HEIGHT.getName(), "90"); // TODO: store in constants
        boundsNode.setAttribute(BPMNAttributes.WIDTH.getName(), "180");
        boundsNode.setAttribute(BPMNAttributes.X.getName(), String.valueOf(x));
        boundsNode.setAttribute(BPMNAttributes.Y.getName(), String.valueOf(y));
        shapeNode.appendChild(boundsNode);
        // creating label child
        Element labelNode = wfDocument.createElement(BPMNNames.LABEL.getName());
        shapeNode.appendChild(labelNode);
        // TODO: create label id
        // creating bounds child
        boundsNode = wfDocument.createElement(BPMNNames.BOUNDS.getName());
        // adding location attributes
        boundsNode.setAttribute(BPMNAttributes.HEIGHT.getName(), "145");
        boundsNode.setAttribute(BPMNAttributes.WIDTH.getName(), "160");
        boundsNode.setAttribute(BPMNAttributes.X.getName(), String.valueOf(x + 5));
        boundsNode.setAttribute(BPMNAttributes.Y.getName(), String.valueOf(y));
        labelNode.appendChild(boundsNode);
        // selecting main diagram node
        NodeList planeNodeList = wfDocument.getElementsByTagName(BPMNNames.PLANE.getName());
        Node planeNode = planeNodeList.item(0);
        // appending new positional node
        planeNode.appendChild(shapeNode);
        return shapeNode;
    }

    /**
     * Returns a {@code List<String>} containing all documentations' content for the
     * given BPMN {@code element}.
     *
     * @param element   the element to extract the documentation content
     *
     * @return a {@code List<String>} containing all documentations' content for the
     *         given BPMN {@code element}
     *
     * @see Element
     */
    public static List<String> getAllBPMNDocContent(Element element) {
        return XMLManager.nodeListAsList(element.getElementsByTagName(BPMNNames.DOCUMENTATION.getName())).stream()
                .map(Node::getTextContent).collect(Collectors.toList());
    }

    /**
     * Returns the global annotation {@code Node}.
     *
     * <p>
     *
     * Creates the global annotation {@code Node} using the
     * {@link #createGlobalAnnotationNode(Document)} method if needed.
     *
     * @param wfDocument document to get the global annotation node
     *
     * @return the global annotation {@code Node}
     *
     * @see Node
     */
    public static Node getGlobalAnnotationNode(Document wfDocument) {
        log.debug("Getting global annotation node...");
        NodeList nodeList = wfDocument.getElementsByTagName(BPMNNames.ANNOTATION.getName());
        List<Node> annotationNodes = XMLManager.nodeListAsList(nodeList);
        for (Node annotation : annotationNodes) {
            NamedNodeMap attributes = annotation.getAttributes();
            if (attributes.getLength() > 0) {
                Node currentId = attributes.getNamedItem(BPMNAttributes.ID.getName());
                if ((currentId != null) && Notation.GLOBAL_ANNOTATION_ID.equals(currentId.getNodeValue())) {
                    return annotation;
                }
            }
        }
        log.warn("Global annotation node not found.");
        return createGlobalAnnotationNode(wfDocument);
    }

    /**
     * Returns the name tag's value of the given {@code node} if exists.
     *
     * Returns an empty string if not.
     *
     * @param node node containing the name attribute
     *
     * @return Returns the name tag's value of the given {@code node} if exists
     *
     * @see Node
     */
    public static String getNodeName(Node node) {
        log.trace("Retrieving name for node : {}...", node);
        if ((node == null) || !node.hasAttributes()) {
            return "";
        }
        Node n = node.getAttributes().getNamedItem(FMAttributes.NAME.getName());
        if (n != null) {
            log.trace("Node's name is : {}", n.getNodeValue());
            return n.getNodeValue();
        }
        return "";
    }

    /**
     * Returns the given {@code node}'s level.
     *
     * @param node node to get the level
     *
     * @return the given {@code node}'s level
     *
     * @see Node
     */
    public static int getNodeLevel(Node node) {
        int i = 1;
        Node parent;
        while (((parent = node.getParentNode()) != null) && !getNodeName(parent).isBlank()) {
            i++;
            node = parent;
        }
        return i;
    }

    /**
     * Returns an {@code Optional} containing the first feature node at the given
     * {@code level} in the given {@code document}.
     *
     * <p>
     *
     * <b>Note</b> that an {@code Optional#empty()} is returned if
     * {@code level <= 0}.
     *
     * @param document  the document to retrieve the node
     * @param level     the nested level
     *
     * @return an {@code Optional} containing the first feature node at the given
     *         {@code level} in the given {@code document}.
     *
     * @see Document
     */
    public static Optional<Node> getFeatureNodeAtLevel(Document document, int level) {
        if (level <= 0) {
            return Optional.empty();
        }
        return getTasksList(document, FMNames.SELECTOR).stream().filter(t -> XMLManager.getNodeLevel(t) == level)
                .findFirst();
    }

    /**
     * Returns an {@code Optional} containing the referred meta task from the given
     * {@code reference} text.
     *
     * @param reference reference containing the referred meta task
     *
     * @return an {@code Optional} containing the referred meta task from the given
     *         {@code reference} text
     */
    public static Optional<String> getReferredTask(String reference) {
        String regex = String.format("%s(\\w*)", Notation.REFERENCE_VOC);
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(reference);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }

    /**
     * Returns an {@code Optional} containing the first referred meta task from the
     * given {@code references}.
     *
     * <p>
     *
     * <b>Note</b> that this method calls the {@link #getReferredTask(String)}
     * method for each reference in {@code references} and returns the first non
     * empty result.
     *
     * @param references references containing the referred meta task
     *
     * @return an {@code Optional} containing the first referred meta task from the
     *         given {@code reference} text
     */
    public static Optional<String> getReferredTask(List<String> references) {
        Optional<String> result = Optional.empty();
        for (String reference : references) {
            result = getReferredTask(reference);
            if (result.isPresent()) {
                return result;
            }
        }
        return result;
    }

    /**
     * Returns all task nodes of the given {@code document}.
     *
     * @param document source document of task nodes extraction
     *
     * @return all task nodes of the given {@code document}
     *
     * @see Document
     * @see Node
     */
    public static List<Node> getTasksList(Document document, TaskTagsSelector selector) {
        log.debug("Retrieving tasks list...");
        List<Node> nodes = new ArrayList<>();
        for (String taskTag : selector.getTaskTags()) {
            nodes.addAll(XMLManager.nodeListAsList(document.getElementsByTagName(taskTag)));
        }
        return nodes;
    }

    /**
     * Parses the document's annotations and returns the workflow's name.
     *
     * <p>
     *
     * If it is not found, returns the document's name.
     *
     * @param wfDocument document containing the workflow's name
     *
     * @return the workflow's name
     */
    public static String getWorkflowName(Document wfDocument) {
        log.debug("Getting workflow's name...");
        Node annotation = XMLManager.getGlobalAnnotationNode(wfDocument);
        String regex = String.format("%s(.+)%s", Notation.getQuotedNotation(Notation.REFERENCES_DELIMITER_LEFT),
                Notation.getQuotedNotation(Notation.REFERENCES_DELIMITER_RIGHT));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        if (annotation != null) {
            String wfNameLine = annotation.getTextContent().split("\n")[0];
            matcher = pattern.matcher(wfNameLine);
            if (matcher.find() && (matcher.groupCount() > 0) && !matcher.group(1).isBlank()) {
                return matcher.group(1);
            }
        }
        log.debug("No workflow's name was found.");
        log.debug("Using file name as new workflow's name.");
        return new File(wfDocument.getDocumentURI()).getName().split("\\.")[0];
    }

    /**
     * Merges {@code nodeA}'s text content with the given {@code content}.
     *
     * @param nodeA   first node
     * @param content content to merge with
     *
     * @return true if the merge operation succeed, false if it failed.
     */
    public static boolean mergeNodesTextContent(Node nodeA, String content) {
        String contentA = nodeA.getTextContent().trim().replace("\\s+", " ");
        String contentB = content.trim().replace("\\s+", " ");
        contentA = contentA.replace(contentB, "");
        String result = contentA + "\n" + contentB;
        nodeA.setTextContent(result.trim());
        return true;
    }

    /**
     * Returns a {@code List} version of the {@code NodeList} parameter.
     *
     * @param nodes {@code NodeList} source
     *
     * @return a {@code List} version of the {@code NodeList} parameter
     *
     * @see List
     * @see NodeList
     */
    public static List<Node> nodeListAsList(NodeList nodes) {
        List<Node> lNodes = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            lNodes.add(nodes.item(i));
        }
        return lNodes;
    }

    /**
     * Sanitizes {@code name} removing genericity and instantiation caracteristics.
     *
     * @param name name to sanitize
     *
     * @return sanitized {@code name}
     */
    public static String sanitizeName(String name) {
        // sanitization for instantiate WF's task
        List<String> splitName = new ArrayList<>(Arrays.asList(name.split(Notation.GENERATED_PREFIX_VOC)));
        splitName.removeIf(String::isBlank);
        if (!splitName.isEmpty()) {
            name = splitName.get(0);
        }
        name = name.replaceFirst(Notation.DOCUMENTATION_VOC, "");
        name = name.replaceFirst(Notation.REFERENCE_VOC, "");
        name = name.replace(Notation.OPTIONALITY, "");
        name = name.trim();
        name = name.replace(" ", "_");
        Pattern validNamePattern = RegexManager.getValidFeatureNamePattern();
        if (!validNamePattern.matcher(name).find()) {
            throw new InvalidTaskException(
                    String.format("The task with name : [%s] is invalid. It must match the following regex : %s", name,
                            validNamePattern.pattern()));
        }
        return name;
    }
}
