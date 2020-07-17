package com.ml2wf.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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

import com.ml2wf.constraints.util.OperAssociation;
import com.ml2wf.generation.InstanceFactoryImpl;


public class TestSamplesInstantiationV1 {

	/**
	 * {@code InstanceFactoryImpl}'s instance.
	 */
	private InstanceFactoryImpl factory;
	/**
	 * {@code ClassLoader}'s instance.
	 *
	 * <p>
	 *
	 * This class is used to get resources.
	 *
	 * @see ClassLoader
	 */
	private ClassLoader classLoader;
	/**
	 * {@code URL}'s instance.
	 *
	 * <p>
	 *
	 * This class is used to get a resource's URL.
	 *
	 * @see URL
	 */
	private URL url;
	/**
	 * Basic WF XML file path.
	 */
	private static final String WF_IN_PATH = "../BPMN-Models/";

	/**
	 *  WF XML file path for results
	 */
	private static final String WF_OUT_PATH = "./target/generated/WF/";
	
	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesInstantiationV1.class);

	
	@BeforeAll
	 public static void setup() {
		File fout = new File(WF_OUT_PATH );
		if (! fout.isDirectory())
			fout.mkdir();
		logger.debug("Instanciation Tests ----------");
    }



	

	
	@Test
	@DisplayName("Test Generation when input file doesn't exist")
	public void testGenerationWithNoInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH +"FeatureBasedMetaWFX.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceFBMFError.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(!fin.exists());
		//Test By code
		//assertThrows(FileNotFoundException.class, 
		//		() -> {factory = new InstanceFactoryImpl(fin);} );
	}
	


	
	
	
	//todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a complex workflow using command line")
	public void testCommandLineGeneration() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH +"FeatureBasedMetaWF.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceFBMF.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		//assertFalse(outputWFPATH.exists());
		//FIX
		//assertTrue(fout.exists());
		com.ml2wf.App.main(new String[] {"generate", "-i ", metaWFPATH, "-o ",outputWFPATH, "-v","7"});
		assertTrue(fout.exists());
		//FIX
		//fout.delete();
	}
	
	
	//todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a simple meta workflow using command line")
	public void testSimpleCommandLineGeneration() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH =  WF_IN_PATH +"BasicMetaWF.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasic.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] {"generate", "-i ", metaWFPATH, "-o ",outputWFPATH, "-v","7"});
		assertTrue(fout.exists());
		//FIX
		//fout.delete();
		
	}
	
}
