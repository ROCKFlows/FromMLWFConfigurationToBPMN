package com.ml2wf.merge.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FeatureAttributes;
import com.ml2wf.conventions.enums.fm.FeatureNames;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

public abstract class BaseMergerImpl extends AbstractMerger implements BaseMerger {

	/**
	 * The {@code Element} corresponding of the merged workflow's created
	 * {@code Node}.
	 *
	 * @see Element
	 */
	protected Element createdWFNode;
	/**
	 * The {@code Element} corresponding of the <b>global unmanaged</b> created
	 * {@code Node}.
	 *
	 * @see Element
	 */
	protected Element unmanagedNode;
	/**
	 * Unmanaged parent's name.
	 *
	 * <p>
	 *
	 * Unmanaged nodes will be placed under a parent with this name.
	 */
	private static String UNMANAGED_PARENT_NAME = "Unmanaged";

	/**
	 * {@code BaseMergerImpl}'s default constructor.
	 *
	 * @param filePath the FeatureModel {@code File}
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public BaseMergerImpl(File file) throws ParserConfigurationException, SAXException, IOException {
		super(file);
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File wfFile) throws Exception {
		if (backUp) {
			super.backUp();
		}
		Set<File> files;
		try (Stream<Path> stream = Files.walk(wfFile.toPath())) {
			// TODO: factorize endsWith filter in a dedicated method (add extension in
			// notation and use the apache-io api
			files = stream.parallel().map(Path::toFile).filter(File::isFile)
					.filter(p -> p.getName().endsWith(".bpmn") || p.getName().endsWith(".bpmn2"))
					.collect(Collectors.toSet());
		}
		if (files.isEmpty()) {
			// wfFile is a regular file (not a directory)
			files.add(wfFile);
		}
		this.unmanagedNode = (Element) this.getGlobalTask(UNMANAGED_PARENT_NAME);
		for (File file : files) {
			Pair<String, Document> wfInfo = this.getWFDocInfoFromFile(file);
			if (wfInfo.isEmpty()) {
				// TODO: add logs
				return;
			}
			Document wfDocument = wfInfo.getValue();
			List<Node> tasks = getTasksList(wfDocument, BPMNNames.SELECTOR);
			for (Node task : tasks) {
				this.processTask(task);
			}
			this.processAnnotations(wfDocument);
			if (completeMerge) {
				this.processCompleteMerge(wfInfo);
				this.processSpecificNeeds(wfInfo);
			}
		}
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, File... wfFiles) throws Exception {
		for (File wfFile : wfFiles) {
			this.mergeWithWF(backUp, completeMerge, wfFile);
		}
	}

	/**
	 * Processes the complete merge of the workflow describe by the
	 * {@code wfInfo Pair}'s instance.
	 *
	 * <p>
	 *
	 * More precisely,
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>processes the association of constraints involving the given workflow
	 * using the {@link #processAssocConstraints(Document, String)} method,</li>
	 * <li>retrieves the root parent node using the {@link #getRootParentNode()}
	 * method,</li>
	 * <li>inserts the workflow's corresponding task under the root parent
	 * node.</li>
	 * </ul>
	 *
	 * @param wfInfo workflow's informations
	 * @throws InvalidConstraintException
	 *
	 * @see Pair
	 */
	private void processCompleteMerge(Pair<String, Document> wfInfo) throws InvalidConstraintException {
		String wfName = wfInfo.getKey();
		// TODO: check order execution (node creation before assocConstraints)
		this.processAssocConstraints(wfInfo.getValue(), wfName);
		this.createdWFNode = this.createFeatureWithName(wfName);
		Node root = this.getRootParentNode();
		this.createdWFNode = (Element) this.insertNewTask(root, this.createdWFNode);
	}

	/**
	 * Processes the given {@code task}.
	 *
	 * <p>
	 *
	 * More precisely,
	 *
	 * <p>
	 *
	 * <ul>
	 * <li>checks if the given {@code task} is duplicated using the
	 * {@link #isDuplicated(String)} method,</li>
	 * <b>If it is not duplicated,</b>
	 * <li>retrieves a suitable parent using the {@link #getSuitableParent(Node)}
	 * method,</li>
	 * <li>inserts the new task under the suitable parent.</li>
	 * </ul>
	 *
	 * @param task task to process
	 */
	protected void processTask(Node task) {
		String currentTaskName;
		// retrieving all existing FM's tasks names
		// for each task
		for (Node nestedTask : this.getNestedNodes(task)) {
			// for each subtask
			currentTaskName = XMLManager.getNodeName(nestedTask);
			currentTaskName = XMLManager.sanitizeName(currentTaskName);
			if (this.isDuplicated(currentTaskName)) {
				// if task is already in the FM
				Node child;
				if ((child = this.getChildWithName(this.unmanagedNode, currentTaskName)) == null) {
					// if it is not under the unmanaged node
					continue;
				}
				Node duplicatedTask = this.unmanagedNode.removeChild(child);
				nestedTask = this.mergeNodes(nestedTask, duplicatedTask);
			}
			// retrieving a suitable parent
			Node parentNode = this.getSuitableParent(nestedTask);
			// inserting the new task
			this.insertNewTask(parentNode, nestedTask);
		}
	}

	protected Node mergeNodes(Node nodeA, Node nodeB) {
		// TODO: improve considering conflicts (e.g same child & different levels)
		NodeList nodeBChildren = nodeB.getChildNodes();
		for (int i = 0; i < nodeBChildren.getLength(); i++) {
			nodeA.appendChild(nodeBChildren.item(i));
		}
		return nodeA;
	}

	protected Node getChildWithName(Node parent, String childName) {
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node candidate = children.item(i);
			if ((getNodeName(candidate).equals(childName))
					|| ((candidate = this.getChildWithName(candidate, childName)) != null)) {
				return candidate;
			}
		}
		return null;
	}

	/**
	 * Returns the <b>global</b> {@code Node} task with the given
	 * {@code globalNodeName}.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this method create it if not exist.
	 *
	 * <p>
	 *
	 * @param globalNodeName the <b>global</b> {@code Node} task with the given
	 *                       {@code globalNodeName}
	 * @return the global {@code Node} task
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node getGlobalTask(String globalNodeName) {
		// TODO: separate in two distinct methods
		List<Node> tasksNodes = XMLManager.getTasksList(getDocument(), FeatureNames.SELECTOR);
		for (Node taskNode : tasksNodes) {
			Node namedItem = taskNode.getAttributes().getNamedItem(FeatureAttributes.NAME.getName());
			if ((namedItem != null) && namedItem.getNodeValue().equals(globalNodeName)) {
				// aldready exists
				return taskNode;
			}
		}
		// create the node
		Element globalNode = getDocument().createElement(FeatureNames.AND.getName());
		globalNode.setAttribute(FeatureAttributes.ABSTRACT.getName(), String.valueOf(true));
		globalNode.setAttribute(FeatureAttributes.NAME.getName(), globalNodeName);
		return getDocument().getElementsByTagName(FeatureNames.AND.getName()).item(1)
				.appendChild(globalNode);
	}
}
