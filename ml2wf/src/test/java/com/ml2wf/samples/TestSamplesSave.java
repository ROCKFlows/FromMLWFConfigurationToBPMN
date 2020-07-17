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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;

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

	private static final String FM_OUT_PATH = "./target/generated/FM_S/";
	
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Samples about SAVE!");
	}

	@AfterEach
	public void clean() {
	}

	@Test
	@DisplayName("Test with a basic workflow adding one Step")
	public void testBasicSampleUsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWF.bpmn2";
		String instanceWFPATH = "../BPMN-Models/BasicWF_instance00.bpmn2";
		String sourceFM="../samples/basicFM.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"basicFM_SAVE0.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(metaWFPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());
		
		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"save","-i ", metaWFPATH, instanceWFPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);
		
		//General Properties to check
		List<String> afterList = TestHelper.noFeatureLost(fmBefore, fmAfter);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		System.out.println(afterList);
		
		//Specific properties
		//assertEquals(1, afterList.size());
		assertTrue(fmAfter.isFeature("Evaluating_step"));
		assertTrue(fmAfter.isDirectChildOf("Steps", "Evaluating_step"));
		//TODO test abstract Features
		
		//Check idempotence
		//TestHelper.checkIdempotence(copiedFM, command);
		
	}
	
	

}
