package com.ml2wf.merge;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * This class merges a given Workflow's instance into a FeatureModel xml file.
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
public class WFInstanceMerger extends AbstractMerger {

	public WFInstanceMerger(String filePath) throws ParserConfigurationException, SAXException, IOException {
		super(filePath);
	}

	@Override
	public void mergeWithWF(String filePath, boolean backUp) {
		// TODO Auto-generated method stub

	}

}
