package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.TaskTagsSelector;
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
		Node n;
		if ((n = node.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName())) != null) {
			logMsg = String.format("Node's name is : %s", n.getNodeValue());
			logger.debug(logMsg);
			return n.getNodeValue();
		}
		logMsg = String.format("No name was found for node %s.", node);
		logger.warn(logMsg);
		return "";
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
	 * Returns a {@code List} containing all {@code nodes}' tasks names.
	 *
	 * @param nodes nodes to get the names
	 * @return a {@code List} containing all {@code nodes}' tasks names
	 *
	 * @since 1.0
	 *
	 * @see NodeList
	 */
	public static List<String> getTasksNames(List<Node> nodes) {
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
		name = name.replaceFirst(Notation.getGeneratedPrefixVoc(), "");
		name = name.replaceFirst(Notation.getGeneratedPrefixVoc() + "\\d*$", "");
		name = name.replaceFirst(Notation.getDocumentationVoc(), "");
		name = name.replaceFirst(Notation.getReferenceVoc(), "");
		// sanitization for generic WF's task
		return name.replaceFirst(Notation.getGenericVoc() + "$", "");
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
