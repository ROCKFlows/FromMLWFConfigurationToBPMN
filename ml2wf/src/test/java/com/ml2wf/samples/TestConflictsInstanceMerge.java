package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

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
public class TestConflictsInstanceMerge {

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestConflictsInstanceMerge.class);

	private static final String CONFLICT_SAMPLE_PATH = "./src/test/resources/ForValidationTests/onConflicts/";
	private static final String FM_IN_PATH = CONFLICT_SAMPLE_PATH + "feature_models/";
	private static final String DEFAULT_IN_FM = FM_IN_PATH + "FMA.xml";
	private static final String FM_OUT_PATH = "./target/generated/FM_CI/";
	private static final String WF_IN_PATH = CONFLICT_SAMPLE_PATH + "wf_instance/";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Conflicts in merge of instance WF!");
	}

	@AfterEach
	public void clean() {
	}

	@Test
	@DisplayName("T1 : just to ensure equivalence between refersTo and #")
	public void testWFInstance1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		// Get the wf instance
		String wfPATH = WF_IN_PATH + "WF1_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		// Copy the initial FM for test
		String sourceFM = DEFAULT_IN_FM;
		String copiedFMPath = FM_OUT_PATH + "FMA_WF1_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);

		// Call the merge
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));

		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);

		// ---- The same behavior is expected but the task refers_to it's name
		// #F31#F31_0
		wfPATH = WF_IN_PATH + "WF1_instance2.bpmn2";
		sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFMBis = FM_OUT_PATH + "FMA_WF1_i_bis.xml";
		TestHelper.copyFM(sourceFM, copiedFMBis);

		// Command
		command = new String[] { "merge", "--instance", "-i ", wfPATH, "-o ", copiedFMBis, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(new File(copiedFMBis).exists());
		fmAfter = new FMHelper(copiedFMBis);

		// General Properties to check
		afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		assertEquals(1, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));

	}

	private String[] commandMergeInstance(String wfPATH, String copiedFM) {
		// Command
		String[] command = new String[] { "merge", "--instance", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}

	// Todo : test the presence of a warning
	//ToFIX no conflict Detected
	@Test
	@DisplayName("ToFIX still Abstract +  Warning : refer to a task known as abstract")
	public void testWFInstance2UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF2_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF2_i.xml";
		TestHelper.copyFM(sourceFM, copiedFM);

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMergeInstance(wfPATH, copiedFM);

		assertTrue(new File(copiedFM).exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		// No features are added
		assertEquals(0, afterList.size());
		// FIX
		assertFalse(fmAfter.isAbstract("F31"));
	}

	// ToFIX : F32 should be concrete
	//ToFIX a new Bug
	@Test
	@DisplayName("ToFIX: still T3 : an unreferenced task is stored in the FM under Unmanaged")
	public void testWFInstance3UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF3_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFMPath = FM_OUT_PATH + "FMA_WF3_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		assertTrue(new File(copiedFMPath).exists());
		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);

		// Specific properties
		// Unmanaged, Unmanaged_Tasks and F32 are added
		assertEquals(3, afterList.size());
		// FIX
		assertFalse(fmAfter.isAbstract("F32"));
		assertTrue(fmAfter.isChildOf("Unmanaged", "F32"));
		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);
	}

	@Test
	@DisplayName("WF4 : to test introduction of a new criteria : F31_0 => criteria31_0 ")
	public void testWF4nstance1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		// Get the wf instance
		String wfPATH = WF_IN_PATH + "WF4_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		// Copy the initial FM for test
		String sourceFM = DEFAULT_IN_FM;
		String copiedFMPath = FM_OUT_PATH + "FMA_WF4_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);

		// Call the merge
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		// F31_0, Unmanaged, unmanagedCriteria, criteria
		assertEquals(4, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));
		// Test constraints
		List<String> constraints = fmAfter.getConstraintList();
		logMsg = String.format("constraints: %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(1, constraints.size());
		// TODO : improve tests

		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);
	}

	@Test
	@DisplayName("WF5 : to test introduction of a set of tasks refering to the same meta-task and a set of constraints between them : F31_0 => F31_1 and F31_1 => F31_2; ")
	public void testWF5nstance1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		// Get the wf instance
		String wfPATH = WF_IN_PATH + "WF5_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		// Copy the initial FM for test
		String sourceFM = DEFAULT_IN_FM;
		String copiedFMPath = FM_OUT_PATH + "FMA_WF5_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);

		// Call the merge
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		// F31_0 1 and 2
		assertEquals(3, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));
		assertTrue(fmAfter.isChildOf("F31", "F31_1"));
		assertTrue(fmAfter.isChildOf("F31", "F31_2"));
		// Test constraints
		List<String> constraints = fmAfter.getConstraintList();
		logMsg = String.format("constraints: %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(2, constraints.size());
		// TODO : improve tests

		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);
	}

	@Test
	/*
	 * [[ F31_0 => F31_1 ]]
	 * [[ F31_0 => F31_2 ]]
	 * [[ F32_0 => criteria32 ]]
	 * [[ criteria33 => F33_0 ]]
	 */
	@DisplayName("WF6 : to test constraints between non existing features ")
	public void testWF6nstance1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		// Get the wf instance
		String wfPATH = WF_IN_PATH + "WF6_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		// Copy the initial FM for test
		String sourceFM = DEFAULT_IN_FM;
		String copiedFMPath = FM_OUT_PATH + "FMA_WF6_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);

		// Call the merge
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		// F31_0 1 and 2 + + F32_0 + F33_0 criteria32 criteria33 and then unmanaged and
		// unmanaged_features
		assertEquals(9, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));
		assertTrue(fmAfter.isChildOf("F31", "F31_1"));
		assertTrue(fmAfter.isChildOf("F31", "F31_2"));
		// Test constraints
		List<String> constraints = fmAfter.getConstraintList();
		logMsg = String.format("constraints: %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(4, constraints.size());
		// TODO : improve tests

		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);
	}

	@Test
	@DisplayName("WF7 : to test a constraint appying on a task that doesn't exist: F31_0 => F31_4 (not exist) and F31_1 => F31_2; ")
	public void testWF7nstance1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		// Get the wf instance
		String wfPATH = WF_IN_PATH + "WF7_instance.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		// Copy the initial FM for test
		String sourceFM = DEFAULT_IN_FM;
		String copiedFMPath = FM_OUT_PATH + "FMA_WF7_i.xml";
		TestHelper.copyFM(sourceFM, copiedFMPath);

		FMHelper fmBefore = new FMHelper(copiedFMPath);

		// Call the merge
		String[] command = this.commandMergeInstance(wfPATH, copiedFMPath);

		FMHelper fmAfter = new FMHelper(copiedFMPath);

		// General Properties to check

		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Specific properties
		// F31_0 1 and 2 + 4 ? then unmanaged and unmanaged_features (the system can't
		// guess it was a task for me ! )
		assertEquals(6, afterList.size());
		assertTrue(fmAfter.isChildOf("F31", "F31_0"));
		assertTrue(fmAfter.isChildOf("F31", "F31_1"));
		assertTrue(fmAfter.isChildOf("F31", "F31_2"));
		// Test constraints
		List<String> constraints = fmAfter.getConstraintList();
		logMsg = String.format("constraints: %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(2, constraints.size());
		// TODO : improve tests

		// Check idempotence
		TestHelper.checkIdempotence(copiedFMPath, command);
	}

	// One feature is abstract and concret
	// e.g. a Meta declare Normalize and an instance uses also Normalize
	// ==> suggest user to specialize Normalize with a concrete task ?
	// e.g. Normalize is said concrete in FM
	// a Meta uses Normalize
	// ==> A warning is raised and Normalize becomes "abstract"?
}
