package com.ml2wf.merge;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactory;
import com.ml2wf.constraints.factory.ConstraintFactoryImpl;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.conventions.enums.fm.FeatureModelAttributes;
import com.ml2wf.conventions.enums.fm.FeatureModelNames;
import com.ml2wf.util.XMLManager;

/**
 * This class contains useful methods for merging a Workflow element into a
 * FeatureModel xml file.
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
 * It is an implementation of the {@link WFMerger} interface.
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
 * @see XMLManager
 *
 */
public abstract class AbstractMerger extends XMLManager implements WFMerger {

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(AbstractMerger.class);
	/**
	 * {@code ConstraintFactory}'s instance that will generate constraint nodes.
	 *
	 * @see ConstraintFactory
	 */
	private ConstraintFactory constraintFactory;

	/**
	 * {@code AbstractMerger}'s default constructor.
	 *
	 * @param filePath the XML filepath.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public AbstractMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
		this.constraintFactory = new ConstraintFactoryImpl();
	}

	/**
	 * Returns the {@code ConstraintFactory}'s instance.
	 *
	 * @return the {@code ConstraintFactory}'s instance
	 *
	 * @see ConstraintFactory
	 */
	public ConstraintFactory getConstraintFactory() {
		return this.constraintFactory;
	}

	@Override
	public abstract void mergeWithWF(String filePath, boolean backUp) throws ParserConfigurationException, SAXException,
			IOException, TransformerException, InvalidConstraintException;

	/**
	 * Creates the <b>constraints</b> {@code Node} if it doesn't exist.
	 *
	 * @return the constraints {@code Node}
	 *
	 * @since 1.0
	 * @see Node
	 */
	protected Node createConstraintTag() {
		NodeList nodeList = this.getDocument().getElementsByTagName(FeatureModelNames.CONSTRAINTS.getName());
		if (nodeList.getLength() > 0) {
			// aldready exists
			return nodeList.item(0);
		} else {
			Node consTag = this.getDocument().createElement(FeatureModelNames.CONSTRAINTS.getName());
			NodeList fmTagList = this.getDocument().getElementsByTagName(FeatureModelNames.FEATUREMODEL.getName());
			if (fmTagList.getLength() == 0) {
				// if it is an ExtendedFeatureModel
				fmTagList = this.getDocument().getElementsByTagName(FeatureModelNames.EXTENDEDFEATUREMODEL.getName());
			}
			Node rootNode = fmTagList.item(0);
			return rootNode.appendChild(consTag);
		}
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
	protected Node getSuitableParent(Node task) {
		String debugMsg = String.format("Getting location for task : %s", task.getNodeName());
		logger.debug(debugMsg);
		// retrieving the references parent
		Node docNode = ((Element) task).getElementsByTagName(BPMNNodesNames.DOCUMENTATION.getName()).item(0);
		if (docNode != null) {
			// if contains a documentation node
			// that can refer to a generic task
			// retrieving all candidates
			List<Node> candidates = XMLManager.getTasksList(super.getDocument(), FeatureModelNames.SELECTOR);
			// electing the good candidate
			String candidateName;
			for (Node candidate : candidates) {
				debugMsg = String.format("	Processing candidate %s...", candidate.getTextContent());
				logger.debug(debugMsg);
				candidateName = XMLManager.getNodeName(candidate);
				if (candidateName.equals(docNode.getTextContent().replace(Notation.getReferenceVoc(), ""))) {
					return candidate;
				}
			}
		}
		debugMsg = String.format("No suitable parent was found for task %s.", task.getTextContent());
		logger.warn(debugMsg);
		logger.warn("Putting task at default location.");
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
	protected void insertNewTask(Node parentNode, Node task) {
		String debugMsg = String.format("Inserting task : %s", task.getTextContent());
		logger.debug(debugMsg);
		// retrieving task name content
		String taskName = XMLManager.getNodeName(task).replaceFirst(Notation.getGeneratedPrefixVoc(), "");
		String[] cleanedTaskName = taskName.split(Notation.getGeneratedPrefixVoc());
		// converting task name to the new node name
		String newNodeName = (cleanedTaskName.length > 1) ? cleanedTaskName[1] : taskName;
		debugMsg = String.format("task's name : %s", newNodeName);
		logger.debug(debugMsg);
		// inserting the new node
		Element newNode = super.getDocument().createElement(FeatureModelNames.FEATURE.getName());
		newNode.setAttribute(FeatureModelAttributes.NAME.getName(), newNodeName);
		parentNode.appendChild(newNode);
		logger.debug("Task inserted.");
	}

	/**
	 * Returns whether the given instantiated task's name is duplicated or not.
	 *
	 * <p>
	 *
	 * An instantiated task's name that is already in the document
	 * is considered as duplicated.
	 *
	 * <p>
	 *
	 * <b>Note</b> that an instantiated task's name is in the form :
	 *
	 * <p>
	 *
	 * <pre>
	 * <code>Notation.getGeneratedPrefixVoc() + genericTaskName + Notation.getGeneratedPrefixVoc() + newTaskName</code>
	 * </pre>
	 *
	 * @param task the instantiated task's name
	 * @return whether the given {@code task} is duplicated or not
	 */
	protected boolean isDuplicated(String instTaskName) {
		// split the instTaskName
		String[] s = instTaskName.split(Notation.getGeneratedPrefixVoc());
		// get all tasks
		List<Node> tasks = XMLManager.getTasksList(this.getDocument(), FeatureModelNames.SELECTOR);
		return XMLManager.getTasksNames(tasks).contains(s[2]);
	}

	/**
	 * Adopts and adds new {@code rules} to the FM document's constraint tag.
	 *
	 * @param rules {@code List} of {@code Node} rules to add to the FM document
	 *
	 * @since 1.0
	 *
	 * @see Node
	 */
	protected void adoptRules(List<Node> rules) {
		Node constraintsNode = this.createConstraintTag();
		for (Node rule : rules) {
			this.getDocument().adoptNode(rule);
			constraintsNode.appendChild(rule);
		}
	}

}
