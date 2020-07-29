package com.ml2wf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ml2wf.util.FMHelper;
import com.ml2wf.util.WFHelper;

public class TestHelper {

	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestHelper.class);

	/*
	 * 
	 * Utility methods
	 */

	public static final String  STEPS = "Steps";
	public static final String  UNMANAGED_TASKS = "Unmanaged_Tasks";
	public static final String  UNMANAGED_FEATURES ="Unmanaged_Features";

	public static void logCurrentDirectory() {
		String curDir = System.getProperty("user.dir");
		String  logMsg = String.format("Current directory %s", curDir);
		// System.out.println (logMsg);
		logger.debug(logMsg);
	}

	public static void testDirectory(String path, String file) {
		String curDir = System.getProperty("user.dir");
		String  logMsg = String.format("Current directory %s", curDir);
		// System.out.println (logMsg);
		logger.debug(logMsg);
		File f = new File(path);
		logMsg = String.format("Expected path %s",   f.getAbsolutePath());
		logger.debug(logMsg);
	}


	private static List<String> normalize(List<String> list) {
		List<String> listCopy= new ArrayList<>();
		list.forEach(t  -> listCopy.add( t.trim().replace(" ", "_")));
		return listCopy;
	}

	private static List<String> normalizeAllTasks(List<String> list) {
		List<String> listCopy= normalize(list);
		//System.out.println("taches a normaliser.." + listCopy);
		List<String> results  = new ArrayList<>();
		listCopy.forEach(t  -> 
		results.addAll(Arrays.asList(t.split("#"))) );
		results.removeAll(Arrays.asList(""));
		//System.out.println("taches normalisées.." + results);
		return results;
	}

	private static List<String> normalizeOnlySubtasks(List<String> list) {
		List<String> listCopy= normalize(list);
		//System.out.println("taches a normaliser" + list);
		list.forEach(t  -> {
			String[] x = t.split("#");
			listCopy.add(x[x.length - 1]);
		} );
		//System.out.println("taches normalisées" + listCopy);
		return listCopy;
	}

	public static List<String> nothingLost(FMHelper fmBefore, FMHelper fmAfter, List<String> wfs) throws ParserConfigurationException, SAXException, IOException {

		List<String> afterList = normalize(fmAfter.getFeatureNameList());

		for (String wfPATH : wfs) {
			testTasksAgainstFeatures(afterList, wfPATH);
		};

		afterList= normalize(noFeatureLost(fmBefore, fmAfter));
		return afterList;
	}

	private static void testTasksAgainstFeatures(List<String> afterList, String wfPATH)
			throws ParserConfigurationException, SAXException, IOException {
		File wfFile = new File(wfPATH);
		//TODO improve error managmenet
		if (! wfFile.exists())
			throw new IOException("Unexpected to check task against feature in a non-existing WF");
		else {
			if (wfFile.isDirectory()) {
				String logMsg = String.format("TODO Checks in a directory %s ", wfPATH);
				logger.debug(logMsg);
				System.out.println(logMsg);
				return;
			}

		}

		WFHelper wf = new WFHelper(wfPATH);
		List<String> tasks = wf.gettaskNameList();
		tasks = normalizeAllTasks(tasks);
		List<String> lostTasks = lostTasks(tasks, afterList);
		String logMsg = String.format("Lost Tasks: %s ", lostTasks);
		logger.debug(logMsg);
		System.out.println(logMsg);
		assertTrue( lostTasks.isEmpty());
	}

	public static List<String> nothingLost(FMHelper fmBefore, FMHelper fmAfter, String wfPATH ) throws ParserConfigurationException, SAXException, IOException{
		List<String> afterList = normalize(fmAfter.getFeatureNameList());
		testTasksAgainstFeatures(afterList, wfPATH);
		afterList= normalize(noFeatureLost(fmBefore, fmAfter));

		return afterList;
	}

	public static List<String> noFeatureLost(FMHelper fmBefore, FMHelper fmAfter) {
		List<String> afterList = checkNoFeaturesAreLost(fmAfter, fmBefore);
		String logMsg = String.format("added features : %s ", afterList);
		logger.debug(logMsg);
		List<String> afterConstraints = checkNoConstraintsAreLost(fmAfter, fmBefore);
		logMsg = String.format("added Constraints : %s ", afterConstraints);
		logger.debug(logMsg);
		//todo check that no tasks are lost
		return afterList;
	}

	//TODO add test on constraints
	public static void checkIdempotence(String fM, String[] command)
			throws ParserConfigurationException, SAXException, IOException {
		FMHelper fmBefore;
		FMHelper fmAfter;
		logger.debug("Test Idempotence {}");
		List<String> afterList;
		fmBefore = new FMHelper(fM);
		String logMsg = String.format("Before : %s" , fmBefore.getFeatureNameList());
		logger.debug(logMsg);
		//System.out.println("Before : " + fmBefore.getFeatureNameList());
		com.ml2wf.App.main(command);
		fmAfter = new FMHelper(fM);
		logMsg = String.format("After : %s" , fmAfter.getFeatureNameList());
		logger.debug(logMsg);
		//System.out.println("After : " + fmAfter.getFeatureNameList());
		afterList = checkNoFeaturesAreLost(fmAfter, fmBefore);
		logMsg = String.format("AFTER should be empty : %s", afterList);
		logger.debug(logMsg);
		//System.out.println("AFTER should be empty : %s" + afterList);
		System.out.println(logMsg);
		//FIX Unmanaged is generated
		assertTrue(afterList.isEmpty());
	}


	public static List<String> checkNoFeaturesAreLost(FMHelper fmAfter, FMHelper fmBefore) {
		List<String> beforeList = fmBefore.getFeatureNameList();
		List<String> afterList = fmAfter.getFeatureNameList();
		beforeList = normalize(beforeList);
		afterList = normalize(afterList);
		logger.debug("No features are lost");
		String logMsg = String.format("Before features : %s ", beforeList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		logMsg = String.format("After features : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		List<String> x = new ArrayList<>(beforeList);
		assertTrue(afterList.containsAll(beforeList));
		afterList.removeAll(beforeList);
		logMsg = String.format("All features are present and added features are: %s ", afterList);
		logger.debug(logMsg);
		return afterList;
	}

	public static List<String> checkNoConstraintsAreLost(FMHelper fmAfter, FMHelper fmBefore) {
		List<String> beforeList = fmBefore.getConstraintList();
		List<String> afterList = fmAfter.getConstraintList();
		logger.debug("Before constraints : %s ", beforeList);
		logger.debug("after constraints : %s ", afterList);
		assertTrue(afterList.containsAll(beforeList));
		afterList.removeAll(beforeList);
		return afterList;
	}

	public static List<String> lostTasks(List<String> tasks, List<String> features){
		List<String> tasksTOSave = normalize(tasks);
		List<String> featureList = normalize(features);
		logger.debug("Features : %s ", featureList);
		tasksTOSave.removeAll(featureList);
		return tasksTOSave;

	}

	public static void noLostTaskAtInstanciation(String wfInPath, String wfOutPath) throws ParserConfigurationException, SAXException, IOException {
		WFHelper wfmeta = new WFHelper(wfInPath);
		List<String> tasksMeta = wfmeta.gettaskNameList();
		WFHelper wfinstance = new WFHelper(wfOutPath);
		List<String> tasksInstance = wfinstance.gettaskNameList();
		assertEquals(tasksMeta.size(), tasksInstance.size());
		//Todo manage references to meta in generated tasks

	}

	public static void allTheseFeaturesAreAbstract(List<String> afterList, FMHelper fmAfter) {
		String logMsg = String.format("These features should be abstract : %s ", afterList);
		logger.debug(logMsg);
		System.out.println(logMsg);
		for (String s : afterList)
			assertTrue(fmAfter.isAbstract(s));

	}

	//TODO add a test for this method
	public static List<String> getTasks(List<String> afterList, FMHelper fmAfter) {
		List<String> tasks = new ArrayList<String>();
		for (String s : afterList)
		{
			if ( (fmAfter.isChildOf(STEPS, s)) || (fmAfter.isChildOf(UNMANAGED_TASKS, s)) )
				tasks.add(s);
		}
		return tasks;
	}

	public static void allTheseFeaturesAreConcret(List<String> afterList, FMHelper fmAfter) {
		for (String s : afterList)
			assertFalse(fmAfter.isAbstract(s));

	}

	public static void testAbstractAndConcreteFeatures(List<String> addedFeatures, String instanceWFPATH, FMHelper fmAfter) throws ParserConfigurationException, SAXException, IOException {

		WFHelper wfinstance = new WFHelper(instanceWFPATH);
		List<String> taskNames = wfinstance.gettaskNameList();
		List<String> subtasks = normalizeOnlySubtasks(taskNames);
		List<String> allTasks = normalizeAllTasks(taskNames);
		allTasks.removeAll(subtasks);
		//all added subtasks should be concrete
		allTheseFeaturesAreConcret(subtasks,fmAfter);
		//all other tasks should be asbstract
		allTheseFeaturesAreAbstract(allTasks,fmAfter);

	}

	public static void compare(String resultingFM, String resultingFMBis) throws ParserConfigurationException, SAXException, IOException {
		String logMsg = String.format("Compare two FM %s avec %s  ",resultingFM, resultingFMBis);
		logger.debug(logMsg);
		System.out.println(logMsg);

		FMHelper fm1 = new FMHelper(resultingFM);
		FMHelper fm2 = new FMHelper(resultingFMBis);
		List<String> fm1FL = fm1.getFeatureNameList();
		List<String> fm2FL = fm2.getFeatureNameList();
		logMsg = String.format("Compare feature lists: %s  With : %s" , fm1FL, fm2FL);
		logger.debug(logMsg);
		System.out.println(logMsg);
		compareEquals(fm2FL,fm1FL);
		//TODO tests same hierarchies and same constraints
		List<String> childrenInFM1 = fm1.getChildren("Steps");
		List<String> childrenInFM2 = fm2.getChildren("Steps");
		logMsg = String.format("Children of Steps : %s against %s" , childrenInFM1, childrenInFM2);
		logger.debug(logMsg);
		System.out.println(logMsg);
		compareEquals(childrenInFM1,childrenInFM2);
	}
	static void compareEquals(List<String> l1, List<String> l2) {
		Collections.sort(l1);
		Collections.sort(l2);
		assertEquals(l1, l2);

	}


	public static void copyFM(String sourceFM,String copiedFM) throws IOException {
		File copiedFile = new File(copiedFM);
		File sourceFile = new File(sourceFM);
		assertTrue(sourceFile.exists());
		FileUtils.copyFile(sourceFile, copiedFile);
		assertTrue(copiedFile.exists());
	}

	public static boolean testConstraintImpliesAnd(List<String> constraints, String leftFeature, List<String> featureList) {
		for (String constraint : constraints) {
			constraint = constraint.trim();
			String[] compose = constraint.split(FMHelper.IMPLY_NOTATION);
			if ( (compose != null ) && compose.length == 2) {
				String left = compose[0];
				if (left.equals(leftFeature)) {
					System.out.println("Found : " + left);
					if (testConstraintAnd(compose[1],featureList) ) 
						return true;//TODO
				}
			}
		}
		return false;
	}

	private static boolean testConstraintAnd(String elements, List<String> featureList) {
		String[] compose = elements.split(" ");
		if ( (compose != null ) && compose.length>0) {
		 for (String f : compose) {
			 f = f.trim();
		 	 if (featureList.contains(f))
		 		 featureList.remove(f);
		 	 else return false;
		 }
		 return featureList.isEmpty();
		 }
		return false;
	}


}
