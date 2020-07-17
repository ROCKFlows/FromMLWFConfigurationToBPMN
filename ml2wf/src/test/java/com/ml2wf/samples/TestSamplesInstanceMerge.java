package com.ml2wf.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

import com.ml2wf.TestHelper;
import com.ml2wf.generation.InstanceFactoryImpl;
import com.ml2wf.merge.base.BaseMergerImpl;
import com.ml2wf.merge.concretes.WFMetaMerger;
import com.ml2wf.util.FMHelper;
import com.ml2wf.util.Pair;


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
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Samples about Merge!");
	}

	@AfterEach
	public void clean() {
	}

	@Test
	@DisplayName("Test with a basic workflow instance adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String instanceWFPATH = "../BPMN-Models/BasicWF_instance00.bpmn2";
		String sourceFM="../samples/basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM= FM_OUT_PATH +"basicFMCopy.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(instanceWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--instance", "-i ", instanceWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		TestHelper.noFeatureLost(fmBefore, fmAfter);


		//TODO Improve to avoid double testing
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, instanceWFPATH);
		logger.debug("added features : %s ", afterList);
		//TODO  Test concrete and abstract features
		
		//FIX : I woul like a warning for Evaluating_Step
		assertEquals(3, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		assertTrue(fmAfter.isFeature("Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Evaluating_step", "Evaluating_10Fold"));
		assertTrue(fmAfter.isDirectChildOf("Preprocessing_step","Normalize"));
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	
	
	@Test
	@Disabled
	@DisplayName("Test with a basic workflow instance adding one Step and one constraint")
	public void testBasicWFWithOneConstraintUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFWithConstraint2.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.nothingLost(fmBefore, fmAfter, metaWFPATH);
		

		//FIX 
		//assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));

		fmAfter.getConstraintList().contains("Training_step=>Evaluating_step");

		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	
	@Test
	@Disabled
	@DisplayName("Test with a basic workflow instance adding one Step, one criteria and one constraint")
	public void testBasicWFWithOneContraintAddingACriteriaUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFWithConstraint.bpmn2";
		String sourceFM="../samples/basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_2.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
		

		//FIX 
		//Evaluating_step  =>  Evaluation_criteria I was expecting Evaluation_criteria to be added as an unmanaged Feature
		
		//assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
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
	@Disabled
	@DisplayName("Test with a basic workflow instance adding two Steps and completing an optional metatask")
	public void testAddingTwoStepsUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWF2.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
		
		//FIX 
		//assertEquals(2, afterList.size());
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
	@Disabled
	@DisplayName("Test with a basic workflow instance adding One hierarchic Step")
	public void testAddingOneHierarchicStepUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFHierarchie.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
				
		//FIX 
		//assertEquals(1, afterList.size());
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
	@Disabled
	@DisplayName("Test with a basic workflow adding One hierarchic Step at two levels")
	public void testAddingMultipleHierarchicStepUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFHierarchie2.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
		
		//
		//FIX 
		//assertEquals(1, afterList.size());
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
	@Disabled
	@DisplayName("#Conflict : A same task as a subtask of several meta task ")
	public void testConflict4aTaskclassifiedAtTwoDifferentPlacesUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFHierarchie3.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
		
		//
		//FIX 
		//assertEquals(1, afterList.size());
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
	

	@Test
	@Disabled
	@DisplayName("Test Merge when input WF file doesn't exist")
	public void testGenerationWithNoWFInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFHierarchieX.bpmn2";
		String sourceFM="../samples/basicFM.xml";
		//FIX avoid to make a copy
		String copiedFM=FM_OUT_PATH +"basicFMCopy_7.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(copiedFile.exists());
		
		
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		
		//Check idempotence : nothing's happening
		TestHelper.checkIdempotence(copiedFM, command);
		
		//com.ml2wf.App.main(command);
		//FIX there is one exception but it's catch...
		//assertThrows(NoSuchFileException.class, 
		//		() -> {com.ml2wf.App.main(command);} );
	}
	
	@Test
	@Disabled
	@DisplayName("Test Merge when input FM file doesn't exist")
	public void testGenerationWithNoFMInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWFHierarchie.bpmn2";
		//FIX avoid to make a copy
		String copiedFM="../samples/basicFMX.xml";
		File fin = new File(metaWFPATH);
		File copiedFile = new File(copiedFM);
		assertTrue(fin.exists());
		assertFalse(copiedFile.exists());
		
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", metaWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);

		
		//FIX there is one exception but it's catch...
		//assertThrows(NoSuchFileException.class, 
		//		() -> {com.ml2wf.App.main(command);} );

		
	}
	
	
	
	//todo: improve testing the content of the file.
	@Test
	@Disabled
	@DisplayName("Test Merge with a complex workflow")
	public void testGenerationWithCompletExample() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/FeatureBasedMetaWF.bpmn2";
		String sourceFM="../samples/basicFM.xml";
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
		TestHelper.noFeatureLost(fmBefore, fmAfter);
		
		//TODO Improve to avoid double testing
		List<String> afterList = TestHelper.checkNoFeaturesAreLost(fmAfter, fmBefore);
		logger.debug("added features : %s ", afterList);
		
		//
		//TODO : IMPROVE with a getTaskList
		//
		assertFalse(afterList.isEmpty());
		System.out.println(afterList.size() + " : " + afterList);
		logger.debug(afterList.size() + " : " + afterList);
		List<String> featuresToAdd=new ArrayList<>(Arrays.asList("Check_Model_Stability", "Spread_observations", "Compute_features", "Filter_confirmed_observations_from_training_data_?","Anotate_observations", "Set_$ml_model_default_param", "Validate_model", "Preprocess_data", "Select_data_for_training", "Acquire_Metadata", "Train_$ml_model", "Solve_labeling_conflicts", "Spread_confirmed_observations", "Compute_similarities", "Select_observations_to_anotate", "Preprocess_features", "Detect_with_$ml_model", "Fine_Tune_$ml_model_hyper_parameters"));
		//FIX removing Unmanaged
		//assertEquals(afterList.size(),featuresToAdd.size());
		afterList.removeAll(featuresToAdd);
		//FIX
		//assertTrue(afterList.isEmpty());
		System.out.println(afterList.size() + " : " + afterList);
		//FIX
		//Ensure all new Features corresponding to Tasks are Steps
		for (String f : featuresToAdd)
			assertTrue(fmAfter.isChildOf("Steps", f));
		//TODO Manage constraints
	
		
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
		
	
	}

	

}
