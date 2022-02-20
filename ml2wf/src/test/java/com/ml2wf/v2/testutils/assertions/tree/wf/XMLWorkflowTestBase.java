package com.ml2wf.v2.testutils.assertions.tree.wf;

import com.ml2wf.util.FileHandler;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.testutils.TreeTestBase;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.factory.IWorkflowFactory;
import com.ml2wf.v2.xml.XMLWorkflowFactory;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

public class XMLWorkflowTestBase extends TreeTestBase<IWorkflowFactory> {

    /**
     * Meta workflows' directory.
     */
    protected static final String META_DIRECTORY = "./wf_meta/";
    /**
     * Instance workflows' directory.
     */
    protected static final String INSTANCES_DIRECTORY = "./wf_instances/";

    /**
     * {@code XMLWorkflowTestBase}'s default constructor.
     */
    public XMLWorkflowTestBase() {
        super(new XMLWorkflowFactory());
    }

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
        File instanceDir = new File(Objects.requireNonNull(classLoader.getResource(META_DIRECTORY)).toURI());
        return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
    }

    /**
     * Returns a {@code Stream} containing all instance-workflows located under the
     * {@link #INSTANCES_DIRECTORY} directory.
     *
     * @return a {@code Stream} containing all instance-workflows located under the
     *         {@link #INSTANCES_DIRECTORY} directory
     *
     * @throws URISyntaxException
     *
     * @see FileUtils
     */
    protected static Stream<File> instancesFiles() throws URISyntaxException {
        File instanceDir = new File(
                Objects.requireNonNull(classLoader.getResource(INSTANCES_DIRECTORY)).toURI());
        return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
    }

    protected Pair<Workflow, Workflow> getReferenceInstanceWorkflows(File file) {
        Workflow workflowToNormalize = getWorkflowFromFile(file);
        workflowToNormalize.instantiate();
        return new Pair<>(getWorkflowFromFile(file), workflowToNormalize);
    }

    protected Pair<Workflow, Workflow> getReferenceNormalizedWorkflows(File file) {
        Workflow workflowToInstantiate = getWorkflowFromFile(file);
        workflowToInstantiate.normalize();
        return new Pair<>(getWorkflowFromFile(file), workflowToInstantiate);
    }

    @SneakyThrows
    protected Workflow getWorkflowFromFile(File file) {
        Try<Workflow> tryWorkflow = factory.workflowFromFile(file);
        return tryWorkflow.getOrElseThrow(f -> f);
    }
}
