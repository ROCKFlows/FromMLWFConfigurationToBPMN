package com.ml2wf.samples;

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

	// FIX Unmanaged
	@Test
	@DisplayName("Test with a basic workflow adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWF.bpmn2";
		String instanceWFPATH = instanceWF_IN_PATH + "BasicWF_instance00.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "basicFM_SAVE0.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		// Command
		String[] command = new String[] { "save", "-i ", metaWFPATH, instanceWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, Arrays.asList(metaWFPATH, instanceWFPATH));
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		// System.out.println(afterList);

		// Specific properties
		// assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		// TODO test abstract Features
		// TODO test generated constraints
		// FIX
		// Check idempotence
		// FIX Unmanaged is generated
		TestHelper.checkIdempotence(copiedFM, command);

	}

	/**
	 * THis test leads to a valid FM because Missing_value is not abstract, and we
	 * don't forbid to select several algorithms in the same class.
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	// FIX : AFTER should be empty : %s[Unmanaged] Only when applying idempotence
	@Test
	@DisplayName("Test with a hierarchic workflow adding one Step")
	public void testBasicHierachieSampleUsingCommandLine()
			throws ParserConfigurationException, SAXException, IOException {

		String metaWFPATH = metaWF_IN_PATH + "BasicMetaWFHierarchie.bpmn2";
		String instanceWFPATH = instanceWF_IN_PATH + "instanceBasicHierarchieToFail.bpmn2";
		String sourceFM = FM_IN_PATH + "basicFM.xml";

		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "basicFM_Hierarchie.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		// Command
		String[] command = new String[] { "save", "-i ", metaWFPATH, instanceWFPATH, "-o ", copiedFM, "-v", "7" };
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter, Arrays.asList(metaWFPATH, instanceWFPATH));
		// This test involves managing naming differences using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		// System.out.println(afterList);

		// Specific properties
		// assertEquals(1, afterList.size());
		// TODO test abstract Features
		// TODO test generated constraints
		// FIX
		// Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);

	}

	// TODO : Test with optional tasks present and absent in the WF instance; verify
	// that the FM is still consistent.
}