package com.ml2wf.generation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
	 * Documentation's counter.
	 *
	 * <p>
	 *
	 * This counter is used to number each documentation which is required for the
	 * <a href="https://featureide.github.io/">FeatureIDE framework</a>.
	 */
	private int docCount;
	/**
	 * This {@code Map} counts for each generic tasks the number of instantiated
	 * tasks.
	 */
	private Map<String, Integer> tasksMap;
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
		this.docCount = 0;
		this.tasksMap = new HashMap<>();
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
		this.addDocumentationNode(node);
		// extension part
		this.addExtensionNode(node);
		// node renaming part
		Node nodeAttrName = node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName());

		String nodeName = Notation.getGeneratedPrefixVoc()
				+ nodeAttrName.getNodeValue().replace(Notation.getGenericVoc(), "");

		this.tasksMap.put(nodeName, this.tasksMap.containsKey(nodeName) ? this.tasksMap.get(nodeName) + 1 : 1);

		nodeName += Notation.getGeneratedPrefixVoc() + this.tasksMap.get(nodeName);
		nodeAttrName.setNodeValue(nodeName);
	}

	/**
	 * Adds the documentation part to the given {@code node}.
	 *
	 * <p>
	 *
	 * The documentation contains informations about the task's ID and the referred
	 * generic task.
	 *
	 * @param node Node to add the documentation
	 *
	 * @since 1.0
	 * @see Node
	 */
	private void addDocumentationNode(Node node) {
		Element documentation = super.getDocument().createElement(BPMNNodesNames.DOCUMENTATION.getName());
		documentation.setAttribute(BPMNNodesAttributes.ID.getName(), Notation.getDocumentationVoc() + this.docCount++);
		documentation.setIdAttribute(BPMNNodesAttributes.ID.getName(), true);
		CDATASection refersTo = super.getDocument().createCDATASection(Notation.getReferenceVoc()
				+ node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName()).getNodeValue());
		String logMsg = String.format("   Adding documentation %s", refersTo.getTextContent());
		logger.debug(logMsg);
		documentation.appendChild(refersTo);
		logMsg = String.format("   Inserting node : %s before %s...", node, node.getFirstChild());
		logger.debug(logMsg);
		node.insertBefore(documentation, node.getFirstChild());
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
