package com.ml2wf.core.util;

import com.ml2wf.core.conventions.Notation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * This class contains all attributes and methods required for the feature
 * attributes definition.
 *
 * @author Nicolas Lacroix
 *
 * @see Pattern
 *
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexManager {

    /**
     * Regular expression for the <b>reference</b> retrieval.
     */
    private static final String REFERENCE = String.format("%s(\\w+)", Notation.REFERENCE_VOC);
    /**
     * Regular expression for the <b>optionality</b> retrieval.
     */
    private static final String OPTIONALITY = String.format("%s:(%s|%s)", Notation.OPTIONALITY, true, false);
    /**
     * Regular expression for the <b>category</b> retrieval.
     */
    private static final String CATEGORY = String.format("%s(\\w+)",
            Notation.getQuotedNotation(Notation.CATEGORY_PREFIX_VOC));
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
     * Compiles the given {@code regex} and returns the corresponding
     * {@code Pattern}.
     *
     * @param regex regular expression to compile
     *
     * @return the {@code Pattern} corresponding of the compiled {@code regex}
     *
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
     * @return the {@code Pattern} corresponding of the <b>valid feature name</b> regex
     *
     * @see Pattern
     */
    public static Pattern getValidFeatureNamePattern() {
        if (validFeatureNamePattern == null) {
            validFeatureNamePattern = getPatternOf(VALID_FEATURE_NAME);
        }
        return validFeatureNamePattern;
    }
}