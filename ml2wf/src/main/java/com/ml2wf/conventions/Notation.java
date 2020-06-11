package com.ml2wf.conventions;

import java.util.regex.Pattern;

import com.ml2wf.generation.InstanceFactory;

/**
 * This class is a <b>Data class</b> containing the notation used for the
 * {@linkplain InstanceFactory instances generation}.
 *
 * <p>
 *
 * It also contains methods for naming and
 *
 * <p>
 *
 * <b>Note</b> that all field are static.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public final class Notation {

	/**
	 * <b>generated prefix</b> notation.
	 */
	private static final String GENERATED_PREFIX_VOC = "#";
	/**
	 * <b>instance</b> notation.
	 */
	private static final String INSTANCE_VOC = "_instance";
	/**
	 * <b>reference</b> notation.
	 */
	private static final String REFERENCE_VOC = "refersTo: ";
	/**
	 * <b>generic</b> notation.
	 */
	private static final String GENERIC_VOC = "_Step";
	/**
	 * <b>documentation</b> notation.
	 */
	private static final String DOCUMENTATION_VOC = "Documentation_";
	/**
	 * <b>Text annotation</b> notation.
	 */
	private static final String TEXT_ANNOTATION_VOC = "TextAnnotation_";
	/**
	 * <b>BPMN shape</b> notation.
	 */
	private static final String BPMN_SHAPE_VOC = "BPMNShape_";
	/**
	 * <b>back up</b> notation.
	 */
	private static final String BACKUP_VOC = "_save";
	/**
	 * <b>Left delimiter</b> for the workflow's name section.
	 */
	private static final String WF_NAME_DELIMITER_LEFT = "{{";
	/**
	 * <b>Right delimiter</b> for the workflow's name section.
	 */
	private static final String WF_NAME_DELIMITER_RIGHT = "}}";

	/**
	 * {@code Notation's} default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is <code>private</code>.
	 */
	private Notation() {

	}

	/**
	 * Returns the {@link #GENERATED_PREFIX_VOC} notation.
	 *
	 * @return the generated prefix notation
	 *
	 * @since 1.0
	 */
	public static String getGeneratedPrefixVoc() {
		return GENERATED_PREFIX_VOC;
	}

	/**
	 * Returns the {@link #INSTANCE_VOC} notation.
	 *
	 * @return the instance notation
	 *
	 * @since 1.0
	 */
	public static String getInstanceVoc() {
		return INSTANCE_VOC;
	}

	/**
	 * Returns the {@link #REFERENCE_VOC} notation.
	 *
	 * @return the reference notation
	 *
	 * @since 1.0
	 */
	public static String getReferenceVoc() {
		return REFERENCE_VOC;
	}

	/**
	 * Returns the {@link #GENERIC_VOC} notation.
	 *
	 * @return the generic notation
	 *
	 * @since 1.0
	 */
	public static String getGenericVoc() {
		return GENERIC_VOC;
	}

	/**
	 * Returns the {@link #DOCUMENTATION_VOC} notation.
	 *
	 * @return the documentation notation
	 *
	 * @since 1.0
	 */
	public static String getDocumentationVoc() {
		return DOCUMENTATION_VOC;
	}

	/**
	 * Returns the {@link #TEXT_ANNOTATION_VOC} notation.
	 *
	 * @return the text annotation notation
	 *
	 * @since 1.0
	 */
	public static String getTextAnnotationVoc() {
		return TEXT_ANNOTATION_VOC;
	}

	/**
	 * Returns the {@link #BPMN_SHAPE_VOC} notation.
	 *
	 * @return the BPMN shape notation
	 *
	 * @since 1.0
	 */
	public static String getBpmnShapeVoc() {
		return BPMN_SHAPE_VOC;
	}

	/**
	 * Returns the {@link #BACKUP_VOC} notation.
	 *
	 * @return the back up notation
	 *
	 * @since 1.0
	 */
	public static String getBackupVoc() {
		return BACKUP_VOC;
	}

	/**
	 * Returns the {@link #WF_NAME_DELIMITER_LEFT} notation.
	 *
	 * @return the back up notation
	 *
	 * @since 1.0
	 */
	public static String getWfNameDelimiterLeft() {
		return WF_NAME_DELIMITER_LEFT;
	}

	/**
	 * Returns the {@link #WF_NAME_DELIMITER_RIGHT} notation.
	 *
	 * @return the back up notation
	 *
	 * @since 1.0
	 */
	public static String getWfNameDelimiterRight() {
		return WF_NAME_DELIMITER_RIGHT;
	}

	/**
	 * Returns the quoted notation using the {@link Pattern#quote(String)} method.
	 *
	 * @return the quoted notation using the {@link Pattern#quote(String)} method
	 *
	 * @since 1.0
	 * @see Pattern
	 */
	public static String getQuotedNotation(String notation) {
		String[] splitted = notation.split("");
		return Pattern.quote(splitted[0]).repeat(splitted.length);
	}

}
