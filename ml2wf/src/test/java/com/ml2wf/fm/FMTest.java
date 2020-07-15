package com.ml2wf.fm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Only usefull to develop FMHelper
 * @author blay
 *
 */
class FMTest {

	@Test
	void test() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM="../samples/basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		List<String> features = fm.getFeatureNameList();
		assertTrue(fm.isFeature("Steps"));
		Node n = fm.extractFeature("Steps") ;
		assertTrue( n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"));
		assertTrue(fm.isDirectChildOf("Steps", "Training_step"));
		assertFalse(fm.isDirectChildOf("Steps", "train OC_SVM"));
		assertTrue(fm.isChildOf("Steps", "train OC_SVM"));
	}

	@Test
	void testConstraints() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM="../samples/basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		List<String> constraints =fm.getConstraintList();
		//System.out.println(constraints);
		assertTrue(constraints.contains("Stability=>ensureStability"));
		assertTrue(constraints.contains("Training_step=>MLAlgorithm"));
	}
}
