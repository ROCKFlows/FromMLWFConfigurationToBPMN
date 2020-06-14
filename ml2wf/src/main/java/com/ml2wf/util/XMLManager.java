package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.TaskTagsSelector;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;

/**
 * This class is the base class for any XML managing class.
 *
 * <p>
 *
 * It also contains useful static methods for XML treatment.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public class XMLManager {

	/**
	 * Path to the XML file's directory.
	 */
	private String path;
	/**
	 * {@code File} instance of the XML file.
	 *
	 * @see File
	 */
	private File sourceFile;
	/**
	 * {@code Document} instance of the XML file.
	 *
	 * @see Document
	 */
	private Document document;
	/**
	 * Extension separator for files.
	 */
	private static final String EXTENSION_SEPARATOR = ".";
	/**
	 * Documentation's counter.
	 *
	 * <p>
	 *
	 * This counter is used to number each documentation which is required for the
	 * <a href="https://featureide.github.io/">FeatureIDE framework</a>.
	 */
	private int docCount = 0;
	/**
	 * Text annotation's counter.
	 *
	 * <p>
	 *
	 * This counter is used to number each text annotation which is required for the
	 * <a href="https://featureide.github.io/">FeatureIDE framework</a>.
	 */
	private static int textAnnotCount = 0;
	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(XMLManager.class);

	/**
	 * {@code XMLTool}'s default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that the {@link #preprocess()} method is called to initialize
	 * {@link #document}.
	 *
	 * @param filePath the XML filePath.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public XMLManager(String filePath) throws ParserConfigurationException, SAXException, IOException {
		this.path = filePath;
		this.sourceFile = new File(this.path);
		this.document = XMLManager.preprocess(this.sourceFile);
	}

	/**
	 * Returns the xml's path
	 *
	 * @return the xml's path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Sets the xml's path
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Returns the xml's {@code File} instance
	 *
	 * @return the xml's {@code File} instance
	 *
	 * @see File
	 */
	public File getSourceFile() {
		return this.sourceFile;
	}

	/**
	 * Sets the xml's {@code File} instance
	 *
	 * @param sourceFile the new xml's {@code File} instance
	 *
	 * @see File
	 */
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * Returns the xml's {@code Document} instance
	 *
	 * @return the xml's {@code Document} instance
	 *
	 * @see Document
	 */
	public Document getDocument() {
		return this.document;
	}

	/**
	 * Returns the xml's {@code Document} instance
	 *
	 * @param document the new xml's {@code Document} instance
	 *
	 * @see Document
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	/**
	 * Returns the file extension separator.
	 *
	 * @return the file extension separator
	 */
	public static String getExtensionSeparator() {
		return EXTENSION_SEPARATOR;
	}

	// Saving methods

	/**
	 * Saves the result file.
	 *
	 * @param resultFname filename of result file
	 * @throws TransformerException
	 *
	 * @since 1.0
	 */
	protected void save(String resultPath) throws TransformerException {
		String logMsg = String.format("Saving file at location : %s...", resultPath);
		logger.info(logMsg);
		DOMSource source = new DOMSource(this.document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// --- protection against XXE attacks
		logger.debug("Protecting against XXE attacks");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		// ---
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(resultPath);
		transformer.transform(source, result);
		logger.info("File saved.");
	}

	/**
	 * Backs up the current {@link #sourceFile}.
	 *
	 * <p>
	 *
	 * The result filename will be : <b>FileBaseName</b> +
	 * {@link Notation#getBackupVoc()} + <b>_dd_MM_yy_hh_mm.FileExtension</b>.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method hasn't any retroactive effect.
	 *
	 * @throws TransformerException
	 *
	 * @since 1.0
	 */
	protected void backUp() throws TransformerException {
		logger.info("Backing up...");
		SimpleDateFormat dateFormater = null;
		Date backUpDate = new Date();
		dateFormater = new SimpleDateFormat("_dd_MM_yy_hh_mm");
		String backUpPath = insertInFileName(this.path, Notation.getBackupVoc() + dateFormater.format(backUpDate));
		this.save(backUpPath);
		logger.info("Back up finished.");
	}

	// General static methods

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
	 * @param wfDocument      document to add the position node
	 * @param referredElement the referred element's name
	 * @param x               abscissa
	 * @param y               ordinate
	 * @return the positional {@code Node}
	 */
	public static Node createPositionalNode(Document wfDocument, String referredElement, double x, double y) {
		// TODO: improve this method (refactoring required)
		// TODO: refactor creating methods in enums
		logger.debug("Creating positional Node...");
		Element shapeNode = wfDocument.createElement(BPMNNodesNames.SHAPE.getName());
		// creating shape node's id attribute
		String idAttrName = Notation.getBpmnShapeVoc() + referredElement;
		Attr idAttr = wfDocument.createAttribute(BPMNNodesAttributes.ID.getName());
		idAttr.setNodeValue(idAttrName);
		shapeNode.setAttributeNode(idAttr);
		// adding referred element attribute
		shapeNode.setAttribute(BPMNNodesAttributes.ELEMENT.getName(), referredElement);
		// creating bounds child
		Element boundsNode = wfDocument.createElement(BPMNNodesNames.BOUNDS.getName());
		// adding location attributes
		boundsNode.setAttribute(BPMNNodesAttributes.HEIGHT.getName(), "150"); // TODO: store in constants
		boundsNode.setAttribute(BPMNNodesAttributes.WIDTH.getName(), "170");
		boundsNode.setAttribute(BPMNNodesAttributes.X.getName(), String.valueOf(x));
		boundsNode.setAttribute(BPMNNodesAttributes.Y.getName(), String.valueOf(y));
		shapeNode.appendChild(boundsNode);
		// creating label child
		Element labelNode = wfDocument.createElement(BPMNNodesNames.LABEL.getName());
		shapeNode.appendChild(labelNode);
		// TODO: create label id
		// creating bounds child
		boundsNode = wfDocument.createElement(BPMNNodesNames.BOUNDS.getName());
		// adding location attributes
		boundsNode.setAttribute(BPMNNodesAttributes.HEIGHT.getName(), "145");
		boundsNode.setAttribute(BPMNNodesAttributes.WIDTH.getName(), "160");
		boundsNode.setAttribute(BPMNNodesAttributes.X.getName(), String.valueOf(x + 5));
		boundsNode.setAttribute(BPMNNodesAttributes.Y.getName(), String.valueOf(y));
		labelNode.appendChild(boundsNode);
		// selecting main diagram node
		NodeList planeNodeList = wfDocument.getElementsByTagName(BPMNNodesNames.PLANE.getName());
		Node planeNode = planeNodeList.item(0);
		// appending new positional node
		planeNode.appendChild(shapeNode);
		return shapeNode;
	}

	/**
	 * Creates the global annotation {@code Node}.
	 *
	 * @param wfDocument document to get the global annotation node
	 * @return the global annotation {@code Node}
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public static Node createGlobalAnnotationNode(Document wfDocument) {
		// getting process node
		NodeList processNodeList = wfDocument.getElementsByTagName(BPMNNodesNames.PROCESS.getName());
		Node processNode = processNodeList.item(0);
		// updating the text annotation counter
		textAnnotCount += wfDocument.getElementsByTagName(BPMNNodesNames.ANNOTATION.getName()).getLength();
		// creating the annotation node
		Element annotationNode = wfDocument.createElement(BPMNNodesNames.ANNOTATION.getName());
		String annotName = Notation.getTextAnnotationVoc() + ++textAnnotCount;
		// creating the id attribute
		annotationNode.setAttribute(BPMNNodesAttributes.ID.getName(), annotName);
		// TODO: check setIdAttributeNode benefits
		// locating the annotation node
		createPositionalNode(wfDocument, annotName, 0., 0.);
		// adding to parent node
		return processNode.appendChild(annotationNode);
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
	 * @return the global annotation {@code Node}
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public static Node getGlobalAnnotationNode(Document wfDocument) {
		logger.debug("Getting global annotation node...");
		NodeList nodeList = wfDocument.getElementsByTagName(BPMNNodesNames.ANNOTATION.getName());
		List<Node> annotationNodes = XMLManager.nodeListAsList(nodeList);
		// TODO: factorize delimiter with the getWorkflowName one
		String delimiter = String.format("%s(.*)%s", Notation.getQuotedNotation(Notation.getWfNameDelimiterLeft()),
				Notation.getQuotedNotation(Notation.getWfNameDelimiterRight()));
		Pattern pattern = Pattern.compile(delimiter);
		for (Node annotation : annotationNodes) {
			if (pattern.matcher(annotation.getTextContent()).find()) {
				logger.debug("Global annotation node found.");
				return annotation;
			}
		}
		logger.warn("Global annotation node not found.");
		return createGlobalAnnotationNode(wfDocument);
	}

	/**
	 * Returns the <b>lowest common ancestor</b> (LCA) for given {@code nodes}.
	 *
	 * @param nodes nodes to retrieve the LCA
	 * @return the <b>lowest common ancestor</b> (LCA) for given {@code nodes}
	 *
	 * @since 1.0
	 */
	public static Node getLowestCommonAncestor(List<Node> nodes) {
		Node parent;
		List<Node> commonParents = new ArrayList<>();
		List<Node> nodeParents = new ArrayList<>();
		for (Node node : nodes) {
			// for each node
			parent = node;
			while ((parent = parent.getParentNode()) != null) {
				// get all parents
				nodeParents.add(parent);
			}
			if (commonParents.isEmpty()) {
				commonParents.addAll(nodeParents);
			}
			// retaining common parents
			commonParents.retainAll(nodeParents);
		}
		return commonParents.get(0);
	}

	/**
	 * Parses the document's annotations and returns the workflow's name.
	 *
	 * <p>
	 *
	 * If it is not found, returns the document's name.
	 *
	 * @param wfDocument document containing the workflow's name
	 * @return the workflow's name
	 */
	public static String getWorkflowName(Document wfDocument) {
		// TODO: add logs
		logger.debug("Getting workflow's name...");
		Node annotation = XMLManager.getGlobalAnnotationNode(wfDocument);
		// TODO: foreach line
		String regex = String.format("%s(.+)%s", Notation.getQuotedNotation(Notation.getWfNameDelimiterLeft()),
				Notation.getQuotedNotation(Notation.getWfNameDelimiterRight()));
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = (annotation != null) ? pattern.matcher(annotation.getTextContent()) : null;
		if ((matcher == null)) {
			return new File(wfDocument.getDocumentURI()).getName().split("\\.")[0];
		}
		if (matcher.find() && (matcher.groupCount() > 0) && !matcher.group(1).isBlank()) {
			return matcher.group(1);
		}
		return new File(wfDocument.getDocumentURI()).getName().split("\\.")[0];
	}

	/**
	 * Returns the name tag's value of the given {@code node} if exists.
	 *
	 * Returns an empty string if not.
	 *
	 * @param node node containing the name attribute
	 * @return Returns the name tag's value of the given {@code node} if exists
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public static String getNodeName(Node node) {
		String logMsg = String.format("Retrieving name for node : %s...", node);
		logger.debug(logMsg);
		if (!node.hasAttributes()) {
			return "";
		}
		Node n = node.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName());
		if (n != null) {
			logMsg = String.format("Node's name is : %s", n.getNodeValue());
			logger.debug(logMsg);
			return n.getNodeValue();
		}
		return "";
	}

	/**
	 * Returns the {@code Node} with the given {@code name} in the document.
	 *
	 * <p>
	 *
	 * Returns null if no node is found.
	 *
	 * @param root root containing source nodes
	 * @param name name of wished node
	 * @return the {@code Node} with the given {@code name} in the document or null
	 *         if no node is found
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	public static Node getNodeWithName(Node root, String name) {
		// TODO: to test
		NodeList children = root.getChildNodes();
		Node child;
		Node recursiveResult; // result of recursive call
		for (int i = 0; i < children.getLength(); i++) {
			child = children.item(i);
			String childName = XMLManager.getNodeName(child);
			logger.debug("child name : " + childName);
			if (childName.equals(name)) {
				return child;
			} else if ((recursiveResult = getNodeWithName(child, name)) != null) {
				return recursiveResult;
			}
		}
		return null;
	}

	/**
	 * Merges {@code nodeA}'s text content with {@code nodeB}'s text content.
	 *
	 * <p>
	 *
	 * If the given nodes have different names (different tags), then the merge will
	 * fail and false will be returned.
	 *
	 * <b>Note</b> that the only node content that change is the {@code nodeA}'s
	 * one.
	 *
	 * @param nodeA first node
	 * @param nodeB second node
	 * @return true if the merge operation succeed, false if it failed.
	 */
	public static boolean mergeNodesTextContent(Node nodeA, Node nodeB) {
		// TODO: improve this method
		if (!nodeA.getNodeName().equals(nodeB.getNodeName())) {
			return false;
		}
		String contentA = nodeA.getTextContent().trim().replace("\\s+", " ");
		String contentB = nodeB.getTextContent().trim().replace("\\s+", " ");
		contentA = contentA.replace(contentB, "");
		nodeA.setTextContent(contentA + "\n" + contentB);
		return true;
	}

	/**
	 * Returns all task nodes of the given {@code document}.
	 *
	 * @param document source document of task nodes extraction
	 * @return all task nodes of the given {@code document}
	 *
	 * @since 1.0
	 *
	 * @see Document
	 * @see Node
	 */
	public static List<Node> getTasksList(Document document, TaskTagsSelector selector) {
		logger.debug("Retrieving tasks list...");
		List<Node> nodes = new ArrayList<>();
		for (String taskTag : selector.getTaskTags()) {
			nodes.addAll(XMLManager.nodeListAsList(document.getElementsByTagName(taskTag)));
		}
		return nodes;
	}

	/**
	 * Preprocess the given XML file before any treatment.
	 *
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *
	 * @since 1.0
	 *
	 * @see Document
	 */
	public static Document preprocess(File file) throws ParserConfigurationException, SAXException, IOException {
		String logMsg = String.format("Preprocessing file : %s...", file.getName());
		logger.info(logMsg);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		// --- protection against XXE attacks
		logger.debug("Protecting against XXE attacks");
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
		// ---
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(file);
		document.getDocumentElement().normalize();
		return document;
	}

	/**
	 * Returns the {@code Document} according to the specified {@code url}.
	 *
	 * @param url url of the xml file
	 * @return the {@code Document} according to the specified {@code url}.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 *
	 * @since 1.0
	 *
	 * @see Document
	 * @see URL
	 */
	public static Document getDocumentFromURL(URL url) throws SAXException, IOException, ParserConfigurationException {
		String logMsg = String.format("Retrieving document for URL : %s...", url);
		logger.debug(logMsg);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(url.openStream());
		document.getDocumentElement().normalize();
		return document;
	}

	/**
	 * Returns a {@code List} version of the {@code NodeList} parameter.
	 *
	 * @param nodes {@code NodeList} source
	 * @return a {@code List} version of the {@code NodeList} parameter
	 *
	 * @since 1.0
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
	 * Returns a {@code List} containing all {@code nodes}' names.
	 *
	 * @param nodes nodes to get the names
	 * @return a {@code List} containing all {@code nodes}' names
	 *
	 * @since 1.0
	 *
	 * @see NodeList
	 */
	public static List<String> getNodesNames(List<Node> nodes) {
		return nodes.stream().map(XMLManager::getNodeName)
				.collect(Collectors.toList());
	}

	/**
	 * Sanitizes {@code name} removing genericity and instantiation caracteristics.
	 *
	 * @param name name to sanitize
	 * @return sanitized {@code name}
	 *
	 * @since 1.0
	 */
	public static String sanitizeName(String name) {
		// sanitization for instantiate WF's task
		List<String> splitted = new ArrayList<>(Arrays.asList(name.split(Notation.getGeneratedPrefixVoc())));
		splitted.removeIf(String::isBlank);
		if (!splitted.isEmpty()) {
			name = splitted.get(0);
		}
		name = name.replaceFirst(Notation.getDocumentationVoc(), "");
		name = name.replaceFirst(Notation.getReferenceVoc(), "");
		name = name.replace(" ", "_");
		// sanitization for generic WF's task
		return name.replaceFirst(Notation.getGenericVoc() + "$", "");
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
	public void addDocumentationNode(Node node, String content) {
		Element documentation = this.document.createElement(BPMNNodesNames.DOCUMENTATION.getName());
		documentation.setAttribute(BPMNNodesAttributes.ID.getName(), Notation.getDocumentationVoc() + this.docCount++);
		documentation.setIdAttribute(BPMNNodesAttributes.ID.getName(), true);
		CDATASection refersTo = this.document.createCDATASection(Notation.getReferenceVoc() + content);
		String logMsg = String.format("   Adding documentation %s", refersTo.getTextContent());
		logger.debug(logMsg);
		documentation.appendChild(refersTo);
		logMsg = String.format("   Inserting node : %s before %s...", node, node.getFirstChild());
		logger.debug(logMsg);
		node.insertBefore(documentation, node.getFirstChild());
	}

	/**
	 * Returns the referred meta task from the given {@code reference} text.
	 *
	 * @param reference reference containing the referred meta task
	 * @return the referred meta task from the given {@code reference} text
	 */
	public static String getReferredTask(String reference) {
		return reference.replace(Notation.getReferenceVoc(), "").replace(Notation.getGenericVoc(), "");
	}

	/**
	 * Inserts given {@code content} between the FileBaseName and the FileExtension.
	 *
	 * @param fName   filename
	 * @param content content to insert
	 * @return the new {@code fName} with the inserted {@code content}
	 *
	 * @since 1.0
	 */
	public static String insertInFileName(String fName, String content) {
		// TODO: fix ..\ issue when splitting with .
		String[] splittedPath = fName.split(Pattern.quote(EXTENSION_SEPARATOR));
		int length = splittedPath.length;
		if (length > 1) {
			return splittedPath[length - 2] + content + EXTENSION_SEPARATOR + splittedPath[length - 1];
		}
		String logMsg = String.format("Error while renaming file : %s", fName);
		logger.warn(logMsg);
		String errorfName = "BACKUP_ERROR.xml";
		logMsg = String.format("Saving backup file as : %s", errorfName);
		logger.warn(logMsg);
		return errorfName;
	}
}
