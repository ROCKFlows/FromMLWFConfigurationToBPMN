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

	// FIX FAILS when looking inside the hierarchy is not the good one
	@Test
	@DisplayName("Test 1 in #147 : #f1 + #f2 + #f1#f2 = #f1#f2 in any order")
	public void test11UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// merge t1, then t2 then t1t2 results in the same FM than t1t2 and the same in
		// any order
		// FIX
		// mergeMeta(1,Arrays.asList( "WFT1","WFT2","WFT1T2"), "WFT1T2" );

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

	@Test
	@DisplayName("Test 4 in #147 : #f2#f3 + #f1#f2#f3 = #f1#f2#f3 in any order")
	public void test4UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// FIX
		// mergeMeta(4, Arrays.asList( "WFT2T3", "WFT1T2T3"), "WFT1T2T3" );
	}

	@Test
	@DisplayName("Test 5 in #147 : #f2#f3 + #f1#f2 = #f1#f2#f3 in any order")
	public void test5UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// FIX
		// mergeMeta(5, Arrays.asList( "WFT2T3", "WFT1T2"), "WFT1T2T3" );

	}

	// TODO Manage this case
	@Test
	@DisplayName("Test 6 in #147 : #f2#f3 + #f1#f3 fails because we don't know the order between f2 and f1 ")
	public void test6UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {

		// FIX
		// mergeMeta(6, Arrays.asList( "WFT2T3", "WFT1T3"), "FAIL" );
	}

	private void mergeMeta(int testNumber, List<String> wfList, String globalWF)
			throws ParserConfigurationException, SAXException, IOException {
		String sourceFM = DEFAULT_IN_FM;
		File sourceFile = new File(sourceFM);
		FMHelper fmBefore = new FMHelper(sourceFM);

		// I make a copy for test
		String resultingFM = FM_OUT_PATH + "FMA_Test" + testNumber + ".xml";
		TestHelper.copyFM(sourceFM, resultingFM);
		File copiedFile = new File(resultingFM);
		FileUtils.copyFile(sourceFile, copiedFile);

		this.mergeMetaAWFList(wfList, fmBefore, resultingFM, copiedFile);

		// I make a copy for test
		String resultingFMBis = FM_OUT_PATH + "FMA_TestBis" + testNumber + ".xml";
		TestHelper.copyFM(sourceFM, resultingFMBis);
		File copiedFileBis = new File(resultingFMBis);

		String wfPATH = WF_IN_PATH + globalWF + ".bpmn2";
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", resultingFMBis, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(copiedFileBis.exists());

		TestHelper.compare(resultingFM, resultingFMBis);

		Collections.shuffle(wfList);
		resultingFM = FM_OUT_PATH + "FMA_TestOrderFree" + testNumber + ".xml";
		copiedFile = new File(resultingFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		this.mergeMetaAWFList(wfList, fmBefore, resultingFM, copiedFile);
		TestHelper.compare(resultingFM, resultingFMBis);
	}

	private void mergeMetaAWFList(List<String> wfList, FMHelper fmBefore, String resultingFM, File copiedFile)
			throws ParserConfigurationException, SAXException, IOException {
		for (String wf : wfList) {
			String wfPATH = WF_IN_PATH + wf + ".bpmn2";
			File fin = new File(wfPATH);
			assertTrue(fin.exists());
			assertTrue(copiedFile.exists());
			// Command
			String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", resultingFM, "-v", "7" };
			com.ml2wf.App.main(command);
			assertTrue(copiedFile.exists());
			FMHelper fmAfter = new FMHelper(resultingFM);
			// General Properties to check
			TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
			// This test involves managing naming differences using '_' in FM and BPMN
			// logger.debug("added features : %s ", afterList);
			// TestHelper.checkIdempotence(resultingFM, command);
		}
	}

	@Test
	@DisplayName("Test referencing a feature omiting one : Steps#F31")
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
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(new File(copiedFM).exists());
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
	@DisplayName("No Conflict : just to ensure that everything is OK")
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
		// Command
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
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

	// FIX a warning should be raised
	// Pas signalé
	@Test
	@DisplayName("Conflict : F31 can't be in the same time child of F3 and F2 #81 #66")
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
		// Command
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
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
	@DisplayName("No Conflict : Add 1 Algo at thrird level F3#F31#F311 ")
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
		// Command
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
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

	// FIX a warning is expected
	// FA is added but I can't see it ! The FM is probably probably poorly formed
	// If FA is Added... At least one constraint? or an intermediate node?
	// Pas signalé
	@Test
	@DisplayName("No Conflict : Add  an intermediate step #F3#FA#F31")
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
		// Command
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		System.out.println(afterList);
		assertEquals(1, afterList.size());
		assertTrue(afterList.contains("FA"));
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// FIX FA is lost... Is it the good solution?
	// Pas signalé.
	@Test
	@DisplayName(" Conflict? : Add  an intermediate step silently  #FA#F31")
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
		// Command
		String[] command = new String[] { "merge", "--meta", "-i ", wfPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, wfPATH);
		// This test involves managing naming differences using '_' in FM and BPMN
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		// System.out.println(afterList);
		// assertEquals(0, afterList.size());
		// No warning is expected
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	// One feature is abstract and concret
	// e.g. a Meta declare Normalize and an instance uses also Normalize
	// ==> suggest user to specialize Normalize with a concrete task ?
	// e.g. Normalize is said concrete in FM
	// a Meta uses Normalize
	// ==> A warning is raised and Normalize becomes "abstract"?
}
