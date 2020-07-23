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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.ml2wf.TestHelper;
import com.ml2wf.util.FMHelper;

public class TestBuild {
	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestBuild.class);

	private static final String SAMPLE_PATH = "./src/test/resources/ForValidationTests/ForBuild/";
	private static final String FM_IN_PATH = SAMPLE_PATH + "feature_models/";
	private static final String DEFAULT_IN_FM = FM_IN_PATH + "FMA.xml";
	private static final String FM_OUT_PATH = "./target/generated/FM_B/";
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Conflicts in merge of instance WF!");
	}
	
	private String[] commandBuild(String metaDirectory, String instanceDirectory, String copiedFM) {
		// Command
		String[] command = new String[] { "build", "-f",copiedFM, "-m", metaDirectory,  "-i ", instanceDirectory, "-v", "7" };
		com.ml2wf.App.main(command);
		return command;
	}
	
	@Test
	@DisplayName("Build0 ")
	public void testBuild0UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String root = SAMPLE_PATH + "build0/";
		
		String metaDirectory = root + "wf_meta";
		File dInMeta = new File(metaDirectory);
		assertTrue(dInMeta.exists() && dInMeta.isDirectory());

		String instanceDirectory = root + "wf_instance";
		File dInInstance = new File(instanceDirectory);
		assertTrue(dInInstance.exists() && dInInstance.isDirectory());
		
		String sourceFM = DEFAULT_IN_FM;
		// I make a copy for test
		String copiedFM = FM_OUT_PATH + "FMA_B0.xml";
		TestHelper.copyFM(sourceFM, copiedFM);

		FMHelper fmBefore = new FMHelper(copiedFM);
		String[] command = commandBuild(metaDirectory, instanceDirectory, copiedFM);

		assertTrue(new File(copiedFM).exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterListMeta = TestHelper.nothingLost(fmBefore, fmAfter, metaDirectory);
		//List<String> afterListInstance = 
				TestHelper.nothingLost(fmBefore, fmAfter, instanceDirectory);
		// This test involves managing naming differences using '_' in FM and BPMN
		//F31, F2, 
		//F311 (WF4), T1 (WFT1), T2(WFT1T2)
		String logMsg = String.format("added features : %s ", afterListMeta);
		logger.debug(logMsg);
		System.out.println(logMsg);

		

		// Specific properties
		//TODO

	}
	
	
}
