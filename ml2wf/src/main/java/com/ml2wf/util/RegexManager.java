package com.ml2wf.util;

import java.util.regex.Pattern;

import com.ml2wf.conventions.Notation;

public class RegexManager {

	// TODO: check +/*

	private static final String REFERENCE = String.format("%s(\\w+)", Notation.getReferenceVoc());
	private static final String OPTIONALITY = String.format("%s:(%s|%s)", Notation.getOptionality(),
			String.valueOf(true), String.valueOf(false));
	private static final String CATEGORY = String.format("%s(\\w+)", Notation.getCategoryPrefixVoc());

	private static Pattern referencePattern;
	private static Pattern optionalityPattern;
	private static Pattern categoryPattern;

	private RegexManager() {

	}

	private static Pattern getPatternOf(String regex) {
		return Pattern.compile(regex);
	}

	public static Pattern getReferencePattern() {
		if (referencePattern == null) {
			referencePattern = getPatternOf(REFERENCE);
		}
		return referencePattern;
	}

	public static Pattern getOptionalityPattern() {
		if (optionalityPattern == null) {
			optionalityPattern = getPatternOf(OPTIONALITY);
		}
		return optionalityPattern;
	}

	public static Pattern getCategoryPattern() {
		if (categoryPattern == null) {
			categoryPattern = getPatternOf(CATEGORY);
		}
		return categoryPattern;
	}

}
