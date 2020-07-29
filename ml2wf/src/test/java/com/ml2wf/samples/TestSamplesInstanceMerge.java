package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

	private String[] mergeInstance(String instanceWFPATH, String copiedFM) {
		String[] command = new String[] { "merge", "--instance", "-i ", instanceWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}
	
	private void logAfterMessage(String testName, List<String> addedFeatures) {
		String logMsg = String.format("******* addedFeatures in %s : %s ", testName , addedFeatures);
		logger.debug(logMsg);
		System.out.println(logMsg);
	}
	

	
	@Test
	@DisplayName("T0 : Test with a basic workflow instance adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "BasicWF_instance00.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		// FIX avoid to make a copy
		String copiedFM = FM_OUT_PATH + "basicFMT0.xml";
	
		TestHelper.copyFM(sourceFM, copiedFM);
		

		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = this.mergeInstance(instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logAfterMessage("basicFMT0", addedFeatures);
		TestHelper.testAbstractAndConcreteFeatures(addedFeatures, instanceWFPATH, fmAfter);

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, instanceWFPATH);
		logger.debug("added features : %s ", afterList);

		// ToFIX : I woul like a warning for Evaluating_Step
		assertEquals(3, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		assertTrue(fmAfter.isFeature("Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Evaluating_step", "Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Preprocessing_step", "Normalize"));
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
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
		String[] command = this.mergeInstance(instanceWFPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logAfterMessage("FM1", addedFeatures);
		TestHelper.testAbstractAndConcreteFeatures(addedFeatures, instanceWFPATH, fmAfter);

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, instanceWFPATH);
		logAfterMessage("lost - FM1", afterList);
		assertTrue(afterList.isEmpty());
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// TODO test adding one constraint

	// TODO Test adding one criteria etc.

	// TODO Test with tasks not related to meta tasks

	// TODO test with tasks relatied to non existent metatasks

	// FIX an error
	@Test
	@DisplayName("ToFIX error FM2 : I lost idempotence by merging a WF with a task already identified as \"unmanaged\" ... the problem is serious because the result is very wrong!: Test with a basic workflow instance adding 3 Steps ")
	public void testAddingHierarchicStepsUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "instanceWF2.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "FM2.xml";
		TestHelper.copyFM(sourceFM, copiedFM);

		FMHelper fmBefore = new FMHelper(copiedFM); 
		String[] command = this.mergeInstance(instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check 
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter); 
		logAfterMessage("FM2",		  addedFeatures);

		assertEquals(7, addedFeatures.size(), "Training_step_1, Preprocessing_step_0, Unmanaged, Unmanaged_Tasks, Preprocess_data, Missing_value, Mean]"  ); 
		assertTrue(fmAfter.isChildOf("Steps", "Training_step_1"));
		assertFalse(fmAfter.isAbstract("Training_step_1"));
		assertTrue(fmAfter.isChildOf("Unmanaged", "Mean"));

		//toFix : Needed to see the resulting FM ! 
		String newFM = FM_OUT_PATH +
				"FM2_BeforeIdempotence.xml"; TestHelper.copyFM(copiedFM, newFM);

		//toFix : I lost Idempotence... and some tasks !! that are under the root !!
				//ToFix I try it step by Step
				System.out.println(" \n \n \n Checking Idempotence ");
				newFM = FM_OUT_PATH + "FM2_FORIdempotence.xml";
				TestHelper.copyFM(copiedFM, newFM);
				this.mergeInstance(instanceWFPATH, newFM);


				FMHelper fmAfterBIS = new FMHelper(newFM);
				//TODO : I Give up
				//addedFeatures = TestHelper.noFeatureLost( fmAfter, fmAfterBIS);
				//logAfterMessage("FM2 BIS", addedFeatures);
				//TestHelper.checkIdempotence(copiedFM, command);


	}
	  
	
	
	
	@Test
	@DisplayName("ToFIX error FM3 : Test with a basic workflow instance adding 2 instance steps")
	public void testAdding2InstanceStepsUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = WF_IN_PATH + "instanceBasicHierarchie.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		// FIX avoid to make a copy
		String copiedFM = FM_OUT_PATH + "FM3.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(instanceWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.mergeInstance(instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> addedFeatures = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logAfterMessage("FM3", addedFeatures);
		assertTrue(fmAfter.isChildOf("Missing_Values", "Missing_Values_0"));
		assertTrue(fmAfter.isChildOf("Training_step", "Training_step_1"));
		assertTrue(fmAfter.isAbstract("Missing_Values"));
		assertFalse(fmAfter.isAbstract("Training_step_1"));
		assertFalse(fmAfter.isAbstract("Missing_Values_0"));

		TestHelper.checkIdempotence(copiedFM, command);
	}


	

}
