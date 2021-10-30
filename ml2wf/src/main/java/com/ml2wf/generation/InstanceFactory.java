package com.ml2wf.generation;

import java.io.File;

/**
 * This interface provides a method for the <b>instantiation of a generic
 * workflow</b>.
 *
 * @author Nicolas Lacroix
 *
 * @see <a href="https://www.bpmn.org/">BPMN</a>
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface InstanceFactory {

    /**
     * Instantiates a generic workflow and saves it under the given
     * {@code outputFile}.
     *
     * @param outputFile the output file or directory
     *
     * @throws Exception
     */
    void getWFInstance(File outputFile) throws Exception;
}
