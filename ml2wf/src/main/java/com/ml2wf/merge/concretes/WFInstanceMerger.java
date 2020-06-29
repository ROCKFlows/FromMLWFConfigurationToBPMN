package com.ml2wf.merge.concretes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.constraints.factory.ConstraintFactoryImpl;
import com.ml2wf.conventions.Notation;
import com.ml2wf.conventions.enums.bpmn.BPMNNames;
import com.ml2wf.conventions.enums.fm.FMNames;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.tasks.BPMNTask;
import com.ml2wf.tasks.FMTask;
import com.ml2wf.tasks.manager.TasksManager;
import com.ml2wf.util.Pair;
import com.ml2wf.util.XMLManager;

/**
 * This class merges a given Workflow's instance into a FeatureModel xml file.
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
public class WFInstanceMerger extends BaseMergerImpl {

	/**
	 * Instances default task tag name.
	 */
	private static final String INSTANCES_TASK = "Instances";
	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFInstanceMerger.class);

	/**
	 * {@code WFInstanceMerger}'s default constructor.
	 *
	 * @param file the XML {@code File}
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public WFInstanceMerger(File file) throws ParserConfigurationException, SAXException, IOException {
		super(file);
	}

	/**
	 * Returns a suitable parent for the {@code Node task} according to its
	 * specified <b>reference</b>.
	 *
	 * <p>
	 *
	 * Creates the referred parent node if not exists.
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
	 *
	 * @since 1.0
	 */
	@Override
	public FMTask getSuitableParent(BPMNTask task) {
		// retrieving the references parent
		String reference = task.getReference();
		if (reference.isBlank()) {
			// if contains a documentation node that can refer to a generic task
			Optional<FMTask> optRef = TasksManager.getFMTaskWithName(reference);
			return optRef.orElseGet(() -> this.createReferred(reference));
		}
		return unmanagedTask;
	}

	@Override
	public FMTask getRootParentNode() {
		System.out.println("getRootParentNode");
		return this.getGlobalFMTask(INSTANCES_TASK);
	}

	@Override
	public void processSpecificNeeds(Pair<String, Document> wfInfo) throws InvalidConstraintException {
		Document wfDocument = wfInfo.getValue();
		logger.debug("Specific need : meta reference.");
		String metaReferrence = this.getMetaReferenced(wfDocument);
		if (metaReferrence == null) {
			logger.error("No referenced meta-workflow.");
			logger.info("Make sure to use instance-Workflows using the generate command before merging/building.");
			logger.error("Skipping...");
			return;
		}
		String associationConstraint = ((ConstraintFactoryImpl) super.getConstraintFactory())
				.getAssociationConstraint(wfInfo.getKey(), Arrays.asList(metaReferrence));
		this.adoptRules(this.getConstraintFactory().getRuleNodes(associationConstraint));
		this.addReferences(wfDocument);
	}

	private FMTask createReferred(String reference) {
		FMTask newParent = this.createFeatureWithName(reference);
		return this.getGlobalFMTask(WFMetaMerger.STEP_TASK).appendChild(newParent);
	}

	/**
	 * Returns the referenced metaworkflow's name.
	 *
	 * @param wfDocument document containing the reference
	 * @return the referenced metaworkflow's name
	 *
	 * @since 1.0
	 */
	private String getMetaReferenced(Document wfDocument) {
		NodeList docNodes = wfDocument.getElementsByTagName(BPMNNames.DOCUMENTATION.getName());
		if (docNodes.getLength() > 0) {
			Node docNode = docNodes.item(0);
			// TODO: improve verification
			return XMLManager.getReferredTask(docNode.getTextContent());
		}
		return null;
	}

	/**
	 * Adds a description {@code Node} to the current {@code createdWFNode}
	 * containing all references (meta-workflow, dataset, author/articleà
	 *
	 * @param wfDocument document containing the references
	 *
	 * @since 1.0
	 */
	private void addReferences(Document wfDocument) {
		logger.debug("Adding references...");
		// getting global annotation content
		Node globalAnnotation = getGlobalAnnotationNode(wfDocument);
		String references = globalAnnotation.getTextContent();
		// removing WF's name
		// and references delimiters
		// TODO: remove WF's name part
		references = references.replace(Notation.getReferencesDelimiterLeft(), "");
		references = references.replace(Notation.getReferencesDelimiterRight(), "");
		// getting/creating the createdWFNode description
		Node descNode = this.createTag(createdWFTask, FMNames.DESCRIPTION);
		// merging content with description
		mergeNodesTextContent(descNode, references);
	}

}
