package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

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
public class TestSamplesSave {

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesSave.class);

	private static final String FM_IN_PATH = "./src/test/resources/ForValidationTests/feature_models/";
	private static final String metaWF_IN_PATH = "./src/test/resources/ForValidationTests/wf_meta/";
	private static final String instanceWF_IN_PATH = "./src/test/resources/ForValidationTests/wf_instances/";

	private static final String FM_OUT_PATH = "./target/generated/FM_S/";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Samples about SAVE!");
	}

	@AfterEach
	public void clean() {
	}

	
	//Add test on abstract and concrete Features
	@Test
	@DisplayName("T0 : Test with only one basic meta workflow and one instance wf adding one Step")
	public void testT0UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF.bpmn2";
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		
		String instanceWFPATH = instanceWF_IN_PATH + "BasicWF_instance00.bpmn2";
		
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		String copiedFM = FM_OUT_PATH + "basicFM_0.xml";
		TestHelper.copyFM(sourceFM, copiedFM);


		FMHelper fmBefore = FMHelper.createFMHelper(sourceFM);
		// Command
		String[] command = commandSave(metaWFPATH, instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, Arrays.asList(metaWFPATH, instanceWFPATH),command);
		String logMsg = String.format("added features in SAV : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		
		// Specific properties
		// assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		
		assertTrue(fmAfter.isFeature("Meta"));
		assertTrue(fmAfter.isFeature("BasicMetaWF"));
		assertTrue(fmAfter.isDirectChildOf("Meta", "BasicMetaWF"));
		
		assertTrue(fmAfter.isFeature("Instances"));
		assertTrue(fmAfter.isFeature("BasicWF_instance00"));
		assertTrue(fmAfter.isDirectChildOf("Instances", "BasicWF_instance00"));
		
		
		  List<String> constraints = fmAfter.getConstraintList(); logMsg =
		  String.format("Constraints : %s ", constraints); logger.debug(logMsg);
		  System.out.println(logMsg);
		  TestHelper.testConstraintImpliesAnd(constraints,"BasicMetaWF",Arrays.asList(
		  "Training_step", "Evaluating_step", "Preprocessing_step"));

		  TestHelper.testConstraintImpliesAnd(constraints,"BasicWF_instance00",Arrays.asList(
				  "Normalize", "Train_OC_SVM", "Evaluating_10Fold"));
				  
		// TODO test abstract Features
		// TODO test generated constraints

		TestHelper.checkIdempotence(copiedFM, command);

	}
	
	
	//TODO Don't forget to test optionnal; empty instances; empty meta; ect.
	
	
      	// ToFIX Unmanaged is generated
	    //Non consistent FM is generated
		@Test
		@DisplayName(" T1 : Test with a basic workflow adding one Step")
		public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
			String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF2.bpmn2";
			File fin = new File(metaWFPATH);
			assertTrue(fin.exists());
			
			String instanceWFPATH = instanceWF_IN_PATH + "BasicWF_instance00.bpmn2";
			
			String sourceFM = FM_IN_PATH + "basicFM.xml";
			String copiedFM = FM_OUT_PATH + "basicFM_1.xml";
			TestHelper.copyFM(sourceFM, copiedFM);


			FMHelper fmBefore = FMHelper.createFMHelper(sourceFM);
			// Command
			String[] command = commandSave(metaWFPATH, instanceWFPATH, copiedFM);
			FMHelper fmAfter = new FMHelper(copiedFM);

			// General Properties to check
			List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, Arrays.asList(metaWFPATH, instanceWFPATH),command);
			logMessageAfter(afterList);
			

			// Specific properties
			// assertEquals(1, afterList.size());
			assertTrue(fmAfter.isFeature("Evaluating_step"));
			assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
			// TODO test abstract Features
			// TODO test generated constraints
			// FIX Unmanaged is generated
			//TODO test relations and constraints
			//Don't forget to test optionnal; empty instances; empty meta; ect.
			TestHelper.checkIdempotence(copiedFM, command);

		}
		

	private String[] commandSave(String metaWFPATH, String instanceWFPATH, String copiedFM) {
		String[] command = new String[] { "save", "-i ", metaWFPATH, instanceWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}

	/**
	 * THis test leads to a valid FM because Missing_value is not abstract, and we
	 * don't forbid to select several algorithms in the same class.
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */

	@Test
	@DisplayName("TOFIX ABSTRACT :BasicHierarchie : Test with a hierarchic workflow adding one Step")
	public void testBasicHierachieSampleUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {

		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie.bpmn2";
		String instanceWFPATH = instanceWF_IN_PATH + "instanceBasicHierarchie.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";

		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "basicFM_Hierarchie.xml";
		TestHelper.copyFM(sourceFM, copiedFM);
		
		/*
		 * File sourceFile = new File(sourceFM); FileUtils.copyFile(sourceFile,
		 * copiedFile); File fin = new File(metaWFPATH); assertTrue(fin.exists());
		 * assertTrue(copiedFile.exists());
		 */
		

		FMHelper fmBefore = FMHelper.createFMHelper(instanceWFPATH);
		String[] command = commandSave(metaWFPATH, instanceWFPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, Arrays.asList(metaWFPATH, instanceWFPATH),command);
		logMessageAfter(afterList);
		
		//META -  #Preprocessing_step#Missing_Values + Training_step 
		//Instance Missing_Values_0 + Traing_step_1
		
		assertTrue(fmAfter.isChildOf("Missing_Values", "Missing_Values_0"));
		assertTrue(fmAfter.isChildOf("Training_step", "Training_step_1"));
		assertTrue(fmAfter.isAbstract("Missing_Values"));
		//TOFIX
		//assertFalse(fmAfter.isAbstract("Missing_Values_0"));
		//assertFalse(fmAfter.isAbstract("Training_step_1"));

		
		// Specific properties
		// assertEquals(1, afterList.size());
		// TODO test abstract Features
		// TODO test generated constraints
		// FIX
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);

	}

	private void logMessageAfter(List<String> afterList) {
		String logMsg = String.format("************** added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
	}
	
	
	//TODO : save sans instance = merge --meta -f (si je ne dis pas de bêtise)
			
			

	// TODO : Test with optional tasks present and absent in the WF instance; verify
	// that the FM is still consistent.
}