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

}
