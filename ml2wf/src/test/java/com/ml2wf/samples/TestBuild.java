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
import org.junit.jupiter.api.Disabled;
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
	
	//TODO : Automate tests
	@Test
	@DisplayName("TODO : Build0 ")
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

		FMHelper fmBefore = FMHelper.createFMHelper(sourceFM);
		String[] command = commandBuild(metaDirectory, instanceDirectory, copiedFM);

		assertTrue(new File(copiedFM).exists());
		FMHelper fmAfter = new FMHelper(copiedFM);

		// General Properties to check
		List<String> afterListMeta = TestHelper.nothingLost(fmBefore, fmAfter, metaDirectory, command);
		//List<String> afterListInstance = 
				TestHelper.nothingLost(fmBefore, fmAfter, instanceDirectory, command);
		//F31, F2, exist
		//F311 (WF4), T1 (WFT1), T2(WFT1T2)
		//Meta Instances
		//5 meta + 
		// instance
		//F31_0 sous F31
		//F31 devient concrete (WF2); F32 sans references(WF3); 
		//WF5 => 2 contraintes + F31_1 et F31_2
				
				
		String logMsg = String.format("added features : %s ", afterListMeta);
		logger.debug(logMsg);
		System.out.println(logMsg);

		

		// Specific properties
		//TODO

	}
	
	
	//TODO : Automate tests
		@Test
		@DisplayName("TODO : Build1 ")
		public void testBuild1UsingCommandLine() throws ParserConfigurationException, SAXException, IOException {
			String root = SAMPLE_PATH + "build1/";
			
			String metaDirectory = root + "wf_meta";
			File dInMeta = new File(metaDirectory);
			assertTrue(dInMeta.exists() && dInMeta.isDirectory());

			String instanceDirectory = root + "wf_instance";
			File dInInstance = new File(instanceDirectory);
			assertTrue(dInInstance.exists() && dInInstance.isDirectory());
			
			String sourceFM = FM_IN_PATH + "basicFM.xml";
			// I make a copy for test
			String copiedFM = FM_OUT_PATH + "FMA_B1.xml";
			TestHelper.copyFM(sourceFM, copiedFM);

			FMHelper fmBefore = FMHelper.createFMHelper(sourceFM);
			
			String[] command = commandBuild(metaDirectory, instanceDirectory, copiedFM);
			FMHelper fmAfter = new FMHelper(copiedFM);

			// General Properties to check
			List<String> afterListMeta = TestHelper.nothingLost(fmBefore, fmAfter, metaDirectory, command);
			//List<String> afterListInstance = 
			TestHelper.nothingLost(fmBefore, fmAfter, instanceDirectory, command);
			//F31, F2, exist
			//F311 (WF4), T1 (WFT1), T2(WFT1T2)
			//Meta Instances
			//5 meta + 
			// instance
			//F31_0 sous F31
			//F31 devient concrete (WF2); F32 sans references(WF3); 
			//WF5 => 2 contraintes + F31_1 et F31_2
					
					
			String logMsg = String.format("added features : %s ", afterListMeta);
			logger.debug(logMsg);
			System.out.println(logMsg);

			

			// Specific properties
			//TODO
		}
	
	
}
