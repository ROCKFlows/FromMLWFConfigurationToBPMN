package com.ml2wf.merge.concretes;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

/**
 * This class merges a given MetaWorkflow into a FeatureModel xml file.
 *
 * <p>
 *
 * It is an extension of the {@link BaseMergerImpl} base class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see BaseMergerImpl
 *
 */
public class WFMetaMerger extends BaseMergerImpl {

	/**
	 * Meta default task tag name.
	 */
	private static final String META_TASK = "Meta";
	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFMetaMerger.class);

	public WFMetaMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	@Override
	public Node getSuitableParent(Node child) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node getRootParentNode() {
		return this.getMetaTask();
	}

	@Override
	public void processSpecificNeeds(Pair<String, Document> wfInfo) throws Exception {
		// adds new metatasks
		/*-List<Node> wfTasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		this.processTasks(wfTasks);*/
	}

	/**
	 * Returns the <b>meta</b> {@code Node} task.
	 *
	 * <p>
	 *
	 * Creates it if not exist.
	 *
	 * <p>
	 *
	 * @return the instances {@code Node} task
	 *
	 * @since 1.0
	 * @see Node
	 */
	private Node getMetaTask() {
		String logMsg;
		// TODO: factorize with a similar method in WFTasksMerger and the
		// WFInstanceMerger#getInstancesTask
		List<Node> tasksNodes = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
		for (Node taskNode : tasksNodes) {
			Node namedItem = taskNode.getAttributes().getNamedItem(FeatureModelAttributes.NAME.getName());
			if ((namedItem != null) && namedItem.getNodeValue().equals(META_TASK)) {
				// aldready exists
				logger.debug("Instances node found.");
				return taskNode;
			}
		}
		// create the node
		// TODO: create method in XMLManager and factorize with
		// AbstractMerger#createConstraintTag()
		logger.debug("Instances node not found.");
		logger.debug("Starting creation...");
		Element instancesNode = getDocument().createElement(FeatureModelNames.AND.getName());
		instancesNode.setAttribute(FeatureModelAttributes.NAME.getName(), META_TASK);
		logMsg = String.format("Instances node created : %s", instancesNode.getNodeName());
		logger.debug(logMsg);
		logger.debug("Inserting at default position...");
		return super.getDocument().getElementsByTagName(FeatureModelNames.AND.getName()).item(1)
				.appendChild(instancesNode);
	}

}
