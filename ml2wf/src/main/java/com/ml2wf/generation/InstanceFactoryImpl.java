package com.ml2wf.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
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
	 * Hexadecimal color code of instantiated elements.
	 */
	public static final String INSTANTIATE_COLOR = "#00f900";
	/**
	 * This {@code taskCounter} is incremented for each task. This allows other
	 * methods to give an unique name to a given task.
	 */
	private int taskCounter = 0;
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
	 * @param filePath the XML filepath.
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public InstanceFactoryImpl(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
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
	public void getWFInstance(String resultPath)
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		logger.info("Starting the Workflow instatiation...");
		this.addMetaWFReferences();
		String logMsg;
		for (Node node : XMLManager.getTasksList(super.getDocument(), BPMNNodesNames.SELECTOR)) {
			logMsg = String.format("Instantiating the node %s...", node);
			logger.debug(logMsg);
			this.instantiateNode(node);
		}
		logger.info("Instantiation finished.");
		String resultFname = XMLManager.insertInFileName(super.getSourceFile().getName(), Notation.getInstanceVoc());
		super.save(Paths.get(resultPath, resultFname).toString());
	}

	/**
	 * Calls the {@link #getWFInstance(String)} method with
	 * {@link #getSourceFile()}'s directory as parameter.
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
		String absolutePath = super.getSourceFile().getAbsolutePath();
		this.getWFInstance(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator)));
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
		// documentation part
		// TODO: factorize in method ?
		String content = node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName()).getNodeValue();
		content = XMLManager.sanitizeName(content);
		this.addDocumentationNode(node, content);
		// extension part
		this.addExtensionNode(node);
		// node renaming part
		Node nodeAttrName = node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName());
		// TODO: update instance syntax
		String nodeName = Notation.getGeneratedPrefixVoc()
				+ nodeAttrName.getNodeValue().replace(Notation.getGenericVoc(), "");
		nodeName = XMLManager.sanitizeName(nodeName);
		nodeName += Notation.getGeneratedPrefixVoc() + content + this.taskCounter++;
		nodeAttrName.setNodeValue(nodeName);
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
	 * {@link #addMetaWFRefAnnot(String)} method,</li>
	 * </ul>
	 *
	 * @since 1.0
	 */
	private void addMetaWFReferences() {
		// TODO: add logs
		logger.debug("Adding the meta reference...");
		String referred = XMLManager.getWorkflowName(this.getDocument()).replace(" ", "_");
		String logMsg = String.format("Referred meta model is : %s.", referred);
		logger.debug(logMsg);
		this.addMetaWFRefDoc(referred);
		this.addMetaWFRefAnnot(referred);
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
		NodeList processNodeList = this.getDocument().getElementsByTagName(BPMNNodesNames.PROCESS.getName());
		if (processNodeList.getLength() == 0) {
			logger.error("Error while getting the reffered metaworkflow's name.");
			logger.warn("Skipping this step...");
			return;
		}
		Node processNode = processNodeList.item(0);
		this.addDocumentationNode(processNode, referred);
	}

	/**
	 * Adds the metaworkflow reference in an annotation tag visible to the user.
	 *
	 * @param reffered referred meta task
	 *
	 * @since 1.0
	 */
	private void addMetaWFRefAnnot(String referred) {
		NodeList processNodeList = this.getDocument().getElementsByTagName(BPMNNodesNames.PROCESS.getName());
		if (processNodeList.getLength() == 0) {
			logger.error("Error while getting the reffered metaworkflow's name.");
			logger.warn("Skipping this step...");
			return;
		}
		Node globalAnnotation = XMLManager.getGlobalAnnotationNode(this.getDocument());
		if ((globalAnnotation != null) && (globalAnnotation.getChildNodes().getLength() > 0)) {
			String content = "Workflow's name " + Notation.getWfNameDelimiterLeft() + Notation.getWfNameDelimiterRight()
					+ "\nThis workflow makes reference to the " + referred + " workflow.";
			Node newTextNode = this.getDocument().createElement(BPMNNodesNames.TEXT.getName());
			newTextNode.setTextContent(content);
			globalAnnotation.appendChild(newTextNode);
		} else {
			logger.error("The global annotation is missing.");
			logger.error("Maybe you removed it during the workflow modification ?");
			logger.warn("Skipping...");
		}
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
		Node extension = super.getDocument().createElement(BPMNNodesNames.EXTENSION.getName());
		Element style = super.getDocument().createElement(BPMNNodesNames.STYLE.getName());
		String logMsg = String.format("	Adding style %s to node %s", INSTANTIATE_COLOR, node);
		logger.debug(logMsg);
		style.setAttribute(BPMNNodesAttributes.BACKGROUND.getName(), INSTANTIATE_COLOR);
		extension.appendChild(style);
		logMsg = String.format("   Inserting node : %s before %s...", node, node.getFirstChild());
		logger.debug(logMsg);
		node.insertBefore(extension, node.getFirstChild());
	}

}
