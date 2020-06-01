package com.ml2wf.constraints.config;

/**
 * This interface provides a method for loading a configuration from a
 * configuration file.
 * 
 * @author Nicolas Lacroix
 *
 * @version 1.0
 *
 */
public interface Config {

	/**
	 * Reads the configuration file at the {@code filePath} location and initializes
	 * the {@code vocMapping Map} using the {@link #processEntry(String[])} method.
	 *
	 * @since 1.0
	 */
	public void readConfig();
}
