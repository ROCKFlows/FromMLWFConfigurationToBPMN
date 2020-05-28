package com.ml2wf.generation;

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
	 * Instantiates a generic workflow.
	 *
	 * @param resultPath result path
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void getWFInstance(String resultPath) throws Exception; // TODO: check parameters
}
