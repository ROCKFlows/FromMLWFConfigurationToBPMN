package com.ml2wf.merge;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.ml2wf.constraints.InvalidConstraintException;
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
public abstract class AbstractMerger extends XMLManager {

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
	}

	public abstract void mergeWithWF(String filePath, boolean backUp) throws ParserConfigurationException, SAXException,
			IOException, TransformerException, InvalidConstraintException;

}
