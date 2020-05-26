package com.ml2wf.conventions;

import com.ml2wf.generation.InstanceFactory;

/**
 * This class is a <b>Data class</b> containing the notation used for the
 * {@linkplain InstanceFactory instances generation}.
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
	 * {@code Notation's} default constructor.
	 *
	 * <p>
	 *
	 * <b>Note</b> that this constructor is <code>private</code>.
	 */
	private Notation() {

	}

	/**
	 * Returns the <b>generated prefix</b> notation.
	 *
	 * @return the generated prefix notation
	 *
	 * @since 1.0
	 */
	public static String getGeneratedPrefixVoc() {
		return GENERATED_PREFIX_VOC;
	}

	/**
	 * Returns the <b>instance</b> notation.
	 *
	 * @return the instance notation
	 *
	 * @since 1.0
	 */
	public static String getInstanceVoc() {
		return INSTANCE_VOC;
	}

	/**
	 * Returns the <b>reference</b> notation.
	 *
	 * @return the reference notation
	 *
	 * @since 1.0
	 */
	public static String getReferenceVoc() {
		return REFERENCE_VOC;
	}

	/**
	 * Returns the <b>generic</b> notation.
	 *
	 * @return the generic notation
	 *
	 * @since 1.0
	 */
	public static String getGenericVoc() {
		return GENERIC_VOC;
	}

	/**
	 * Returns the <b>documentation</b> notation.
	 *
	 * @return the documentation notation
	 *
	 * @since 1.0
	 */
	public static String getDocumentationVoc() {
		return DOCUMENTATION_VOC;
	}

}
