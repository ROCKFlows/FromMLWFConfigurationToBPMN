package com.ml2wf.merge.base;

import java.io.IOException;
import java.util.List;

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
	 * @param filePath the FeatureModel file path
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public BaseMergerImpl(String filePath) throws ParserConfigurationException, SAXException, IOException {
		// TODO: change String filePath to Path filePath (or File)
		super(filePath);
	}

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, String filePath) throws Exception {
		if (backUp) {
			super.backUp();
		}
		Pair<String, Document> wfInfo = this.getWFDocInfoFromPath(filePath);
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

	@Override
	public void mergeWithWF(boolean backUp, boolean completeMerge, String... filesPath) throws Exception {
		for (String path : filesPath) {
			this.mergeWithWF(backUp, completeMerge, path);
		}
		// TODO
		/*-try (Stream<Path> paths = Files.walk(Paths.get("/home/you/Desktop"))) {
			paths
					.filter(Files::isRegularFile)
					.forEach(p -> this.mergeWithWF(backUp, p));
		}*/
	}

	private void processCompleteMerge(Pair<String, Document> wfInfo) throws InvalidConstraintException {
		String wfName = wfInfo.getKey();
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
