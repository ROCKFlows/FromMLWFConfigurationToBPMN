package com.ml2wf.merge;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.util.XMLManager;

/**
 * This class merges a FeatureModel xml file with a Workflow xml file.
 *
 * <p>
 *
 * It is an extension of the {@link XMLManager} base class.
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
public class FeatureModelMerger extends XMLManager {

	/**
	 * {@code FeatureModelMerger}'s default constructor.
	 *
	 * @param path  the XML filepath.
	 * @param fname the XML filename.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public FeatureModelMerger(String path, String fname)
			throws ParserConfigurationException, SAXException, IOException {
		super(path, fname);
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
	 * <p>
	 *
	 * The result filename will be <b>FileBaseName</b> + <b>_instance</b> +
	 * <b>.FileExtension</b>.
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
		Document wfDocument = XMLManager.preprocess(new File(path + fname));
		// retrieving workflow's tasks
		List<Node> tasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		// retrieving all existing FM's tasks names
		List<Node> existingTasks = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		List<String> existingTasksNames = existingTasks.stream().map(XMLManager::getNodeName)
				.collect(Collectors.toList());
		// iterating for each task
		String currentTaskName;
		for (Node task : tasks) {
			currentTaskName = XMLManager.getNodeName(task);
			currentTaskName = currentTaskName.replaceFirst(Notation.getGeneratedPrefixVoc(), "");
			// splitting
			String[] tName = currentTaskName.split(Notation.getGeneratedPrefixVoc());
			if (existingTasksNames.contains(tName[1] + "_" + tName[0])) {
				continue;
			}
			Node parentNode = this.getSuitableParent(task);
			this.insertNewTask(parentNode, task);
		}
		String resultFname = super.getFname().split("\\.")[0] + "_result." + super.getFname().split("\\.")[1];
		super.save(resultFname);
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
		List<Node> candidates = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		// electing the good candidate
		String candidateName;
		for (Node candidate : candidates) {
			candidateName = XMLManager.getNodeName(candidate);
			if (candidateName.equals(docNode.getTextContent().replace(Notation.getReferenceVoc(), ""))) {
				return candidate;
			}
		}
		return super.getDocument().getElementsByTagName(FeatureModelNames.AND.getName()).item(0);
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
		// retrieving task name content
		String[] taskName = XMLManager.getNodeName(task).replaceFirst(Notation.getGeneratedPrefixVoc(), "")
				.split(Notation.getGeneratedPrefixVoc());
		// converting task name to the new node name
		String newNodeName = taskName[1] + "_" + taskName[0];
		// inserting the new node
		Element newNode = super.getDocument().createElement(FeatureModelNames.FEATURE.getName());
		newNode.setAttribute(FeatureModelAttributes.NAME.getName(), newNodeName);
		parentNode.appendChild(newNode);
	}
}
