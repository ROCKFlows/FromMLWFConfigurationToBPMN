package com.ml2wf.util;

import java.util.regex.Pattern;

import com.ml2wf.conventions.Notation;

/**
 * This class contains all attributes and methods required for the feature
 * attributes definition.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see Pattern
 *
 */
public final class RegexManager {

	/**
	 * Regular expression for the <b>reference</b> retrieval.
	 */
	private static final String REFERENCE = String.format("%s(\\w+)", Notation.getReferenceVoc());
	/**
	 * Regular expression for the <b>optionality</b> retrieval.
	 */
	private static final String OPTIONALITY = String.format("%s:(%s|%s)", Notation.getOptionality(),
			String.valueOf(true), String.valueOf(false));
	/**
	 * Regular expression for the <b>category</b> retrieval.
	 */
	private static final String CATEGORY = String.format("%s(\\w+)",
			Notation.getQuotedNotation(Notation.getCategoryPrefixVoc()));
	/**
	 * Regular expression for the <b>digit</b> retrieval.
	 */
	private static final String DIGIT = "\\d+";
	/**
	 * Regular expression that defines a valid feature name.
	 */
	private static final String VALID_FEATURE_NAME = "^[a-zA-Z]+\\w*$";
	/**
	 * {@code Pattern} corresponding of the {@link #REFERENCE} regex.
	 */
	private static Pattern referencePattern;
	/**
	 * {@code Pattern} corresponding of the {@link #OPTIONALITY} regex.
	 */
	private static Pattern optionalityPattern;
	/**
	 * {@code Pattern} corresponding of the {@link #CATEGORY} regex.
	 */
	private static Pattern categoryPattern;
	/**
	 * {@code Pattern} corresponding of the {@link #DIGIT} regex.
	 */
	private static Pattern digitPattern;
	/**
	 * {@code Pattern} corresponding of the {@link #VALID_FEATURE_NAME} regex.
	 */
	private static Pattern validFeatureNamePattern;

	/**
	 * {@code RegexManager} empty constructor.
	 */
	private RegexManager() {

	}

	/**
	 * Compiles the given {@code regex} and returns the corresponding
	 * {@code Pattern}.
	 *
	 * @param regex regular expression to compile
	 * @return the {@code Pattern} corresponding of the compiled {@code regex}
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	private static Pattern getPatternOf(String regex) {
		return Pattern.compile(regex, Pattern.MULTILINE);
	}

	/**
	 * Returns the {@code Pattern} corresponding of the <b>reference</b> regex.
	 *
	 * @return the {@code Pattern} corresponding of the <b>reference</b> regex
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static Pattern getReferencePattern() {
		if (referencePattern == null) {
			referencePattern = getPatternOf(REFERENCE);
		}
		return referencePattern;
	}

	/**
	 * Returns the {@code Pattern} corresponding of the <b>optionality</b> regex.
	 *
	 * @return the {@code Pattern} corresponding of the <b>optionality</b> regex
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static Pattern getOptionalityPattern() {
		if (optionalityPattern == null) {
			optionalityPattern = getPatternOf(OPTIONALITY);
		}
		return optionalityPattern;
	}

	/**
	 * Returns the {@code Pattern} corresponding of the <b>category</b> regex.
	 *
	 * @return the {@code Pattern} corresponding of the <b>category</b> regex
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static Pattern getCategoryPattern() {
		if (categoryPattern == null) {
			categoryPattern = getPatternOf(CATEGORY);
		}
		return categoryPattern;
	}

	/**
	 * Returns the {@code Pattern} corresponding of the <b>digit</b> regex.
	 *
	 * @return the {@code Pattern} corresponding of the <b>digit</b> regex
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static Pattern getDigitPattern() {
		if (digitPattern == null) {
			digitPattern = getPatternOf(DIGIT);
		}
		return digitPattern;
	}

	/**
	 * Returns the {@code Pattern} corresponding of the <b>valid feature name</b>
	 * regex.
	 *
	 * @return the {@code Pattern} corresponding of the <b>valid feature name</b>
	 *         regex
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static Pattern getValidFeatureNamePattern() {
		if (validFeatureNamePattern == null) {
			validFeatureNamePattern = getPatternOf(VALID_FEATURE_NAME);
		}
		return validFeatureNamePattern;
	}

}
