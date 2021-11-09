package com.ml2wf.testutils;

import com.ml2wf.AbstractXMLTest;
import com.ml2wf.util.FileHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class XMLTestBase {

    /**
     * Meta workflows' directory.
     */
    protected static final String META_DIRECTORY = "./wf_meta/";

    /**
     * {@code ClassLoader}'s instance used to get resources.
     *
     * @see ClassLoader
     */
    protected static ClassLoader classLoader = AbstractXMLTest.class.getClassLoader();

    /**
     * Returns a {@code Stream} containing all meta-workflows located under the
     * {@link #META_DIRECTORY} directory.
     *
     * @return a {@code Stream} containing all meta-workflows located under the
     *         {@link #META_DIRECTORY} directory
     *
     * @throws URISyntaxException
     *
     * @see FileUtils
     */
    protected static Stream<File> metaFiles() throws URISyntaxException {
        File instanceDir = new File(
                Objects.requireNonNull(classLoader.getResource(META_DIRECTORY)).toURI());
        return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
    }
}
