package com.ml2wf.generation;

import java.io.File;

/**
 * This interface provides a method for the <b>instantiation of a generic
 * workflow</b>.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 * @see <a href="https://www.bpmn.org/">BPMN</a>
 *
 */
public interface InstanceFactory {

	/**
	 * Instantiates a generic workflow and saves it under the given
	 * {@code ouputDir}.
	 *
	 * @param outputDir the output directory
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void getWFInstance(File outputDir) throws Exception; // TODO: check parameters
}
