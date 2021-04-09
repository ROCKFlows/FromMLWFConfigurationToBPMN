package com.ml2wf.constraints.config;

/**
 * This interface provides a method for loading a configuration from a
 * configuration file.
 *
 * @author Nicolas Lacroix
 *
 * @version 1.0
 */
@FunctionalInterface
public interface Config {

    /**
     * Reads a configuration file.
     *
     * @since 1.0
     */
    void readConfig();
}
