package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.ml2wf.TestHelper;
import com.ml2wf.util.FMHelper;

/**
 * @author blay
 *
 */
public class TestSamplesInstanceMerge {

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesInstanceMerge.class);

	private static final String FM_OUT_PATH = "./target/generated/FM_I/";
	private static final String FM_IN_PATH = "./src/test/resources/ForValidationTests/feature_models/";
	private static final String WF_IN_PATH = "./src/test/resources/ForValidationTests/wf_instances/";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Samples about Merge!");
	}

	@AfterEach
	public void clean() {
	}

	
	//ToFIX : The only problem lies in abstract features that should be concrete
	@Test
	@DisplayName("ToFIX ; T0 : Test with a basic workflow instance adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "BasicWF_instance00.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		// FIX avoid to make a copy
		String copiedFM = FM_OUT_PATH + "basicFMT0.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(instanceWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		// Command
		String[] command = mergeInstance(instanceWFPATH, copiedFM);
	
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);
		TestHelper.testAbstractAndConcreteFeatures(addedFeatures, instanceWFPATH, fmAfter);

		// TODO Improve to avoid double testing
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, instanceWFPATH);
		logger.debug("added features : %s ", afterList);

		// FIX : I woul like a warning for Evaluating_Step
		assertEquals(3, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		assertTrue(fmAfter.isFeature("Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Evaluating_step", "Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Preprocessing_step", "Normalize"));
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	private String[] mergeInstance(String instanceWFPATH, String copiedFM) {
		String[] command = new String[] { "merge", "--instance", "-i ", instanceWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}

	@Test
	@DisplayName("T1 : Test a merge that should do nothing")
	public void testUselessMergeUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "BasicWF_instance00.bpmn2";
		// THe FM results from a save.
		String sourceFM = FM_IN_PATH + "basicFM_SAVE0.xml";
		// FIX avoid to make a copy
		String copiedFM = FM_OUT_PATH + "basicFM_SAVE_Idempotence_with_merge_intance.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(instanceWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = mergeInstance(instanceWFPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);
		TestHelper.testAbstractAndConcreteFeatures(addedFeatures, instanceWFPATH, fmAfter);

		// TODO Improve to avoid double testing
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, instanceWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertTrue(afterList.isEmpty());
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// TODO test adding one constraint
	
	// TODO Test adding one criteria etc.

	// TODO Test with tasks not related to meta tasks
	
	// TODO test with tasks relatied to non existent metatasks
	
	
	// FIX an error is raised...
	// It should be solved managing hierarchies of tasks
	// I expect :  
	//Preprocessing_step#Preprocess_data#Missing_value#Mean
	//Preprocessing_step#Preprocessing_step0
	//Training_step_1 refersTo: Training_step
	@Test
	@DisplayName("ToFIX FM2 : Test with a basic workflow instance adding 3 Steps ")
	public void testAddingHierarchicStepsUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "instanceWF2.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		// FIX avoid to make a copy
		String copiedFM = FM_OUT_PATH + "FM2.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(instanceWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = mergeInstance(instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		
		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);

		String logMsg = String.format("addedFeatures: %s ", addedFeatures);
		logger.debug(logMsg);
		System.out.println(logMsg);
		
		//Preprocessing_step0 refersTo: Preprocessing_step
		//#Preprocess_data#Missing_value#Mean refersTo:Preprocessing STep
		//Training_step_1 refersTo: Training_step
		// TOFIX
		// TestHelper.testAbstractAndConcreteFeatures(addedFeatures,instanceWFPATH,
		// fmAfter);
		// FIX
		// assertEquals(2, afterList.size());
		/*
		 * String f1 = "Evaluating_step"; String f2 = "Acquire_Metadata";
		 * assertTrue(fmAfter.isFeature(f1)); assertTrue(fmAfter.isFeature(f2));
		 * assertTrue(fmAfter.isDirectChildOf("Steps", f1));
		 * assertTrue(fmAfter.isDirectChildOf("Steps", f2));
		 */

		// Check idempotence
		// TOFIX
		// TestHelper.checkIdempotence(copiedFM, command);
	}



}
