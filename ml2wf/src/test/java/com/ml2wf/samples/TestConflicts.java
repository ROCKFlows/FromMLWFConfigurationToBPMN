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
public class TestConflicts {


	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestConflicts.class);

	private static final String FM_IN_PATH = "../samples";
	private static final String FM_OUT_PATH = "./target/generated/FM_C/";
	private static final String WF_IN_PATH = "../BPMN-Models/";
	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello Test Conflicts!");
	}

	@AfterEach
	public void clean() {
	}

	//FIX exception null pointer is raised! 
	@Test
	@DisplayName("Test referencing a feature omiting one : Base#F3#F31")
	public void testWF1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF1.bpmn2";
		String sourceFM="../samples/FMA.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"FMA_WF1.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		//System.out.println(afterList);
		assertEquals(0, afterList.size());
		//No warning is expected
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}


	//FIX exception null pointer is raised! 
	@Test
	@DisplayName("No Conflict : just to ensure that everything is OK")
	public void testWF2UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF2.bpmn2";
		String sourceFM="../samples/FMA.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"FMA_WF2.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		//System.out.println(afterList);
		assertEquals(0, afterList.size());
		//No warning is expected
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	//FIX exception null pointer is raised! 
	@Test
	@DisplayName("Conflict : F31 can't be in the same time child of F3 and F2")
	public void testWF3UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF3.bpmn2";
		String sourceFM="../samples/FMA.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"FMA_WF3.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		//System.out.println(afterList);
		assertEquals(0, afterList.size());
		//No warning is expected
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	//FIX exception null pointer is raised! 
	@Test
	@DisplayName("No Conflict : Add 1 Algo at thrird level F3#F31#F311 ")
	public void testWF4UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF4.bpmn2";
		String sourceFM="../samples/FMA.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"FMA_WF4.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		//System.out.println(afterList);
		assertEquals(0, afterList.size());
		//No warning is expected
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}

	//FIX exception null pointer is raised! 
	@Test
	@DisplayName("No Conflict : Add  an intermediate step #F3#FA#F31")
	public void testWF5UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
		String wfPATH = WF_IN_PATH + "WF5.bpmn2";
		String sourceFM="../samples/FMA.xml";
		//I make a copy for test
		String copiedFM= FM_OUT_PATH +"FMA_WF5.xml";
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		FileUtils.copyFile(sourceFile, copiedFile);
		File fin = new File(wfPATH);
		assertTrue(fin.exists());
		assertTrue(copiedFile.exists());

		FMHelper fmBefore = new FMHelper(copiedFM);
		//Command
		String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
		com.ml2wf.App.main(command);
		assertTrue(copiedFile.exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		//General Properties to check
		List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
		//This test involves managing naming differences  using '_' in FM and BPMN
		logger.debug("added features : %s ", afterList);
		//System.out.println(afterList);
		assertEquals(1, afterList.size());
		//No warning is expected
		//Check idempotence
		TestHelper.checkIdempotence(copiedFM, command);
	}
	
	//FIX exception null pointer is raised! 
		@Test
		@DisplayName(" Conflict? : Add  an intermediate step silently  #FA#F31")
		public void testWF6UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
			String wfPATH = WF_IN_PATH + "WF6.bpmn2";
			String sourceFM="../samples/FMA.xml";
			//I make a copy for test
			String copiedFM= FM_OUT_PATH +"FMA_WF6.xml";
			File copiedFile = new File(copiedFM);
			File sourceFile = new File(sourceFM);
			FileUtils.copyFile(sourceFile, copiedFile);
			File fin = new File(wfPATH);
			assertTrue(fin.exists());
			assertTrue(copiedFile.exists());

			FMHelper fmBefore = new FMHelper(copiedFM);
			//Command
			String[] command = new String[] {"merge","--meta", "-i ", wfPATH, "-o ",copiedFM, "-v","7"};
			com.ml2wf.App.main(command);
			assertTrue(copiedFile.exists());
			FMHelper fmAfter = new FMHelper(copiedFM);

			//General Properties to check
			List<String> afterList = TestHelper.nothingLost(fmBefore, fmAfter,wfPATH);
			//This test involves managing naming differences  using '_' in FM and BPMN
			logger.debug("added features : %s ", afterList);
			//System.out.println(afterList);
			assertEquals(1, afterList.size());
			//No warning is expected
			//Check idempotence
			TestHelper.checkIdempotence(copiedFM, command);
		}
}

