package com.ml2wf.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.fm.FMAttributes;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.base.BaseMergerImpl;

/**
 * This class contains useful methods for the file handling and more precisely
 * for the processing of {@code File} instances that will be used for the
 * {@code Document} save operations.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public class FileHandler {

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(FileHandler.class);
	/**
	 * The default file name.
	 */
	private static final String DEFAULT_FNAME = "BAD_FILE_NAME";
	/**
	 * Extension separator for files.
	 */
	private static final String EXTENSION_SEPARATOR = ".";
	/**
	 * Workflow's extensions.
	 * TODO: manage considering used norm (e.g. BPMN)
	 */
	private static final String[] WF_EXTENSIONS = { "bpmn", "bpmn2" };

	/**
	 * {@code FileHandler} empty constructor.
	 */
	private FileHandler() {

	}

	/**
	 * Returns the default file name.
	 *
	 * <p>
	 *
	 * The default file name is the document's file name if it is not null,
	 * otherwise it is {@link #DEFAULT_FNAME}.
	 *
	 * @return the default file name
	 *
	 * @since 1.0
	 */
	private static String getDefaultFileName() {
		if (XMLManager.getDocument() == null) {
			return DEFAULT_FNAME;
		}
		String documentURI = XMLManager.getDocument().getDocumentURI();
		return (documentURI != null) ? FilenameUtils.getName(documentURI) : DEFAULT_FNAME;
	}

	/**
	 * Returns the file extension separator.
	 *
	 * @return the file extension separator
	 */
	public static String getExtensionSeparator() {
		return EXTENSION_SEPARATOR;
	}

	/**
	 * Returns the workflow's extensions.
	 *
	 * @return the workflow's extensions
	 */
	public static String[] getWfExtensions() {
		return WF_EXTENSIONS;
	}

	/**
	 * Processes the given {@code File} instance by calling the
	 * {@link #processExistingFile(File)} method if the given {@code outputFile}
	 * exists, otherwise by calling the {@link #processMissingFile(File)} method.
	 *
	 * @param manager    the manager that will use the processed file. It is used to
	 *                   adopt a strategy according to the given {@code outputFile}
	 *                   type.
	 * @param outputFile output file or directory
	 * @return the processed {@code File} instance
	 * @throws IOException
	 *
	 * @since 1.0
	 */
	public static File processFile(XMLManager manager, File outputFile) throws IOException {
		// TODO: remove manager and access it through XMLManager#instance if singleton
		if ((manager instanceof BaseMergerImpl) && isDirectory(outputFile)) {
			String errorMsg = String.format("Can't process the given directory : %s", outputFile);
			errorMsg += "\nA valid XML file was expected.";
			throw new IOException(errorMsg);
		}
		return (outputFile.exists()) ? processExistingFile(outputFile) : processMissingFile(outputFile);
	}

	/**
	 * Processes the {@code File} instance by joining the given
	 * {@code outputFile} with the {@link #getDefaultFileName()} if it is a
	 * directory.
	 *
	 * @param outputFile output file or directory
	 * @return the processed {@code File} instance
	 *
	 * @since 1.0
	 */
	public static File processExistingFile(File outputFile) {
		if (outputFile.isFile()) {
			// if it is a file
			logger.warn("The operation will override the file : {}.", outputFile.getPath());
			return outputFile;
		} else {
			// else we have to join the directory with the file name based on the
			// source file name
			return Paths.get(outputFile.getPath(), insertInFileName(getDefaultFileName(), Notation.getInstanceVoc()))
					.toFile();
		}
	}

	/**
	 * Processes the {@code File} instance by creating the required
	 * (sub)directories and joining the given {@code outputFile} with the
	 * {@link #getDefaultFileName()} if it is a directory.
	 *
	 * @param outputFile output file or directory
	 * @return the processed {@code File} instance
	 *
	 * @since 1.0
	 */
	public static File processMissingFile(File outputFile) throws IOException {
		String path = outputFile.getPath();
		String fname = FilenameUtils.getName(path);
		if (FilenameUtils.getExtension(path).isBlank() || fname.isBlank()) {
			// if it references to a directory, we have to create it
			if (outputFile.mkdirs()) {
				// if it is created
				return Paths
						.get(path, insertInFileName(getDefaultFileName(), Notation.getInstanceVoc()))
						.toFile();
			} else {
				// we can't create the wished directory
				throw new IOException(String.format("Can't make the directory : %s", outputFile.getPath()));
			}
		} else {
			String dirPath = FilenameUtils.getFullPath(path);
			File dirFile = new File(dirPath);
			if (!dirFile.exists() && !dirFile.mkdirs()) {
				throw new IOException(String.format("Can't make the directory : %s", dirPath));
			}
			return outputFile;
		}
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
		String extension = FilenameUtils.getExtension(fName);
		if (extension != null) {
			String name = FilenameUtils.getBaseName(fName);
			String parentPath = FilenameUtils.getFullPath(fName);
			return parentPath + name + content + EXTENSION_SEPARATOR + extension;
		}
		logger.warn("Error while renaming file : {}", fName);
		String errorfName = "BACKUP_ERROR.xml";
		logger.warn("Saving backup file as : {}", errorfName);
		return errorfName;
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
		logger.info("Preprocessing file : {}...", file.getName());
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		// --- protection against XXE attacks
		logger.debug("Protecting against XXE attacks");
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
		dbFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant
		// ---
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document;
		if (file.exists()) {
			document = dBuilder.parse(file);
			document.getDocumentElement().normalize();
		} else {
			document = createEmptyFM(dBuilder);
		}
		return document;
	}

	/**
	 * Creates an empty FeatureModel by creating an empty {@code Document} and
	 * adding a default structure.
	 *
	 * <p>
	 *
	 * The default structure is the following :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>
	 *	{@literal <extendedFeatureModel>}
	 *		{@literal <struct>}
	 *			{@literal <and abstract="true" mandatory="true" name="Root">}
	 *				{@literal <feature name="AD"/>}
	 *			{@literal </and>}
	 *		{@literal </struct>}
	 *	{@literal </extendedFeatureModel>}
	 * </code>
	 * </pre>
	 *
	 * @param docBuilder Document builder used to create the document
	 * @return the created document
	 * @throws ParserConfigurationException
	 *
	 * @since 1.0
	 * @see Document
	 */
	public static Document createEmptyFM(DocumentBuilder docBuilder) {
		Document document = docBuilder.newDocument();
		// adding the DOM structure
		Element root = (Element) document.appendChild(document.createElement(FMNames.EXTENDEDFEATUREMODEL.getName()));
		Element struct = (Element) root.appendChild(document.createElement(FMNames.STRUCT.getName()));
		Element base = (Element) struct.appendChild(document.createElement(FMNames.AND.getName()));
		base.setAttribute(FMAttributes.ABSTRACT.getName(), String.valueOf(true));
		base.setAttribute(FMAttributes.MANDATORY.getName(), String.valueOf(true));
		base.setAttribute(FMAttributes.NAME.getName(), BaseMergerImpl.DEFAULT_ROOT_NAME);
		Element adElement = (Element) base.appendChild(document.createElement(FMNames.FEATURE.getName()));
		adElement.setAttribute(FMAttributes.NAME.getName(), BaseMergerImpl.DEEPER_DEFAULT_ROOT_NAME);
		return document;
	}

	/**
	 * Saves the given {@code document} into the given {@code destFile}.
	 *
	 * @param file     the destination {@code File}
	 * @param document the document to save
	 * @throws TransformerException
	 * @throws IOException
	 *
	 * @since 1.0
	 * @see Document
	 */
	public static void saveDocument(File destFile, Document document) throws TransformerException, IOException {
		logger.info("Saving file at location : {}...", destFile);
		if (!destFile.createNewFile()) {
			logger.debug("[SAVE] Destination file aldready exists.");
			logger.debug("[SAVE] Overriding...");
		}
		DOMSource source = new DOMSource(document);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// --- protection against XXE attacks
		logger.debug("Protecting against XXE attacks");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		// ---
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(destFile);
		transformer.transform(source, result);
		logger.info("File saved.");
	}

	/**
	 * Backs up the given {@code document} at the given {@code path} location.
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
	 * @param path     the path destination
	 * @param document the document to back up
	 * @throws TransformerException
	 * @throws IOException
	 *
	 * @since 1.0
	 * @see Document
	 */
	public static void backUp(String path, Document document) throws TransformerException, IOException {
		logger.info("Backing up...");
		SimpleDateFormat dateFormater = null;
		Date backUpDate = new Date();
		dateFormater = new SimpleDateFormat("_dd_MM_yy_hh_mm");
		String backUpPath = FileHandler.insertInFileName(path,
				Notation.getBackupVoc() + dateFormater.format(backUpDate));
		File destFile = new File(backUpPath);
		FileHandler.saveDocument(destFile, document);
		logger.info("Back up finished.");
	}

	/**
	 * Returns whether the given {@code file} is a directory or not.
	 *
	 * <p>
	 *
	 * Unlike the {@link File#isDirectory()} method, this method will return true
	 * for a non-existing file that looks like a directory.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method, in a context of XML files treatment, considers
	 * a file without extension as a directory.
	 *
	 * @param file file to test
	 * @return whether the given {@code file} is a directory or not
	 *
	 * @since 1.0
	 * @see File
	 * @see FilenameUtils
	 */
	private static boolean isDirectory(File file) {
		return file.isDirectory() || FilenameUtils.getExtension(file.getName()).isBlank();
	}

}
