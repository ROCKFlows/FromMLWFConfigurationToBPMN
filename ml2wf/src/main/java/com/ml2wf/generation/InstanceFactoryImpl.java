package com.ml2wf.generation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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
	 * {@code InstanceFactory}'s default constructor.
	 *
	 * @param path  the XML filepath.
	 * @param fname the XML filename.
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public InstanceFactoryImpl(String path, String fname)
			throws ParserConfigurationException, SAXException, IOException {
		super(path, fname);
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
	 * The result filename will be <b>FileBaseName</b> + <b>_instance</b> +
	 * <b>.FileExtension</b>.
	 *
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 *
	 * @since 1.0
	 * @see Node
	 */
	@Override
	public void getWFInstance() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		for (Node node : XMLManager.getTasksList(super.getDocument(), BPMNNodesNames.SELECTOR)) {
			this.instantiateNode(node);
		}
		String resultFname = super.getFname().split("\\.")[0] + Notation.getInstanceVoc() + "."
				+ super.getFname().split("\\.")[1];
		super.save(resultFname);
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

		if (this.tasksMap.containsKey(nodeName)) {
			this.tasksMap.put(nodeName, this.tasksMap.get(nodeName) + 1);
		} else {
			this.tasksMap.put(nodeName, 1);
		}
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
		// TODO: define documentation numerotation
		documentation.setAttribute(BPMNNodesAttributes.ID.getName(), Notation.getDocumentationVoc() + this.docCount++);
		documentation.setIdAttribute(BPMNNodesAttributes.ID.getName(), true);
		CDATASection refersTo = super.getDocument().createCDATASection(Notation.getReferenceVoc()
				+ node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName()).getNodeValue());
		documentation.appendChild(refersTo);
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
		style.setAttribute(BPMNNodesAttributes.BACKGROUND.getName(), INSTANTIATE_COLOR);
		extension.appendChild(style);
		node.insertBefore(extension, node.getFirstChild());
	}

}
