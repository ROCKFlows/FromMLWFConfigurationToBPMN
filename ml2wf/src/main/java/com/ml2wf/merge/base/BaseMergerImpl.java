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
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
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
			// TODO: factorize endsWith filter in a dedicated method
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
			List<Node> tasks = getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
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

	private void processCompleteMerge(Pair<String, Document> wfInfo) throws InvalidConstraintException {
		String wfName = wfInfo.getKey();
		// TODO: check order execution (node creation before assocConstraints)
		this.processAssocConstraints(wfInfo.getValue(), wfName);
		this.createdWFNode = this.createFeatureWithName(wfName);
		Node root = this.getRootParentNode();
		this.createdWFNode = (Element) this.insertNewTask(root, this.createdWFNode);
	}

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
}
