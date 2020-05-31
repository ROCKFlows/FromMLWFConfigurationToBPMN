package com.ml2wf.constraints.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ml2wf.conventions.enums.fm.FeatureModelNames;

/**
 * TODO
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
public class Config {

	private String filePath;
	private static final Map<String, FeatureModelNames> vocMapping; // TODO: replace by a Config instance
	static {
		Map<String, FeatureModelNames> aMap = new HashMap<>();
		aMap.put(">>", null); // before : TODO
		aMap.put("<<", null); // after : TODO
		aMap.put("=>", FeatureModelNames.IMPLIES); // implies
		aMap.put("!", FeatureModelNames.NOT); // not
		aMap.put("<=>", FeatureModelNames.EQUIVALENT); // equivalent
		aMap.put("&", FeatureModelNames.CONJ); // and
		aMap.put("|", FeatureModelNames.DISJ); // or
		vocMapping = Collections.unmodifiableMap(aMap);
	}

	public Config(String filePath) {

	}

	public static Map<String, FeatureModelNames> getVocmapping() {
		return vocMapping;
	}

	public List<String> getOperatorsList() {
		return vocMapping.keySet().stream().collect(Collectors.toList());
	}

	public List<String> getBinaryOperators() {
		List<String> binary = this.getOperatorsList();
		binary.removeAll(this.getUnaryOperators());
		return binary;
	}

	public List<String> getUnaryOperators() {
		return new ArrayList<>(Arrays.asList("!")); // TODO: replace with config file
	}

	public boolean isUnaryOperator(String operator) {
		return this.getUnaryOperators().contains(operator); // TODO: change considering the config file
	}

	private boolean isAnOperator(String character) {
		return this.getOperatorsList().contains(character);
	}
}
