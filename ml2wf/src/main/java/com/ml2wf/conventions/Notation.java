package com.ml2wf.conventions;

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
	 * <b>back up</b> notation.
	 */
	private static final String BACKUP_VOC = "_save";

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
	 * Returns the {@link #BACKUP_VOC} notation.
	 *
	 * @return the back up notation
	 *
	 * @since 1.0
	 */
	public static String getBackupVoc() {
		return BACKUP_VOC;
	}

}
