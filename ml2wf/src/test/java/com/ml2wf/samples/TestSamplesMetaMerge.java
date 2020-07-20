package com.ml2wf.samples;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

import com.ml2wf.TestHelper;
import com.ml2wf.util.FMHelper;
import com.ml2wf.util.WFHelper;


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
	@DisplayName("Test with a basic workflow adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		//../samples/basicFMCopy.xml";
		String copiedFM= FM_OUT_PATH +"basicFMCopy.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
	
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		//List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logger.debug("added features : %s ", afterList);
		//All added feature should be abstract
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		//Specific properties
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		//TODO test abstract Features
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		
	}
	
	
	@Test
	@Disabled
	@DisplayName("Test with a basic workflow adding one Step and one constraint")
	public void testBasicWFWithOneConstraintUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BBasicMetaWFWithConstraint2.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_1.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//Specific properties
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));

		fmAfter.getConstraintList().contains("Training_step=>Evaluating_step");

		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	
	@Test
	@DisplayName("Test with a basic workflow adding one Step, one criteria and one constraint")
	public void testBasicWFWithOneContraintAddingACriteriaUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFWithConstraint.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_2.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		TestHelper.testDirectory(metaWF_IN_PATH, metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//Specific properties
		
		//FIX 
		//Evaluating_step  =>  Evaluation_criteria I was expecting Evaluation_criteria to be added as an unmanaged Feature
		//assertEquals(2, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		//assertTrue(fmAfter.isFeature("Evaluation_criteria"));
		//FIX test constraint : Evaluating_step => Evaluation_criteria
		//assertTrue(fmAfter.isFeature("Evaluation_criteria"));
		//FIX Test position of criteria to be improved
		//assertTrue(fmAfter.isChildOf("criteria", "Evaluation_criteria"));
		//assertTrue(fmAfter.getConstraintList().contains("Evaluating_step=>Evaluation_criteria"));

		//FIX 
		//Ensure a new feature has been added
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		//FIX Constraint is duplicated (check it) Evaluating_step  =>  Evaluation_criteria
	}
	
	
	
	@Test
	@DisplayName("Test with a basic workflow adding two Steps")
	public void testAddingTwoStepsUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF2.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_3.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//Specific properties
		
		//FIX 
		assertEquals(2, afterList.size());
		String f1 = "Evaluating_step";
		String f2 = "Acquire_Metadata";
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(f2));
		assertTrue(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isDirectChildOf("Steps", f2));
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		
	}
	
	@Test
	@DisplayName("Test with a basic workflow adding One hierarchic Step")
	public void testAddingOneHierarchicStepUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_4.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		
		//Specific properties
		assertEquals(1, afterList.size());
		String parent  = "Preprocessing_step";
		String f1 = "Missing_Values";
		assertTrue(fmBefore.isFeature(parent));
		assertFalse(fmBefore.isFeature(f1));
		assertTrue(fmAfter.isFeature(f1));
		//Test SubFeature
		assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf(parent, f1));
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	
	@Test
	@DisplayName("Test with a basic workflow adding One hierarchic Step at two levels")
	public void testAddingMultipleHierarchicStepUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie2.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_5.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		
		FMHelper fmAfter = new FMHelper(copiedFM);

		
		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//Specific properties
		 
		assertEquals(3, afterList.size());
		String otherParent = "Preprocess_data";
		String f1 = "Missing_Values";
		assertFalse(fmBefore.isFeature(f1));
		assertFalse(fmBefore.isFeature(otherParent));
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(otherParent));
		//FIX
		assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", otherParent));
		assertTrue(fmAfter.isChildOf(otherParent, f1));
		
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}


	
	@Test
	@DisplayName("#Conflict : A same task as a subtask of several meta task ")
	public void testConflict4aTaskclassifiedAtTwoDifferentPlacesUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie3.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_6.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		
		FMHelper fmAfter = new FMHelper(copiedFM);

		
		//General Properties to check
		List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		//System.out.println(afterList);
		//Specific properties
		//
		//FIX ABstract they are false..
		assertEquals(3, afterList.size());
		String parent  = "Preprocessing_step";
		String otherParent = "Preprocess_data";
		String f1 = "Missing_Values";
		assertTrue(fmBefore.isFeature(parent));
		assertFalse(fmBefore.isFeature(f1));
		assertFalse(fmBefore.isFeature(otherParent));
		assertTrue(fmAfter.isFeature(f1));
		assertTrue(fmAfter.isFeature(otherParent));
		//FIX
		assertFalse(fmAfter.isDirectChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", f1));
		assertTrue(fmAfter.isChildOf("Steps", otherParent));
		assertTrue(fmAfter.isChildOf(parent, f1));
		//assertTrue(fmAfter.isChildOf(otherParent, f1));
		//Not SURE for this one
		//assertTrue(fmAfter.isChildOf(parent, otherParent));
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	

	//TODO IMPROVE
	@Test
	@DisplayName("Test Merge when input WF file doesn't exist")
	public void testGenerationWithNoWFInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchieX.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_7.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		assertTrue(copiedFile.exists());
		
		
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		
		//Check idempotence : nothing's happening
		//TestHelper.checkIdempotence(copiedFM, command);
		
		//com.ml2wf.App.main(command);
		//FIX there is one exception but it's catch...
		//assertThrows(NoSuchFileException.class, 
		//		() -> {com.ml2wf.App.main(command);} );
	}
	
	//TODO IMPROVE
	@Test
	@DisplayName("Test Merge when input FM file doesn't exist")
	public void testGenerationWithNoFMInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie2.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFMX.xml";
		//FIX avoid to make a copy
		String copiedFM=sourceFM;
		File fin = new File(metaWFPATH);
		File copiedFile = new File(copiedFM);
		assertTrue(fin.exists());
		//assertFalse(copiedFile.exists());
		
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);

		
		//FIX there is one exception but it's catch...
		//assertThrows(NoSuchFileException.class, 
		//		() -> {com.ml2wf.App.main(command);} );

		
	}
	
	
	
	//todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Merge with a complex workflow")
	public void testGenerationWithCompletExample() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = metaWF_IN_PATH + "FeatureBasedMetaWF.bpmn2";
		String sourceFM= FM_IN_PATH + "basicFMX.xml";
		
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMComplex.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		
		FMHelper fmAfter = new FMHelper(copiedFM);

		
		//General Properties to check
		List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//No Task are lost
		TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);

	
		//Ensure all new Features corresponding to Tasks are Steps
		for (String f : afterList)
			assertTrue(fmAfter.isChildOf("Steps", f));
		//TODO Manage constraints
	
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		
	
	}
	

	//todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Merge that should do nothing")
	public void testUselessMerge() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = metaWF_IN_PATH +  "BasicMetaWF.bpmn2";
		//THe FM results from a save.
		String sourceFM=FM_IN_PATH +"basicFM_SAVE0.xml";
		
		
		
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"uselessMerge.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		
		FMHelper fmAfter = new FMHelper(copiedFM);

		
		//General Properties to check
		List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		logger.debug("added features : %s ", afterList);
		TestHelper.allTheseFeaturesAreAbstract(afterList, fmAfter);
		
		//No Task are lost
		TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);

		assertTrue(afterList.isEmpty());
		//Ensure all new Features corresponding to Tasks are Steps
		for (String f : afterList)
			assertTrue(fmAfter.isChildOf("Steps", f));
		//TODO Manage constraints
	
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		
	
	}
	

	

}
