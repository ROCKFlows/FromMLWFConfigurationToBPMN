package com.ml2wf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	

	private static List<String> normalize(List<String> list) {
		List<String> listCopy= new ArrayList<>();
		list.forEach(t  -> listCopy.add( t.replace(" ", "_")));
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
		WFHelper wf = new WFHelper(wfPATH);
		List<String> tasks = wf.gettaskNameList();
		tasks = normalizeAllTasks(tasks);
		List<String> lostTasks = lostTasks(tasks, afterList);
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
		logger.debug("added features : %s ", afterList);
		List<String> afterConstraints = checkNoConstraintsAreLost(fmAfter, fmBefore);
		logger.debug("added Constraints : %s ", afterConstraints);
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
		System.out.println("Before : " + fmBefore.getFeatureNameList());
		com.ml2wf.App.main(command);
		fmAfter = new FMHelper(fM);
		System.out.println("After : " + fmAfter.getFeatureNameList());
		afterList = checkNoFeaturesAreLost(fmAfter, fmBefore);
		logger.debug("AFTER should be empty : %s", afterList);
		System.out.println("AFTER should be empty : %s" + afterList);
		assertTrue(afterList.isEmpty());
	}

	
	public static List<String> checkNoFeaturesAreLost(FMHelper fmAfter, FMHelper fmBefore) {
		List<String> beforeList = fmBefore.getFeatureNameList();
		List<String> afterList = fmAfter.getFeatureNameList();
		beforeList = normalize(beforeList);
		afterList = normalize(afterList);
		logger.debug("Before features : %s ", beforeList);
		logger.debug("after features : %s ", afterList);
		
		List<String> x = new ArrayList<>(beforeList);
		assertTrue(afterList.containsAll(beforeList));
		afterList.removeAll(beforeList);
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
		for (String s : afterList)
			assertTrue(fmAfter.isAbstract(s));
		
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


}
