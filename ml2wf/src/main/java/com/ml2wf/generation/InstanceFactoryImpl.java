package com.ml2wf.generation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.enums.BPMNNodesAttributes;
import com.ml2wf.enums.BPMNNodesNames;

/**
 * This class is a factory for the instantiation of abstract workflows.
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
public class InstanceFactoryImpl implements InstanceFactory {

	/**
	 * Hexadecimal color code of instantiated elements.
	 */
	public static final String INSTANTIATE_COLOR = "#00f900";
	/**
	 * Path to the XML file's directory.
	 */
	private String path;
	/**
	 * XML filename.
	 */
	private String fname;
	/**
	 * {@code File} instance of the XML file.
	 *
	 * @see File
	 */
	private File inputFile;
	/**
	 * {@code Document} instance of the XML file.
	 *
	 * @see Document
	 */
	private Document document;
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
	 * {@code InstanceFactory}'s default constructor.
	 *
	 * @param path  The XML filepath.
	 * @param fname the XML filename.
	 */
	public InstanceFactoryImpl(String path, String fname) {
		this.path = path;
		this.fname = fname;
		// TODO: check path always ending with /
		this.inputFile = new File(this.path + this.fname);
		this.docCount = 0;
	}

	/**
	 * Preprocess the given XML file before any treatment.
	 *
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *
	 * @since 1.0
	 */
	private void preprocess() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		this.document = dBuilder.parse(this.inputFile);
		this.document.getDocumentElement().normalize();
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
	 * <b>Note</b> that the {@link #preprocess()} method is called before any
	 * treatment.
	 *
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 *
	 * @since 1.0
	 * @see Node
	 */
	public void getWFInstance() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		this.preprocess();
		// TODO: improve this part
		NodeList nodes = this.document.getElementsByTagName(BPMNNodesNames.TASK.getName());
		for (int i = 0; i < nodes.getLength(); i++) {
			this.instantiateNode(nodes.item(i));
		}
		nodes = this.document.getElementsByTagName(BPMNNodesNames.USERTASK.getName());
		for (int i = 0; i < nodes.getLength(); i++) {
			this.instantiateNode(nodes.item(i));
		}
		this.save();
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
		Node nodeName = node.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName());
		nodeName.setNodeValue(
				Notation.getGeneratedPrefixVoc() + nodeName.getNodeValue().replace(Notation.getGenericVoc(), ""));
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
		Element documentation = this.document.createElement(BPMNNodesNames.DOCUMENTATION.getName());
		// TODO: define documentation numerotation
		documentation.setAttribute(BPMNNodesAttributes.ID.getName(), "Documentation_" + this.docCount++);
		documentation.setIdAttribute(BPMNNodesAttributes.ID.getName(), true);
		CDATASection refersTo = this.document.createCDATASection(Notation.getReferenceVoc()
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
		Node extension = this.document.createElement(BPMNNodesNames.EXTENSION.getName());
		Element style = this.document.createElement(BPMNNodesNames.STYLE.getName());
		style.setAttribute(BPMNNodesAttributes.BACKGROUND.getName(), INSTANTIATE_COLOR);
		extension.appendChild(style);
		node.insertBefore(extension, node.getFirstChild());
	}

	/**
	 * Saves the instantiated workflow as a new bpmn2 file.
	 *
	 * <p>
	 *
	 * The result filename will be <b>FileBaseName</b> + <b>_instance</b> +
	 * <b>.FileExtension</b>.
	 *
	 * @throws TransformerException
	 *
	 * @since 1.0
	 */
	private void save() throws TransformerException {
		DOMSource source = new DOMSource(this.document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		String resultFname = this.fname.split("\\.")[0] + Notation.getInstanceVoc() + "." + this.fname.split("\\.")[1];
		StreamResult result = new StreamResult(this.path + resultFname);
		transformer.transform(source, result);
	}

}
