package com.ml2wf.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
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
 * @version 1.0
 *
 */
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
	private int taskCounter = 0;
	/**
	 * This {@code propertyCounter} is incremented for each property added in the
	 * current workflow. This allows other methods to give a unique ID to a given
	 * property.
	 */
	private int propertyCounter = 0;
	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(InstanceFactoryImpl.class);

	/**
	 * {@code InstanceFactory}'s default constructor.
	 *
	 * @param file the XML file.
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
	 * <b>{@link Notation#getInstanceVoc()}</b> + <b>.FileExtension</b>.
	 *
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see Node
	 */
	@Override
	public void getWFInstance(File outputFile)
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		logger.info("Starting the Workflow instatiation...");
		this.addMetaWFReferences();
		String logMsg;
		for (Node node : XMLManager.getTasksList(getDocument(), BPMNNames.SELECTOR)) {
			logMsg = String.format("Instantiating the node %s...", node);
			logger.debug(logMsg);
			this.instantiateNode(node);
		}
		logger.info("Instantiation finished.");
	}

	/**
	 * Calls the {@link #getWFInstance(String)} method with
	 * {@link #getSourceFile()} as parameter.
	 *
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see {@link #getWFInstance(String)}
	 */
	public void getWFInstance() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		this.getWFInstance(super.getSourceFile());
	}

	/**
	 * Returns the last reference from the given {@code content}.
	 *
	 * <p>
	 *
	 * e.g. A#B#C -> C
	 *
	 * @param content content to retrieve the last reference
	 * @return the last reference from the given {@code content}
	 *
	 * @since 1.0
	 */
	public static String getLastReference(String content) {
		// splitting references
		String[] splittedContent = content.split(Notation.getGeneratedPrefixVoc());
		// getting the last reference
		return sanitizeName(splittedContent[splittedContent.length - 1]);
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
	 * @since 1.0
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
		this.addProperty(node, Notation.getBpmnPropertyInstance());
		this.addExtensionNode(node);
		// node renaming part
		Node nodeAttrName = node.getAttributes().getNamedItem(BPMNAttributes.NAME.getName());
		String nodeName = XMLManager.sanitizeName(currentRef) + "_" + this.taskCounter++;
		nodeAttrName.setNodeValue(nodeName);
	}

	/**
	 * Adds the given {@code propertyValue} to the given {@code node}.
	 *
	 * @param node node to add the property
	 *
	 * @since 1.0
	 */
	private void addProperty(Node node, String propertyValue) {
		Element propertyAttr = getDocument().createElement(BPMNNames.PROPERTY.getName());
		String propertyId = Notation.getBpmnPropertyPrefix() + this.propertyCounter++;
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
	 * {@link #addMetaWFRefAnnot(String...)} method,</li>
	 * </ul>
	 *
	 * @since 1.0
	 */
	private void addMetaWFReferences() {
		// TODO: add logs
		logger.debug("Adding the meta reference...");
		String metaReference = XMLManager.getWorkflowName(getDocument()).replace(" ", "_");
		this.addMetaWFRefDoc(metaReference);
		this.addWFRefAnnot(metaReference);
	}

	/**
	 * Adds the metaworkflow reference in a documentation tag under the process one.
	 *
	 * @param reffered referred meta task
	 *
	 * @since 1.0
	 */
	private void addMetaWFRefDoc(String referred) {
		logger.debug("Adding meta referrence in the documentation...");
		NodeList processNodeList = getDocument().getElementsByTagName(BPMNNames.PROCESS.getName());
		if (processNodeList.getLength() == 0) {
			logger.error("Error while getting the reffered metaworkflow's name.");
			logger.warn("Skipping this step...");
			return;
		}
		Node processNode = processNodeList.item(0);
		mergeNodesTextContent(addDocumentationNode(processNode), getReferenceDocumentation(referred));
	}

	/**
	 * Adds the workflow's reference in an annotation tag visible to the user.
	 *
	 * @param reference the referred meta-workflow
	 *
	 * @since 1.0
	 */
	private void addWFRefAnnot(String reference) {
		Node globalAnnotation = XMLManager.getGlobalAnnotationNode(getDocument());
		List<String> lines = new ArrayList<>(Notation.getGlobalAnnotationDefaultContent());
		String metaRef = lines.remove(1);
		metaRef = String.format(metaRef, Notation.getReferencesDelimiterLeft(), reference,
				Notation.getReferencesDelimiterRight());
		lines = lines.stream()
				.map(l -> String.format(l, Notation.getReferencesDelimiterLeft(),
						Notation.getReferencesDelimiterRight()))
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
	 * @since 1.0
	 * @see Node
	 */
	private void addExtensionNode(Node node) {
		Node extension = getDocument().createElement(BPMNNames.EXTENSION.getName());
		Element style = getDocument().createElement(BPMNNames.STYLE.getName());
		String backColor = (this.isOptionalElement((Element) node)) ? OPTIONAL_COLOR : MANDATORY_COLOR;
		logger.debug("	Adding style {} to node {}", backColor, node);
		style.setAttribute(BPMNAttributes.BACKGROUND.getName(), backColor);
		extension.appendChild(style);
		logger.debug("   Inserting node : {} before {}...", node, node.getFirstChild());
		node.insertBefore(extension, node.getFirstChild());
	}

	/**
	 * Returns whether the given {@code element} is optional or not.
	 *
	 * @param element element to get the optionality value
	 * @return whether the given {@code element} is optional or not
	 *
	 * @since 1.0
	 */
	private boolean isOptionalElement(Element element) {
		for (String documentation : XMLManager.getAllBPMNDocContent(element)) {
			Matcher matcher = RegexManager.getOptionalityPattern().matcher(documentation.replace(" ", ""));
			if (matcher.find() && (matcher.groupCount() > 0)) {
				return Boolean.valueOf(matcher.group(1));
			}
		}
		return false;
	}

}
