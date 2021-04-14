package com.ml2wf.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class WFHelperTest {

	@Test
	void test() throws ParserConfigurationException, SAXException, IOException {
		String sourceWF = "../BPMN-Models/BasicMetaWF.bpmn2";
		File ffm = new File(sourceWF);
		assertTrue(ffm.exists());
		WFHelper wf = new WFHelper(sourceWF);
		List<String> tasks = wf.getTaskNamesList();
		assertEquals(3, tasks.size());
		// System.out.println(tasks);
		/*
		 * assertTrue(wf.isFeature("Steps")); Node n = wf.extractFeature("Steps") ;
		 * assertTrue(
		 * n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"))
		 * ; assertTrue(wf.isDirectChildOf("Steps", "Training_step"));
		 * assertFalse(wf.isDirectChildOf("Steps", "train OC_SVM"));
		 * assertTrue(wf.isChildOf("Steps", "Train OC_SVM"));
		 */
	}

	@Test
	void testOnInstanceWF() throws ParserConfigurationException, SAXException, IOException {
		String sourceWF = "../BPMN-Models/BasicWF_instance00.bpmn2";
		File ffm = new File(sourceWF);
		assertTrue(ffm.exists());
		WFHelper wf = new WFHelper(sourceWF);
		List<String> tasks = wf.getTaskNamesList();
		assertEquals(3, tasks.size());
		// System.out.println(tasks);
		/*
		 * assertTrue(wf.isFeature("Steps")); Node n = wf.extractFeature("Steps") ;
		 * assertTrue(
		 * n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"))
		 * ; assertTrue(wf.isDirectChildOf("Steps", "Training_step"));
		 * assertFalse(wf.isDirectChildOf("Steps", "train OC_SVM"));
		 * assertTrue(wf.isChildOf("Steps", "Train OC_SVM"));
		 */
	}

	@Test
	void testOnComplexWF() throws ParserConfigurationException, SAXException, IOException {
		String sourceWF = "../BPMN-Models/FeatureBasedMetaWF.bpmn2";
		File ffm = new File(sourceWF);
		assertTrue(ffm.exists());
		WFHelper wf = new WFHelper(sourceWF);
		List<String> tasks = wf.getTaskNamesList();
		// assertEquals(3, tasks.size());
		System.out.println(tasks);
		/*
		 * assertTrue(wf.isFeature("Steps")); Node n = wf.extractFeature("Steps") ;
		 * assertTrue(
		 * n.getAttributes().getNamedItem("name").getNodeValue().contentEquals("Steps"))
		 * ; assertTrue(wf.isDirectChildOf("Steps", "Training_step"));
		 * assertFalse(wf.isDirectChildOf("Steps", "train OC_SVM"));
		 * assertTrue(wf.isChildOf("Steps", "Train OC_SVM"));
		 */
	}

}
