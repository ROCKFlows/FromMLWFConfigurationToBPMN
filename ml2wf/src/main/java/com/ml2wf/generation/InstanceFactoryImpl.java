package com.ml2wf.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.tasks.specs.BPMNTaskSpecs;
import com.ml2wf.util.RegexManager;
import com.ml2wf.util.XMLManager;

/**
 * This class is a factory for the instantiation of abstract workflows.
 *
 * <p>
 *
 * It is an extension of the {@link XMLManager} base class.
 *
 * <p>
 *
 * It is an implementation of the {@link InstanceFactory} interface.
 *
 * @author Nicolas Lacroix
 *
 * @since 1.0.0
 */
@Log4j2
public class InstanceFactoryImpl extends XMLManager implements InstanceFactory {

    /**
     * Hexadecimal color code of instantiated mandatory elements.
     */
    public static final String MANDATORY_COLOR = "#00f900";
    /**
     * Hexadecimal color code of instantiated optional elements.
     */
    public static final String OPTIONAL_COLOR = "#835C3B";
    /**
     * This {@code taskCounter} is incremented for each task. This allows other
     * methods to give an unique name to a given task.
     */
    private int taskCounter;
    /**
     * This {@code propertyCounter} is incremented for each property added in the
     * current workflow. This allows other methods to give a unique ID to a given
     * property.
     */
    private int propertyCounter;

    /**
     * {@code InstanceFactory}'s default constructor.
     *
     * @param file the XML file.
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public InstanceFactoryImpl(File file) throws ParserConfigurationException, SAXException, IOException {
        super(file);
    }

    @Override
    protected void normalizeDocument() {
        getDocument().getDocumentElement().normalize();
        List<Node> taskNodes = XMLManager.getTasksList(getDocument(), BPMNNames.SELECTOR);
        taskNodes.stream().map(t -> t.getAttributes().getNamedItem(BPMNAttributes.NAME.getName()))
                .filter(Objects::nonNull)
                .forEach(t -> t.setNodeValue(t.getNodeValue().trim().replace(" ", "_")));
    }

    /**
     * Instantiates the given workflow and saves it using the {@link #save()}
     * method.
     *
     * <p>
     *
     * More precisely,
     *
     * <ul>
     * <li>iterates over all XML file's nodes,</li>
     * <li>replaces each <b>task</b> node by an instantiated version,</li>
     * <li>saves the modifications using the {@link #save()} method.</li>
     * </ul>
     *
     * <p>
     *
     * The result filename will be <b>FileBaseName</b> +
     * <b>{@link Notation#INSTANCE_VOC}</b> + <b>.FileExtension</b>.
     *
     * @see Node
     */
    @Override
    public void getWFInstance(File outputFile) {
        log.info("Starting the Workflow instantiation...");
        addMetaWFReferences();
        for (Node node : XMLManager.getTasksList(getDocument(), BPMNNames.SELECTOR)) {
            log.debug("Instantiating the node {}...", node);
            instantiateNode(node);
        }
        log.info("Instantiation finished.");
    }

    /**
     * Calls the {@link #getWFInstance(File)} method with
     * {@link #getSourceFile()} as parameter.
     *
     * @see #getWFInstance(File)
     */
    public void getWFInstance() {
        getWFInstance(getSourceFile());
    }

    /**
     * Returns the last reference from the given {@code content}.
     *
     * <p>
     *
     * e.g. A#B#C -> C
     *
     * @param content content to retrieve the last reference
     *
     * @return the last reference from the given {@code content}
     */
    public static String getLastReference(String content) {
        // splitting references
        String[] splitContent = content.split(Notation.GENERATED_PREFIX_VOC);
        // getting the last reference
        return sanitizeName(splitContent[splitContent.length - 1]);
    }

    /**
     * Instantiates the given {@code node}.
     *
     * <p>
     *
     * More precisely,
     *
     * <ul>
     * <li>adds the documentation node using the {@link #addDocumentationNode(Node)}
     * method,</li>
     * <li>adds the extension node using the {@link #addExtensionNode(Node)}
     * method,</li>
     * <li>renames the current node</li>
     * </ul>
     *
     * @param node Node to instantiate.
     *
     * @see Node
     */
    private void instantiateNode(Node node) {
        // retrieving node name
        String content = node.getAttributes().getNamedItem(BPMNAttributes.NAME.getName()).getNodeValue();
        // getting the node's reference
        String currentRef = getLastReference(content);
        // adding a documentation node for the current node
        Node docNode = addDocumentationNode(node);
        mergeNodesTextContent(docNode, BPMNTaskSpecs.OPTIONAL.formatSpec(content));
        mergeNodesTextContent(docNode, getReferenceDocumentation(currentRef));
        // property/extension part
        addProperty(node, Notation.BPMN_PROPERTY_INSTANCE);
        addExtensionNode(node);
        // node renaming part
        Node nodeAttrName = node.getAttributes().getNamedItem(BPMNAttributes.NAME.getName());
        String nodeName = XMLManager.sanitizeName(currentRef) + "_" + taskCounter++;
        nodeAttrName.setNodeValue(nodeName);
    }

