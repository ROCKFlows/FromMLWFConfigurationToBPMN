package com.ml2wf.conventions;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.ml2wf.generation.InstanceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Notation {

    /**
     * The <b>generated prefix</b> notation.
     */
    public static final String GENERATED_PREFIX_VOC = "#";
    /**
     * The <b>instance</b> notation.
     */
    public static final String INSTANCE_VOC = "_instance";
    /**
     * The <b>reference</b> notation.
     */
    public static final String REFERENCE_VOC = "refersTo: ";
    /**
     * The <b>generic</b> notation.
     */
    public static final String GENERIC_VOC = "_Step";
    /**
     * The <b>documentation</b> notation.
     */
    public static final String DOCUMENTATION_VOC = "Documentation_";
    /**
     * The <b>text annotation</b> notation.
     */
    public static final String TEXT_ANNOTATION_VOC = "TextAnnotation_";
    /**
     * The <b>BPMN shape</b> notation.
     */
    public static final String BPMN_SHAPE_VOC = "BPMNShape_";
    /**
     * The <b>BPMN property</b> perfix.
     */
    public static final String BPMN_PROPERTY_PREFIX = "Property_";
    public static final String TASK_ID_PREFIX = "Task_";
    /**
     * The <b>BPMN "instance" property</b> notation.
     */
    public static final String BPMN_PROPERTY_INSTANCE = "instance";
    /**
     * The <b>back up</b> notation.
     */
    public static final String BACKUP_VOC = "_save";
    /**
     * The <b>left delimiter</b> for the workflow's references section.
     */
    public static final String REFERENCES_DELIMITER_LEFT = "{{";
    /**
     * The <b>right delimiter</b> for the workflow's references section.
     */
    public static final String REFERENCES_DELIMITER_RIGHT = "}}";
    /**
     * The <b>left delimiter</b> for the constraint section.
     */
    public static final String CONSTRAINT_DELIMITER_LEFT = "[[";
    /**
     * The <b>right delimiter</b> for the constraint section.
     */
    public static final String CONSTRAINT_DELIMITER_RIGHT = "]]";
    /**
     * The <b>ID</b> for the global annotation node.
     */
    public static final String GLOBAL_ANNOTATION_ID = "Global_Annotation";
    /**
     * The sentence for workflow's name.
     */
    public static final String WF_NAME_SENTENCE = "Workflow's name %s%s.";
    /**
     * The sentence for referred meta-workflow.
     */
    public static final String META_REFERENCE_SENTENCE = "Meta-Workflow : %s%s%s.";
    /**
     * The sentence for referred data.
     */
    public static final String DATA_REFERENCE_SENTENCE = "Data : %s%s.";
    /**
     * The sentence for referred author/article.
     */
    public static final String AUTHOR_REFERENCE_SENTENCE = "Author/Article : %s%s.";
    /**
     * The optionality keyword.
     */
    public static final String OPTIONALITY = "@Optional";
    /**
     * The <b>category prefix</b> notation.
     */
    public static final String CATEGORY_PREFIX_VOC = "$";

    /**
     * Returns the quoted notation using the {@link Pattern#quote(String)} method.
     *
     * @return the quoted notation using the {@link Pattern#quote(String)} method
     *
     * @since 1.0
     * @see Pattern
     */
    public static String getQuotedNotation(String notation) {
        String[] split = notation.split("");
        return Pattern.quote(split[0]).repeat(split.length);
    }

    public static List<String> getGlobalAnnotationDefaultContent() {
        return Arrays.asList(WF_NAME_SENTENCE, META_REFERENCE_SENTENCE, DATA_REFERENCE_SENTENCE,
                AUTHOR_REFERENCE_SENTENCE);
    }
}
