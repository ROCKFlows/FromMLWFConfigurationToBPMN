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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;


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
	private static final String SIMPLE_WF_PATH = "./wf_meta/simple_wf.bpmn";

	/**
	 * Basic WF XML file path.
	 */
	private static final String RESULT_DIRECTORY = ".src/test/resources/results/";
	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesInstantiationV1.class);

	@BeforeEach
	public void setUp() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
		logger.info("Hello World!");
		this.classLoader = this.getClass().getClassLoader();
		this.url = this.classLoader.getResource(SIMPLE_WF_PATH);
		this.factory = new InstanceFactoryImpl(new File(this.url.toURI()));
	}

	@AfterEach
	public void clean() {
		this.classLoader = null;
		this.url = null;
		this.factory = null;
	}

	
	//todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a complex workflow")
	public void testGenerationWithCompletExample() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/FeatureBasedMetaWF.bpmn2";
		String outputWFPATH = "../BPMN-Models";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		String pwd = System.getProperty("user.dir");
        System.out.println("Le répertoire courant est : " + pwd);
		assertTrue(fout.exists() && fout.isDirectory());
		assertTrue(fin.exists());
		//Test By code
		factory = new InstanceFactoryImpl(fin);
		factory.getWFInstance(fout);
		fout = new File(outputWFPATH+"/FeatureBasedMetaWF_instance.bpmn2");
		assertTrue(fout.exists());
		fout.delete();
		//com.ml2wf.App.main(new String[] { "-g -i " + metaWFPATH+ " -o " + outputWFPATH });
	}
	
	@Test
	@DisplayName("Test Generation when input file doesn't exist")
	public void testGenerationWithNoInputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/FeatureBasedMetaWFX.bpmn2";
		String outputWFPATH = "../BPMN-Models";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(!fin.exists());
		assertTrue(fout.exists());
		//Test By code
		assertThrows(FileNotFoundException.class, 
				() -> {factory = new InstanceFactoryImpl(fin);} );
	}
	//fix it: If we choose to fix the generation, by generating according to the return file (and not by giving it a predefined name), then there should be no error, because in this case, the file may not exist.
	//	On the contrary, if we decide to save in a directory, there should be an error message if that directory does not exist.
	// Currently it generates in the tree above, if the output does not exist.
	@Test
	@DisplayName("Test Generation when output file doesn't exist")
	public void testGenerationWithNoOutputFile() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/FeatureBasedMetaWF.bpmn2";
		String outputWFPATH = "../BPMN-Models2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		assertTrue(!fout.exists());
		//Test By code
		factory = new InstanceFactoryImpl(fin);
		//Test By code
		//FIX : I have no idea what the program does! 
//				assertThrows(FileNotFoundException.class, 
//						() -> {		factory.getWFInstance(fout);} );
	}
	
	
	//todo: improve testing the content of the file.
	//fix : correct the test: it is not consistent to give an output file and get the result in another file.
	@Test
	@DisplayName("Test Generation with a complex workflow using command line")
	public void testCommandLineGeneration() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/FeatureBasedMetaWF.bpmn2";
		String outputWFPATH = "../BPMN-Models/instance.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		//FIX
		assertTrue(fout.exists());
		com.ml2wf.App.main(new String[] {"generate", "-i ", metaWFPATH, "-o ",outputWFPATH, "-v","7"});
		fout = new File("../BPMN-Models/FeatureBasedMetaWF_instance.bpmn2");
		assertTrue(fout.exists());
		//FIX
		//fout.delete();
		
	}
	
	
	//todo: improve testing the content of the file.
	//fix : correct the test: it is not consistent to give an output file and get the result in another file.
	@Test
	@DisplayName("Test Generation with a simple meta workflow using command line")
	public void testSimpleCommandLineGeneration() throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = "../BPMN-Models/BasicMetaWF.bpmn2";
		String outputWFPATH = "../BPMN-Models/instance.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		//FIX
		assertTrue(fout.exists());
		com.ml2wf.App.main(new String[] {"generate", "-i ", metaWFPATH, "-o ",outputWFPATH, "-v","7"});
		fout = new File("../BPMN-Models/BasicMetaWF_instance.bpmn2");
		assertTrue(fout.exists());
		//FIX
		//fout.delete();
		
	}
	
}
