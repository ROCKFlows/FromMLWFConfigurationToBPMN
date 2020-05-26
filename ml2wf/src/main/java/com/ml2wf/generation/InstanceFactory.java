package com.ml2wf.generation;

/**
 * This interface defines the main methods for an {@code InstanceFactory}
 * implementation.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public interface InstanceFactory {

	/**
	 * Instantiates a workflow.
	 *
	 * @throws Exception
	 *
	 * @since 1.0
	 */
	public void getWFInstance() throws Exception;
}
