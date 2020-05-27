package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesAttributes;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.util.XMLTool;

/**
 * This class merges a FeatureModel xml file with a Workflow xml file.
 *
 * <p>
 *
 * It aims at the application of <b>metalearning</b> for workflow automation as
 * part of the <b>ml2wf project</b>.
 *
 * <p>
 *
 * Please refer to the <a href="https://featureide.github.io/">FeatureIDE
 * framework</a> for further information about a FeatureModel.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public class FeatureModelMerger {

	/**
	 * Path to the XML FeatureModel file's directory.
	 */
	private String path;
	/**
	 * XML FeatureModel filename.
	 */
	private String fname;
	/**
	 * {@code File} instance of the XML FeatureModel file.
	 *
	 * @see File
	 */
	private File fmFile;
	/**
	 * {@code Document} instance of the XML FeatureModel file.
	 *
	 * @see Document
	 */
	private Document fmDocument;

	/**
	 * {@code FeatureModelMerger}'s default constructor.
	 *
	 * @param path
	 * @param fname
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public FeatureModelMerger(String path, String fname)
			throws ParserConfigurationException, SAXException, IOException {
		this.path = path;
		this.fname = fname;
		// TODO: check path always ending with /
		this.fmFile = new File(this.path + this.fname);
		this.fmDocument = XMLTool.preprocess(this.fmFile);
	}

	/**
	 * Merges the given <b>instantiated</b> Workflow with the {@link #fmDocument
	 * FeatureModel};
	 *
	 * <p>
	 *
	 * More precisely,
	 *
	 * <ul>
	 * <li>retrieves a suitable parent from the FeatureModel for each task using the
	 * {@link #getSuitableParent(Node)} method,</li>
	 * <li>inserts the task under this suitable parent using the
	 * {@link #insertNewTask(Node, Node)} method.</li>
	 * <li>saves the modifications using the {@link #save()} method.</li>
	 * </uL>
	 *
	 * @param path  Path to the xml workflow file.
	 * @param fname file name of the xml workflow file.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 *
	 * @since 1.0
	 * @see Node
	 * @see InstanceFactory
	 *
	 */
	public void mergeWithWF(String path, String fname)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		// TODO: to comment
		// preprocessing the document
		Document wfDocument = XMLTool.preprocess(new File(path + fname));
		// retrieving workflow's tasks
		List<Node> tasks = XMLTool.nodeListAsList(wfDocument.getElementsByTagName(BPMNNodesNames.TASK.getName()));
		tasks.addAll(XMLTool.nodeListAsList(wfDocument.getElementsByTagName(BPMNNodesNames.USERTASK.getName())));
		// retrieving all existing FM's tasks names
		// TODO: to optimize
		List<Node> existingTasks = XMLTool
				.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.AND.getName()));
		existingTasks
				.addAll(XMLTool.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.ALT.getName())));
		existingTasks.addAll(
				XMLTool.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.FEATURE.getName())));
		List<String> existingTasksNames = existingTasks.stream().map(Node::getAttributes)
				.map((n) -> n.getNamedItem(FeatureModelAttributes.NAME.getName())).map(Node::getNodeValue)
				.collect(Collectors.toList());
		// iterating for each task
		String currentTaskName;
		for (Node task : tasks) {
			currentTaskName = task.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName()).getNodeValue();
			currentTaskName = currentTaskName.replaceFirst(Notation.getGeneratedPrefixVoc(), "");
			// splitting
			String[] tName = currentTaskName.split(Notation.getGeneratedPrefixVoc());
			if (existingTasksNames.contains(tName[1] + "_" + tName[0])) {
				continue;
			}
			Node parentNode = this.getSuitableParent(task);
			this.insertNewTask(parentNode, task);
		}
		this.save();
	}

	/**
	 * Returns a suitable parent for the {@code Node task} according to its
	 * specified <b>reference</b>.
	 *
	 * <p>
	 *
	 * If there isn't any valid referenced parent, returns the first document node.
	 *
	 * <p>
	 *
	 * <b>Note</b> that each instantiated task <b>refers to a generic one presents
	 * in the FeatureModel</B>.
	 *
	 * @param task task to get a suitable parent
	 * @return a suitable parent for the {@code Node task} according to its
	 *         specified reference
	 */
	private Node getSuitableParent(Node task) {
		// TODO: to test
		// retrieving the references parent
		Node docNode = ((Element) task).getElementsByTagName(BPMNNodesNames.DOCUMENTATION.getName()).item(0);
		// retrieving all candidates
		List<Node> candidates = XMLTool
				.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.AND.getName()));
		candidates
				.addAll(XMLTool.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.ALT.getName())));
		candidates.addAll(
				XMLTool.nodeListAsList(this.fmDocument.getElementsByTagName(FeatureModelNames.FEATURE.getName())));
		// electing the good candidate
		Node nameAttribute;
		for (Node candidate : candidates) {
			nameAttribute = candidate.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName());
			if (nameAttribute.getNodeValue().equals(docNode.getTextContent().replace(Notation.getReferenceVoc(), ""))) {
				return candidate;
			}
		}
		return this.fmDocument.getElementsByTagName(FeatureModelNames.AND.getName()).item(0);
	}

	/**
	 * Inserts the new task corresponding of the given {@code Node task} under the
	 * given {@code Node parentNode}.
	 *
	 * <p>
	 *
	 * The new task is converted to match the FeatureModel format.
	 *
	 * @param parentNode Parent node of the new task
	 * @param task       task to insert
	 *
	 * @see Node
	 */
	private void insertNewTask(Node parentNode, Node task) {
		// TODO: to test
		// retrieving task name content
		Node taskNameNode = task.getAttributes().getNamedItem(BPMNNodesAttributes.NAME.getName());
		String[] nameParts = taskNameNode.getNodeValue().replaceFirst(Notation.getGeneratedPrefixVoc(), "")
				.split(Notation.getGeneratedPrefixVoc());
		// converting task name to the new node name
		String nodeName = nameParts[1] + "_" + nameParts[0];
		// inserting the new node
		Element newNode = this.fmDocument.createElement(FeatureModelNames.FEATURE.getName());
		newNode.setAttribute(FeatureModelAttributes.NAME.getName(), nodeName);
		parentNode.appendChild(newNode);
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
		// TODO: factorize with InstanceFactoryImpl#save
		DOMSource source = new DOMSource(this.fmDocument);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		String resultFname = this.fname.split("\\.")[0] + "_result." + this.fname.split("\\.")[1]; // TODO: to define
		StreamResult result = new StreamResult(this.path + resultFname);
		transformer.transform(source, result);
	}
}
