package com.ml2wf.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final String FM_IN_PATH = "./src/test/resources/ForValidationTests/feature_models/";

	private static final Logger logger = LogManager.getLogger(FMTest.class);
	@Test
	void test() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM= FM_IN_PATH +"basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		List<String> features = fm.getFeatureNameList();
		//System.out.println(features);
		assertTrue(fm.isFeature("Steps"));
		Node n = fm.extractFeature("Steps") ;
		assertTrue( n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"));
		assertTrue(fm.isDirectChildOf("Steps", "Training_step"));
		assertFalse(fm.isDirectChildOf("Steps", "Train_OC_SVMM"));
		assertTrue(fm.isChildOf("Steps", "Train_OC_SVM"));
	}
	
	@Test
	void testChildren() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM= FM_IN_PATH +"basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		List<String> features = fm.getFeatureNameList();
		//System.out.println(features);
		assertTrue(fm.isFeature("Steps"));
		List<String> list = fm.getChildren("Steps");
		String logMsg = String.format("list of children of Steps %s", list);
		System.out.println(logMsg);
		logger.debug(logMsg);
		assertEquals(4,list.size());
		list = fm.getChildren("Tools");
		logMsg = String.format("list of children of Tools %s", list);
		System.out.println(logMsg);
		logger.debug(logMsg);
		assertEquals(1,list.size());
		list = fm.getChildren("Yacine");
		logMsg = String.format("list of children of Yacine %s", list);
		System.out.println(logMsg);
		logger.debug(logMsg);
		assertEquals(0,list.size());
	}

	@Test
	void testConstraints() throws ParserConfigurationException, SAXException, IOException {
		String sourceFM= FM_IN_PATH +"basicFM.xml";
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
		String sourceFM= FM_IN_PATH +"basicFM.xml";
		File ffm = new File(sourceFM);
		assertTrue(ffm.exists());
		FMHelper fm = new FMHelper(sourceFM);
		assertTrue(fm.isAbstract("Steps"));
		assertFalse(fm.isAbstract("Stability"));
	}
}
