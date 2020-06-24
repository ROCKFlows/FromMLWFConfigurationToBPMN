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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(BaseMergerImpl.class);

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
				// TODO: change behavior according to #77 / #78
				continue;
			}
			// retrieving a suitable parent
			Node parentNode = this.getSuitableParent(nestedTask);
			// inserting the new task
			this.insertNewTask(parentNode, nestedTask);
		}
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
		String logMsg;
		List<Node> tasksNodes = XMLManager.getTasksList(getDocument(), FeatureNames.SELECTOR);
		for (Node taskNode : tasksNodes) {
			Node namedItem = taskNode.getAttributes().getNamedItem(FeatureAttributes.NAME.getName());
			if ((namedItem != null) && namedItem.getNodeValue().equals(globalNodeName)) {
				// aldready exists
				logger.debug("Instances node found.");
				return taskNode;
			}
		}
		// create the node
		logger.debug("Instances node not found.");
		logger.debug("Starting creation...");
		Element instancesNode = getDocument().createElement(FeatureNames.AND.getName());
		instancesNode.setAttribute(FeatureAttributes.NAME.getName(), globalNodeName);
		logMsg = String.format("Instances node created : %s", instancesNode.getNodeName());
		logger.debug(logMsg);
		logger.debug("Inserting at default position...");
		return getDocument().getElementsByTagName(FeatureNames.AND.getName()).item(1)
				.appendChild(instancesNode);
	}
}