    /**
     * Adds the given {@code propertyValue} to the given {@code node}.
     *
     * @param node node to add the property
     */
    private void addProperty(Node node, String propertyValue) {
        Element propertyAttr = getDocument().createElement(BPMNNames.PROPERTY.getName());
        String propertyId = Notation.BPMN_PROPERTY_PREFIX + propertyCounter++;
        propertyAttr.setAttribute(BPMNAttributes.ID.getName(), propertyId);
        propertyAttr.setIdAttribute(BPMNAttributes.ID.getName(), true);
        propertyAttr.setAttribute(BPMNAttributes.NAME.getName(), propertyValue);
        node.appendChild(propertyAttr);
    }

    /**
     * Adds the metaworkflow references in the document.
     *
     * <p>
     *
     * More precisely,
     *
     * <p>
     *
     * <ul>
     * <li>add the reference in a documentation tag with the
     * {@link #addMetaWFRefDoc(String)} method,</li>
     * <li>add the reference in an annotation with the
     * {@link #addWFRefAnnotation(String)} method,</li>
     * </ul>
     *
     * @see #addMetaWFRefDoc(String)
     * @see #addWFRefAnnotation(String)
     */
    private void addMetaWFReferences() {
        log.debug("Adding the meta reference...");
        String metaReference = XMLManager.getWorkflowName(getDocument()).replace(" ", "_");
        addMetaWFRefDoc(metaReference);
        addWFRefAnnotation(metaReference);
    }

    /**
     * Adds the meta-workflow reference in a documentation tag under the process one.
     *
     * @param referred referred meta task
     */
    private void addMetaWFRefDoc(String referred) {
        log.debug("Adding meta reference in the documentation...");
        NodeList processNodeList = getDocument().getElementsByTagName(BPMNNames.PROCESS.getName());
        if (processNodeList.getLength() == 0) {
            log.error("Error while getting the referred meta-workflow's name.");
            log.warn("Skipping this step...");
            return;
        }
        Node processNode = processNodeList.item(0);
        mergeNodesTextContent(addDocumentationNode(processNode), getReferenceDocumentation(referred));
    }

    /**
     * Adds the workflow's reference in an annotation tag visible to the user.
     *
     * @param reference the referred meta-workflow
     */
    private static void addWFRefAnnotation(String reference) {
        Node globalAnnotation = XMLManager.getGlobalAnnotationNode(getDocument());
        List<String> lines = new ArrayList<>(Notation.getGlobalAnnotationDefaultContent());
        String metaRef = lines.remove(1);
        metaRef = String.format(metaRef, Notation.REFERENCES_DELIMITER_LEFT, reference,
                Notation.REFERENCES_DELIMITER_RIGHT);
        lines = lines.stream()
                .map(l -> String.format(l, Notation.REFERENCES_DELIMITER_LEFT,
                        Notation.REFERENCES_DELIMITER_RIGHT))
                .collect(Collectors.toList());
        lines.add(1, metaRef);
        Node newTextNode = getDocument().createElement(BPMNNames.TEXT.getName());
        newTextNode.setTextContent(String.join("\n", lines));
        globalAnnotation.appendChild(newTextNode);
    }

    /**
     * Adds the extension part to the given {@code node}.
     *
     * <p>
     *
     * The extension consists in style parameters to highlight instantiated tasks.
     *
     * @param node Node to add the extension
     *
     * @see Node
     */
    private void addExtensionNode(Node node) {
        Node extension = getDocument().createElement(BPMNNames.EXTENSION.getName());
        Element style = getDocument().createElement(BPMNNames.STYLE.getName());
        String backColor = (isOptionalElement((Element) node)) ? OPTIONAL_COLOR : MANDATORY_COLOR;
        log.debug("   Adding style {} to node {}", backColor, node);
        style.setAttribute(BPMNAttributes.BACKGROUND.getName(), backColor);
        extension.appendChild(style);
        log.debug("   Inserting node : {} before {}...", node, node.getFirstChild());
        node.insertBefore(extension, node.getFirstChild());
    }

    /**
     * Returns whether the given {@code element} is optional or not.
     *
     * @param element element to get the optionality value
     *
     * @return whether the given {@code element} is optional or not
     */
    private static boolean isOptionalElement(Element element) {
        for (String documentation : XMLManager.getAllBPMNDocContent(element)) {
            Matcher matcher = RegexManager.getOptionalityPattern().matcher(documentation.replace(" ", ""));
            if (matcher.find() && (matcher.groupCount() > 0)) {
                return Boolean.parseBoolean(matcher.group(1));
            }
        }
        return false;
    }
}
