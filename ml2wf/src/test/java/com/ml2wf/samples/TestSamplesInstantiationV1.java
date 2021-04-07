package com.ml2wf.samples;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import com.ml2wf.TestHelper;
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
	private static final String WF_IN_PATH = "./src/test/resources/ForValidationTests/wf_meta/";

	/**
	 * WF XML file path for results
	 */
	private static final String WF_OUT_PATH = "./target/generated/WF/";

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestSamplesInstantiationV1.class);

	@BeforeAll
	public static void setup() {
		File fout = new File(WF_OUT_PATH);
		if (!fout.isDirectory()) {
			fout.mkdir();
		}
		logger.debug("Instanciation Tests ----------");
	}

	@Test
	@DisplayName("Test Generation when input file doesn't exist")
	public void testGenerationWithNoInputFile()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "FeatureBasedMetaWFX.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceFBMFError.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(!fin.exists());
		// Test By code
		// assertThrows(FileNotFoundException.class,
		// () -> {factory = new InstanceFactoryImpl(fin);} );
	}

	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a complex workflow using command line")
	public void testCommandLineGeneration()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "FeatureBasedMetaWF.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceFBMF.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		// assertFalse(outputWFPATH.exists());
		// FIX
		// assertTrue(fout.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();
	}

	// todo: improve testing the content of the file.
	// test : @related_to
	@Test
	@DisplayName("(1) Test Generation with a simple meta workflow using command line")
	public void testSimpleCommandLineGeneration()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWF.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasic.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		TestHelper.logCurrentDirectory();
		String logMsg = String.format("Reading Meta WF %s", metaWFPATH);
		logger.debug(logMsg);
		TestHelper.testDirectory(WF_IN_PATH, metaWFPATH);
		assertTrue(fin.exists(), metaWFPATH + "must exist");
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}

	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a meta workflow with composed tasks using command line")
	public void testCommandLineGeneration4hierarchie()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWFHierarchie.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasicHierarchie.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}

	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a meta workflow with composed tasks using command line")
	public void testCommandLineGeneration4hierarchie2()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWFHierarchie2.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasicHierarchie2.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}

	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a meta workflow with composed tasks using command line")
	public void testCommandLineGeneration4hierarchie3()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWFHierarchie3.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasicHierarchie3.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}

	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a meta workflow with constraints using command line")
	public void testCommandLineGenerationwithConstraints()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWFWithConstraint2.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasicWFWithConstraint2.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}

	// todo test with optionnal task
	// todo: improve testing the content of the file.
	@Test
	@DisplayName("Test Generation with a meta workflow with optionnal Task using command line")
	public void testCommandLineGenerationwithOptionnalTask()
			throws TransformerException, SAXException, IOException, ParserConfigurationException {
		String metaWFPATH = WF_IN_PATH + "BasicMetaWF3.bpmn2";
		String outputWFPATH = WF_OUT_PATH + "instanceBasicWF3.bpmn2";
		File fin = new File(metaWFPATH);
		File fout = new File(outputWFPATH);
		assertTrue(fin.exists());
		com.ml2wf.App.main(new String[] { "generate", "-i ", metaWFPATH, "-o ", outputWFPATH, "-v", "7" });
		assertTrue(fout.exists());
		TestHelper.noLostTaskAtInstanciation(metaWFPATH, outputWFPATH);
		// FIX
		// fout.delete();

	}
}
