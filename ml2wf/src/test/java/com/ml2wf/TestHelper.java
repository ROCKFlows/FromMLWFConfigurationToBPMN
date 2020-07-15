package com.ml2wf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.ml2wf.fm.FMHelper;

public class TestHelper {
	
	/**
	 * {@code Logger}'s instance.
	 */
	private static final Logger logger = LogManager.getLogger(TestHelper.class);

	/*
	 * 
	 * Utility methods
	 */

	
	public static void nothingIsLost(FMHelper fmBefore, FMHelper fmAfter) {
		List<String> afterList = checkNoFeaturesAreLost(fmAfter, fmBefore);
		logger.debug("added features : %s ", afterList);
		afterList = checkNoConstraintsAreLost(fmAfter, fmBefore);
		logger.debug("added Constraints : %s ", afterList);
		//todo check that no tasks are lost
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
		logger.debug("Before features : %s ", beforeList);
		logger.debug("after features : %s ", afterList);
		assertTrue(fmAfter.getFeatureNameList().containsAll(beforeList));
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
}
