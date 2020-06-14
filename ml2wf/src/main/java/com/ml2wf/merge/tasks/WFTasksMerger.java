package com.ml2wf.merge.tasks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
import com.ml2wf.conventions.enums.bpmn.BPMNNodesNames;
import com.ml2wf.generation.InstanceFactory;
import com.ml2wf.merge.AbstractMerger;
import com.ml2wf.util.XMLManager;

/**
 * This class merges all given Workflow's tasks into a FeatureModel xml file.
 *
 * <p>
 *
 * It is an extension of the {@link AbstractMerger} base class.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see AbstractMerger
 *
 */
public class WFTasksMerger extends AbstractMerger {

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFTasksMerger.class);

	/**
	 * {@code FeatureModelMerger}'s default constructor.
	 *
	 * @param filePath the XML filepath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public WFTasksMerger(String filePath)
			throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	/**
	 * Merges the given <b>instantiated</b> Workflow with the {@link #fmDocument
	 * FeatureModel};
	 *
	 * <p>
	 *
	 * If {@code backUp} parameter is {@code true}, then the current source file
	 * will be backed up by the {@link #backUp()} method.
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
	 * @param filePath the WF file path.
	 * @param backUp   backs up the current {@link #getSourceFile()} or not
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws InvalidConstraintException
	 *
	 * @since 1.0
	 * @see Node
	 * @see InstanceFactory
	 *
	 */
	@Override
	public void mergeWithWF(boolean backUp, String filePath)
			throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		// TODO: improve method readability
		// backing up if required
		if (backUp) {
			super.backUp();
		}
		logger.info("Starting the importation...");
		// preprocessing the document
		Document wfDocument = XMLManager.preprocess(new File(filePath));
		// retrieving workflow's tasks
		List<Node> wfTasks = XMLManager.getTasksList(wfDocument, BPMNNodesNames.SELECTOR);
		this.processTasks(wfTasks);
		this.processAnnotations(wfDocument);
		// saving result
		super.save(super.getPath());
	}

	/**
	 * Calls the {@link #mergeWithWF(String, boolean)} with {@code backUp} parameter
	 * as {@code false}.
	 *
	 * @param filePath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws InvalidConstraintException
	 */
	public void mergeWithWF(String filePath)
			throws ParserConfigurationException, SAXException, IOException, TransformerException,
			InvalidConstraintException {
		this.mergeWithWF(false, filePath);
	}

}
