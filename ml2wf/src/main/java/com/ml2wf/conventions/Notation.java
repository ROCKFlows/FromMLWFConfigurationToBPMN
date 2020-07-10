package com.ml2wf.conventions;

import java.util.Arrays;
import java.util.List;
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
	 * The <b>generated prefix</b> notation.
	 */
	private static final String GENERATED_PREFIX_VOC = "#";
	/**
	 * The <b>instance</b> notation.
	 */
	private static final String INSTANCE_VOC = "_instance";
	/**
	 * The <b>reference</b> notation.
	 */
	private static final String REFERENCE_VOC = "refersTo: ";
	/**
	 * The <b>generic</b> notation.
	 */
	private static final String GENERIC_VOC = "_Step";
	/**
	 * The <b>documentation</b> notation.
	 */
	private static final String DOCUMENTATION_VOC = "Documentation_";
	/**
	 * The <b>text annotation</b> notation.
	 */
	private static final String TEXT_ANNOTATION_VOC = "TextAnnotation_";
	/**
	 * The <b>BPMN shape</b> notation.
	 */
	private static final String BPMN_SHAPE_VOC = "BPMNShape_";
	/**
	 * The <b>back up</b> notation.
	 */
	private static final String BACKUP_VOC = "_save";
	/**
	 * The <b>left delimiter</b> for the workflow's references section.
	 */
	private static final String REFERENCES_DELIMITER_LEFT = "{{";
	/**
	 * The <b>right delimiter</b> for the workflow's references section.
	 */
	private static final String REFERENCES_DELIMITER_RIGHT = "}}";
	/**
	 * The <b>left delimiter</b> for the constraint section.
	 */
	private static final String CONSTRAINT_DELIMITER_LEFT = "[[";
	/**
	 * The <b>right delimiter</b> for the constraint section.
	 */
	private static final String CONSTRAINT_DELIMITER_RIGHT = "]]";
	/**
	 * The <b>ID</b> for the global annotation node.
	 */
	private static final String GLOBAL_ANNOTATION_ID = "Global_Annotation";
	/**
	 * The sentence for workflow's name.
	 */
	private static final String WF_NAME_SENTENCE = "Workflow's name %s%s.";
	/**
	 * The sentence for referred meta-workflow.
	 */
	private static final String META_REFERENCE_SENTENCE = "Meta-Workflow : %s%s%s.";
	/**
	 * The sentence for referred data.
	 */
	private static final String DATA_REFERENCE_SENTENCE = "Data : %s%s.";
	/**
	 * The sentence for referred author/article.
	 */
	private static final String AUTHOR_REFERENCE_SENTENCE = "Author/Article : %s%s.";
	/**
	 * The optionality keyword.
	 */
	private static final String OPTIONALITY = "@Optional";
	/**
	 * The <b>category prefix</b> notation.
	 */
	private static final String CATEGORY_PREFIX_VOC = "$";

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
	 * Returns the {@link #REFERENCES_DELIMITER_LEFT} notation.
	 *
	 * @return the workflow's references left delimiter notation
	 *
	 * @since 1.0
	 */
	public static String getReferencesDelimiterLeft() {
		return REFERENCES_DELIMITER_LEFT;
	}

	/**
	 * Returns the {@link #REFERENCES_DELIMITER_RIGHT} notation.
	 *
	 * @return the workflow's references right delimiter notation
	 *
	 * @since 1.0
	 */
	public static String getReferencesDelimiterRight() {
		return REFERENCES_DELIMITER_RIGHT;
	}

	/**
	 * Returns the {@link #CONSTRAINT_DELIMITER_LEFT} notation.
	 *
	 * @return the constraint left delimiter notation
	 *
	 * @since 1.0
	 */
	public static String getConstraintDelimiterLeft() {
		return CONSTRAINT_DELIMITER_LEFT;
	}

	/**
	 * Returns the {@link #CONSTRAINT_DELIMITER_RIGHT} notation.
	 *
	 * @return the constraint right delimiter notation
	 *
	 * @since 1.0
	 */
	public static String getConstraintDelimiterRight() {
		return CONSTRAINT_DELIMITER_RIGHT;
	}

	/**
	 * Returns the {@link #GLOBAL_ANNOTATION_ID} notation.
	 *
	 * @return the global annotation ID notation
	 *
	 * @since 1.0
	 */
	public static String getGlobalAnnotationId() {
		return GLOBAL_ANNOTATION_ID;
	}

	/**
	 * Returns the {@link #WF_NAME_SENTENCE} notation.
	 *
	 * @return the worflow's name notation
	 *
	 * @since 1.0
	 */
	public static String getWfNameSentence() {
		return WF_NAME_SENTENCE;
	}

	/**
	 * Returns the {@link #META_REFERENCE_SENTENCE} notation.
	 *
	 * @return the referred meta-workflow notation
	 *
	 * @since 1.0
	 */
	public static String getMetaReferenceSentence() {
		return META_REFERENCE_SENTENCE;
	}

	/**
	 * Returns the {@link #DATA_REFERENCE_SENTENCE} notation.
	 *
	 * @return the referred data notation
	 *
	 * @since 1.0
	 */
	public static String getDataReferenceSentence() {
		return DATA_REFERENCE_SENTENCE;
	}

	/**
	 * Returns the {@link #AUTHOR_REFERENCE_SENTENCE} notation.
	 *
	 * @return the referred author/article notation
	 *
	 * @since 1.0
	 */
	public static String getAuthorReferenceSentence() {
		return AUTHOR_REFERENCE_SENTENCE;
	}

	/**
	 * Returns the {@link #OPTIONALITY} keyword notation.
	 *
	 * @return the optionality keyword notation
	 *
	 * @since 1.0
	 */
	public static String getOptionality() {
		return OPTIONALITY;
	}

	/**
	 * Returns the {@link #CATEGORY_PREFIX_VOC} keyword notation.
	 *
	 * @return the category prefix notation
	 *
	 * @since 1.0
	 */
	public static String getCategoryPrefixVoc() {
		return CATEGORY_PREFIX_VOC;
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

	public static List<String> getGlobalAnnotationDefaultContent() {
		return Arrays.asList(WF_NAME_SENTENCE, META_REFERENCE_SENTENCE, DATA_REFERENCE_SENTENCE,
				AUTHOR_REFERENCE_SENTENCE);
	}

}
