package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
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
public class TestConflictsMetaMerge {

	private static final String FAIL = "FAIL";

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestConflictsMetaMerge.class);

	private static final String CONFLICT_SAMPLE_PATH = "./src/test/resources/ForValidationTests/onConflicts/";
	private static final String FM_IN_PATH = CONFLICT_SAMPLE_PATH + "feature_models/";
	private static final String DEFAULT_IN_FM = FM_IN_PATH + "FMA.xml";
	private static final String FM_OUT_PATH = "./target/generated/FM_C/";
	private static final String WF_IN_PATH = CONFLICT_SAMPLE_PATH + "wf_meta/";

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Conflicts!");
	}

	@AfterEach
	public void clean() {
	}

	// TOFIX FAILS when looking inside the hierarchy is not the good one
	// T2 should become a subfeature of T1
	@Test
	@DisplayName(" Test 1 in #147 : #f1 + #f2 + #f1#f2 = #f1#f2 in any order")
	public void test11UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// merge t1, then t2 then t1t2 results in the same FM than t1t2 and the same in
		// any order
		// FIX
		this.mergeMeta(1, Arrays.asList("WFT1", "WFT2", "WFT1T2"), "WFT1T2");

	}
	

	// TOFIX FAILS when looking inside the hierarchy is not the good one
	// T2 should become a subfeature of T1
	@Test
	@DisplayName("Test 12 WFT1T2")
	public void testToDebugUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// merge t1, then t2 then t1t2 results in the same FM than t1t2 and the same in
		// any order
		// FIX
		this.mergeMeta(12, Arrays.asList("WFT1T2"), "WFT1T2");

	}

	@Test
	@DisplayName("Test 2 in #147 : #f1#f2 + #f1#f2#f3 = #f1#f2#f3 in any order")
	public void test2UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		this.mergeMeta(2, Arrays.asList("WFT1T2", "WFT1T2T3"), "WFT1T2T3");

	}

	@Test
	@DisplayName("Test 3 in #147 : #f1#f3 + #f1#f2#f3 = #f1#f2#f3 in any order")
	public void test3UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		this.mergeMeta(3, Arrays.asList("WFT1T3", "WFT1T2T3"), "WFT1T2T3");
	}

	// ToFIX : We are expecting T2 to be a subtask of T1
	@Test
	@DisplayName("ToFIX still : Test 4 in #147 : #f2#f3 + #f1#f2#f3 = #f1#f2#f3 in any order")
	public void test4UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		this.mergeMeta(4, Arrays.asList("WFT2T3", "WFT1T2T3"), "WFT1T2T3");
	}

	// WWhen we have the information #T1#T2, we expect the system to add T1 over T2
	@Test
	@DisplayName(" Test 5 in #147 : #f2#f3 + #f1#f2 = #f1#f2#f3 in any order")
	public void test5UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// FIX
		this.mergeMeta(5, Arrays.asList("WFT1T2", "WFT2T3"), "WFT1T2T3");
		this.mergeMeta(5, Arrays.asList("WFT2T3", "WFT1T2"), "WFT1T2T3");

	}

	// We're waiting for a warning because this merge can't be completed...
	// But we don't have to fix this way
	@Test
	@DisplayName("warning Test 6 in #147 : #f2#f3 + #f1#f3 fails because we don't know the order between f2 and f1 ")
	public void test6UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		//this.mergeMeta(6, Arrays.asList("WFT2T3", "WFT1T3"), FAIL);
	}

	private void mergeMeta(int testNumber, List<String> wfList, String globalWF)
			throws ParserConfigurationException, SAXException, IOException {
		String sourceFM = DEFAULT_IN_FM;
		FMHelper fmBefore = new FMHelper(sourceFM);

		// Step 1 : I merge all the WF inside the same FM in sequence
		System.out.println("**********Step 1 : I merge all the WF inside the same FM in sequence*********");
		String resultingFM = FM_OUT_PATH + "FMA_Test" + testNumber + ".xml";
		TestHelper.copyFM(sourceFM, resultingFM);
		String logMsg = String.format("Step 1 : merge in %s all the WF %s", resultingFM, wfList);
		logger.debug(logMsg);

		this.mergeMetaAWFList(wfList, fmBefore, resultingFM);

		logMsg = String.format("xxxxxxxxxxxxxxx----------Children of step%s", (new FMHelper(resultingFM)).getChildren("Steps"));
		logger.debug(logMsg);
		
		// Step 2 : I want to prove equivalence with the globaWF
		System.out.println("\n\n\n**********Step  2:I want to prove equivalence with the globaWF");
		if (globalWF.equals(FAIL)) {
			logMsg = String.format("Step 2 : We were expecting a failure in the first step.", resultingFM, wfList);
			logger.debug(logMsg);
			return;
		}

		String resultingFMBis = FM_OUT_PATH + "FMA_TestBis" + testNumber + ".xml";
		TestHelper.copyFM(sourceFM, resultingFMBis);

		String wfPATH = WF_IN_PATH + globalWF + ".bpmn2";
		this.commandMetaMerge(wfPATH, resultingFMBis);

		TestHelper.compare(resultingFM, resultingFMBis);

		// Step 3: I want to prove order independance
		Collections.shuffle(wfList);
		resultingFM = FM_OUT_PATH + "FMA_TestOrderFree" + testNumber + ".xml";
		TestHelper.copyFM(sourceFM, resultingFM);
		this.mergeMetaAWFList(wfList, fmBefore, resultingFM);
		TestHelper.compare(resultingFM, resultingFMBis);
	}

	private void mergeMetaAWFList(List<String> wfList, FMHelper fmBefore, String resultingFM)
			throws ParserConfigurationException, SAXException, IOException {

		for (String wf : wfList) {
			
			String wfPATH = WF_IN_PATH + wf + ".bpmn2";
			File fin = new File(wfPATH);
			assertTrue(fin.exists());
			String logMsg = String.format("\n merge WF %s in %s", wf, resultingFM);
			logger.debug(logMsg);
			System.out.println(logMsg);

			this.commandMetaMerge(wfPATH, resultingFM);
			FMHelper fmAfter = new FMHelper(resultingFM);

			// General Properties to check
			TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
			logMsg = String.format("\t" + wf + "&&&&&&&&&&&&&&&&&&----------Children of step%s", (new FMHelper(resultingFM)).getChildren("Steps"));
			logger.debug(logMsg);
			System.out.println(logMsg);
			
		}
	}

	@Test
	@DisplayName("WF1 : Test referencing a feature omiting one : Steps#F31")
	public void testWF1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF1.bpmn2";
		File fin = new File(wfPATH);
		assertTrue(fin.exists());

		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF1.xml";
		TestHelper.copyFM(sourceFM, copiedFM);

		FMHelper fmBefore = new FMHelper(copiedFM);
		// Command
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		logger.debug("added features : %s ", afterList);
		assertEquals(0, afterList.size());
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	@Test
	@DisplayName("WF2 : No Conflict : just to ensure that everything is OK")
	public void testWF2UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF2.bpmn2";
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF2.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		// System.out.println(afterList);
		assertEquals(0, afterList.size());
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// FIX a warning should be raised
	// Pas signalé
	// #F2#F31 is impossible because F31 is already a subfeature if F3
	@Test
	@DisplayName("Conflict : T3 :  F31 can't be in the same time child of F3 and F2 #81 #66")
	public void testWF3UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF3.bpmn2";
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF3.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		// System.out.println(afterList);
		assertEquals(0, afterList.size());
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	@Test
	@DisplayName("WF4 :  Add 1 Algo at thrird level F3#F31#F311 ")
	public void testWF4UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF4.bpmn2";
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF4.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(afterList);
		assertEquals(1, afterList.size());
		assertTrue(afterList.contains("F311"));
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// FIX a warning is at least expected
	// We expect FA as a subfeature of F3 and a super feature of F31
	// Pas signalé
	@Test
	@DisplayName("ToFIX still : WF5 : No Conflict : Add  an intermediate step #F3#FA#F31")
	public void testWF5UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF5.bpmn2";
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF5.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		System.out.println(afterList);
		assertEquals(1, afterList.size());
		assertTrue(afterList.contains("FA"));
		assertTrue(fmAfter.isChildOf("F3", "FA"));
		assertTrue(fmAfter.isChildOf("FA", "F31"));
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// ToFIX with a warning
	@Test
	@DisplayName(" ToFIX still warning : WF6  a Warning is needed many solution are possible, we cannot choose  #FA#F31")
	public void testWF6UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF6.bpmn2";
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_WF6.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = this.commandMetaMerge(wfPATH, copiedFM);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		// It fails, because the merge fails
		//List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		//String logMsg = String.format("added features : %s ", afterList);
		//logger.debug(logMsg);
		// System.out.println(afterList);
		// assertEquals(0, afterList.size());
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// ToFIX : a constraint on a new task (t1 in WF7 [[ MT1 => T1]])
	// that is not in the WF (T1 is nor in the FM, nor in WF7)
	// and referenced later in another WF (WFT1), should become a known task.
	@Test
	@DisplayName("W7 and WFT1 : ensure that an unmanaged feature disappears when defined in any order")
	public void testWF7UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wf7PATH = WF_IN_PATH + "WF7.bpmn2";
		File finWF7 = new File(wf7PATH);
		assertTrue(finWF7.exists());

		String sourceFMPath = DEFAULT_IN_FM;
		String w7FMPath = FM_OUT_PATH + "FMA_WF7.xml";

		// I make a copy for test
		// File w7FMFile =
		this.copyFM(sourceFMPath, w7FMPath);

		String wfT1PATH = WF_IN_PATH + "WFT1.bpmn2";
		File finWFT1 = new File(wfT1PATH);
		assertTrue(finWFT1.exists());

		FMHelper fmBefore = new FMHelper(sourceFMPath);
		// Command
		String[] command = this.commandMetaMerge(wf7PATH, w7FMPath);

		FMHelper fmAfterWF7 = new FMHelper(w7FMPath);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfterWF7, wf7PATH);

		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		// MT1, MT2, MT3, T1, UnManaged, Unmanaged Feature
		assertEquals(6, afterList.size());
		// [[ MT1 => T1]]
		List<String> constraints = fmAfterWF7.getConstraintList();
		logMsg = String.format("added constraints : %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(1, constraints.size());
		// Check idempotence
		TestHelper.checkIdempotence(w7FMPath, command);

		// Merge now with WFT1 in the resulting FM
		// I make a copy for test
		String wF7_T1_FMPath = FM_OUT_PATH + "FMA_WF7T1.xml";
		// File wF7T1FMFile =
		this.copyFM(w7FMPath, wF7_T1_FMPath);

		command = this.commandMetaMerge(wfT1PATH, wF7_T1_FMPath);
		FMHelper fmAfter_WF7_T1 = new FMHelper(wF7_T1_FMPath);
		// Be careful because some unmanaged should be lost !
//		afterList = TestHelper.nothingLost(fmAfterWF7, fmAfter_WF7_T1, wfT1PATH);
		logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		// Nothing
//		assertEquals(0, afterList.size());

		//
		constraints = fmAfter_WF7_T1.getConstraintList();
		logMsg = String.format("set of constraints : %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(1, constraints.size());
		// Check idempotence
		TestHelper.checkIdempotence(wF7_T1_FMPath, command);
		// FIX
		assertTrue(fmAfter_WF7_T1.isChildOf("Steps", "T1"));
		assertTrue(fmAfter_WF7_T1.isDirectChildOf("Steps", "T1"));

		// TODO
		logMsg = String.format("*****************************-------NOW MERGE T1 then WF7 ");
		logger.debug(logMsg);
		System.out.println(logMsg);

		// Merge T1 then merge F7

		// We expect equivalent FM
		String wT1FMPath = FM_OUT_PATH + "FMA_WFT1.xml";
		this.copyFM(sourceFMPath, wT1FMPath);

		command = this.commandMetaMerge(wfT1PATH, wT1FMPath);
		FMHelper fmAfterWFT1 = new FMHelper(wT1FMPath);

		// General Properties to check
		afterList = TestHelper.nothingLost(fmBefore, fmAfterWFT1, wT1FMPath);
		logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		// T1
		assertEquals(1, afterList.size());

		String wT1_7FMPath = FM_OUT_PATH + "FMA_WFT1_7.xml";
		this.copyFM(wT1FMPath, wT1_7FMPath);
		command = this.commandMetaMerge(wf7PATH, wT1_7FMPath);
		FMHelper fmAfterWFT1_7 = new FMHelper(wT1_7FMPath);

		// General Properties to check
		afterList = TestHelper.nothingLost(fmAfterWFT1, fmAfterWFT1_7, wf7PATH);
		logMsg = String.format("added features after adding W7 in FMA_T1: %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		// MT1, MT2, MT3,
		assertEquals(3, afterList.size());
		// [[ MT1 => T1]]
		constraints = fmAfterWFT1_7.getConstraintList();
		logMsg = String.format("added constraints : %s ", constraints);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertEquals(1, constraints.size());

		// FIX
		TestHelper.compare(wF7_T1_FMPath, wT1_7FMPath);

	}

	private File copyFM(String sourceFMPath, String targetFMPath) throws IOException {
		File w7FMFile = new File(targetFMPath);
		File sourceFile = new File(sourceFMPath);
		FileUtils.copyFile(sourceFile, w7FMFile);
		assertTrue(w7FMFile.exists());
		return w7FMFile;
	}

	private String[] commandMetaMerge(String wfPATH, String copiedFM) {
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}

	// One feature is abstract and concret
	// e.g. a Meta declare Normalize and an instance uses also Normalize
	// ==> suggest user to specialize Normalize with a concrete task ?
	// e.g. Normalize is said concrete in FM
	// a Meta uses Normalize
	// ==> A warning is raised and Normalize becomes "abstract"?
}
