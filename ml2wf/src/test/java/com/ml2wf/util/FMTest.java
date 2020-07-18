package com.ml2wf.util;

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

import com.ml2wf.util.FMHelper;

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
		//System.out.println(features);
		assertTrue(fm.isFeature("Steps"));
		Node n = fm.extractFeature("Steps") ;
		assertTrue( n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"));
		assertTrue(fm.isDirectChildOf("Steps", "Training_step"));
		assertFalse(fm.isDirectChildOf("Steps", "train OC_SVM"));
		assertTrue(fm.isChildOf("Steps", "Train OC_SVM"));
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
	
	@Test
	void testIsAbstract() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM="../samples/basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		//System.out.println(constraints);
		assertTrue(fm.isAbstract("Steps"));
		assertFalse(fm.isAbstract("Stability"));
	}
}
