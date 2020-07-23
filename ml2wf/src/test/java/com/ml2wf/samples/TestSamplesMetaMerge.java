package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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
public class TestSamplesMetaMerge {

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesMetaMerge.class);

	private static final String FM_OUT_PATH = "./target/generated/FM/";
	private static final String FM_IN_PATH = "./src/test/resources/ForValidationTests/feature_models/";
	private static final String metaWF_IN_PATH = "./src/test/resources/ForValidationTests/wf_meta/";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Samples about meta Merge!");
	}

	@AfterEach
	public void clean() {
	}

	@Test
	@DisplayName("T0 : Test with a basic workflow adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_0.xml";
		TestHelper.copyFM(sourceFM, copiedFM);



		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		// List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logger.debug("added features : %s ", afterList);
		// All added feature should be abstract
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		// Specific properties
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		// TODO test abstract Features

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);

	}

	private String[] commandMerge(String metaWFPATH, String copiedFM) {
		String[] command = new String[] { "merge", "--meta", "-i ", metaWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}

	@Test
	@DisplayName("T1 : Test with a basic workflow adding one Step and one constraint")
	public void testBasicWFWithOneConstraintUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFWithConstraint2.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_1.xml";
		TestHelper.copyFM(sourceFM, copiedFM);



		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);

		// Specific properties
		//Evaluating_Step
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));

		fmAfter.getConstraintList().contains("Training_step=>Evaluating_step");

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	@Test
	@DisplayName("T2 : Test with a basic workflow adding one Step, one criteria and one constraint")
	public void testBasicWFWithOneContraintAddingACriteriaUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFWithConstraint.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_2.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		List<String> tasks = TestHelper.getTasks(afterList,fmAfter);
		TestHelper.allTheseFeaturesAreAbstract(tasks, fmAfter);

		// Specific properties
		//Evaluation_criteria and Unmanaged and Unmanaged_features
		assertEquals(4, afterList.size());
		assertEquals(1, tasks.size());
		// Evaluating_step => Evaluation_criteria I was expecting Evaluation_criteria to
		// be added as an unmanaged Feature
		// assertEquals(2, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		assertTrue(fmAfter.isFeature("Evaluation_criteria"));
		// TODO Test position of criteria to be improved
		assertTrue(fmAfter.isChildOf(TestHelper.UNMANAGED_FEATURES, "Evaluation_criteria"));
		assertTrue(fmAfter.getConstraintList().contains("Evaluating_step=>Evaluation_criteria"));

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	@Test
	@DisplayName("T3 : Test with a basic workflow adding two Steps")
	public void testAddingTwoStepsUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF2.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_3.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);


		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);

		// Specific properties

		assertEquals(2, afterList.size());
		String f1 = "Evaluating_step";
		String f2 = "Acquire_Metadata";
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(f2));
		assertTrue(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isDirectChildOf("Steps", f2));

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);

	}

	@Test
	@DisplayName("T4 : Test with a basic workflow adding One hierarchic Step")
	public void testAddingOneHierarchicStepUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_4.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		//System.out.println(logMsg);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);

		// Specific properties
		assertEquals(1, afterList.size());
		String parent = "Preprocessing_step";
		String f1 = "Missing_Values";
		assertTrue(fmBefore.isFeature(parent));
		assertFalse(fmBefore.isFeature(f1));
		assertTrue(fmAfter.isFeature(f1));
		// Test SubFeature
		assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf(parent, f1));

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	@Test
	@DisplayName("T5 : Test with a basic workflow adding One hierarchic Step at two levels")
	public void testAddingMultipleHierarchicStepUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie2.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_5.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		//System.out.println(logMsg);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		
	

		// Specific properties

		assertEquals(3, afterList.size());
		String otherParent = "Preprocess_data";
		String f1 = "Missing_Values";
		assertFalse(fmBefore.isFeature(f1));
		assertFalse(fmBefore.isFeature(otherParent));
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(otherParent));

		assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", otherParent));
		assertTrue(fmAfter.isChildOf(otherParent, f1));

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}


	//ToFIX:  A clearer error message is expected
	@Test
	@DisplayName("ToFIX:T6#Conflict : A same task as a subtask of several meta task ")
	public void testConflict4aTaskclassifiedAtTwoDifferentPlacesUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie3.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_6.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		// Specific properties
	
		assertEquals(3, afterList.size());
		String parent = "Preprocessing_step";
		String otherParent = "Preprocess_data";
		String f1 = "Missing_Values";
		assertTrue(fmBefore.isFeature(parent));
		assertFalse(fmBefore.isFeature(f1));
		assertFalse(fmBefore.isFeature(otherParent));
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(otherParent));
		// FIX
		// AN error should be raised. 
		//f1 can't be a child of parent and other parent and we have no ways to choose
		//assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		//assertTrue(fmAfter.isChildOf("Steps", f1));
		//assertTrue(fmAfter.isChildOf("Steps", otherParent));
		//assertTrue(fmAfter.isChildOf(parent, f1));
		// assertTrue(fmAfter.isChildOf(otherParent, f1));
		// Not SURE for this one
		// assertTrue(fmAfter.isChildOf(parent, otherParent));

		// Check idempotence
		//TestHelper.checkIdempotence(copiedFM, command);
	}

	// ToFIX:  A clearer error message is expected
	@Test
	@DisplayName("ToFIX: T7 : Test Merge when input WF file doesn't exist")
	public void testGenerationWithNoWFInputFile()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchieX.bpmn2";
		File fin = new File(metaWFPATH);
		assertFalse(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_7.xml";
		TestHelper.copyFM(sourceFM, copiedFM);

		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		
		// Check idempotence : nothing's happening
		 TestHelper.checkIdempotence(copiedFM, command);

	}

	// ToFIX:  An error message is expected
	// ToFIX: The generated FM is corrupted
	// 
	@Test
	@DisplayName("ToFIX:T8 : Test Merge when input FM file doesn't exist")
	public void testGenerationWithNoFMInputFile()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie2.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String copiedFM = FM_OUT_PATH + "basicFM_8.xml";

		// Command
		commandMerge(metaWFPATH, copiedFM);

	}

	@Test
	@DisplayName("T9 : Test Merge with a complex workflow")
	public void testGenerationWithCompletExample()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		
		String metaWFPATH = metaWF_IN_PATH + "FeatureBasedMetaWF.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_9.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		//System.out.println(logMsg);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		

		// No Task are lost
		TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);

		// Ensure all new Features corresponding to Tasks are Steps
		for (String f : afterList) {
			assertTrue(fmAfter.isChildOf("Steps", f));
		}

		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);

	}

	@Test
	@DisplayName("T10 : Test Merge that should do nothing")
	public void testUselessMerge()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String sourceFM = FM_IN_PATH + "basicFM_SAVE0.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_10.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = new FMHelper(sourceFM);
		// Command
		String[] command = commandMerge(metaWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);
		

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		//System.out.println(logMsg);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		

		// No Task are lost
		TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);

		logMsg = String.format("No Features should be added : %s", afterList);
		logger.debug(logMsg);
		//System.out.println(logMsg);
		assertTrue(afterList.isEmpty());
		

	}

}
