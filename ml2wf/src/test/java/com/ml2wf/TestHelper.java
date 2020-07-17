package com.ml2wf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
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
	
	public static List<String> nothingLost(FMHelper fmBefore, FMHelper fmAfter, WFHelper wf){
		List<String> afterList = normalize(fmAfter.getFeatureNameList());
		List<String> tasks = wf.gettaskNameList();
		List<String> lostTasks = lostTasks(tasks, afterList);
		//System.out.println(" LOST : " + lostTasks);
		assertTrue( lostTasks.isEmpty());
		
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
		List<String> afterList;
		fmBefore = new FMHelper(fM);
		com.ml2wf.App.main(command);
		fmAfter = new FMHelper(fM);
		afterList = checkNoFeaturesAreLost(fmAfter, fmBefore);
		logger.debug("AFTER should be empty : %s", afterList);
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
		tasksTOSave.removeAll(featureList);
		return tasksTOSave;
		
	}
}
