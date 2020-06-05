package com.ml2wf.merge;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.util.Pair;

public abstract class WFCompleteMerger extends AbstractMerger {

	/**
	 * Logger instance.
	 *
	 * @since 1.0
	 * @see Logger
	 */
	private static final Logger logger = LogManager.getLogger(WFCompleteMerger.class);

	public WFCompleteMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	@Override
	public void mergeWithWF(boolean backUp, String filePath) throws Exception {
		// this method is a template for the Template Method design pattern
		String logMsg;
		logger.info("Processing backUp argument...");
		if (backUp) {
			super.backUp();
		}
		Pair<String, Document> wfInfo = this.getWFDocInfoFromPath(filePath);
		if (wfInfo.isEmpty()) {
			logger.fatal("The given file path is invalid or the workflow is already contained in the FeatureModel...");
			logger.warn("Skipping...");
			// TODO: add logs
			return;
		}
		String wfName = wfInfo.getKey();
		logMsg = String.format("Current workflow's name : %s", wfName);
		logger.debug(logMsg);
		Document wfDocument = wfInfo.getValue();
		logger.debug("Creating associated task...");
		Element newNode = this.createFeatureWithName(wfName);
		logger.debug("Getting parent...");
		Node parent = this.getRootParentNode();
		logMsg = String.format("Parent is %s.", parent.getNodeValue());
		logger.debug(logMsg);
		logger.debug("Inserting task...");
		this.insertNewTask(parent, newNode);
		logger.debug("Processing annotations constraints...");
		this.processAssocConstraints(wfDocument, wfName);
		logger.debug("Processing specific needs...");
		this.processSpecificNeeds(wfDocument, wfName);
		logger.debug("Saving file...");
		super.save(super.getPath());
	}

	/**
	 * Returns the root parent {@code Node} according to the workflow's
	 * specifications.
	 *
	 * <p>
	 *
	 * e.g. : meta workflow, instance workflow...
	 *
	 * @return the root parent {@code Node} according to the workflow's
	 *         specifications
	 *
	 * @since 1.0
	 */
	protected abstract Node getRootParentNode();

	/**
	 * Processes other specific needs to complete the merge operation.
	 *
	 * @param wfDocument workflow's document
	 * @param wfName     workflow's name
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	protected abstract void processSpecificNeeds(Document wfDocument, String wfName) throws Exception;
}
