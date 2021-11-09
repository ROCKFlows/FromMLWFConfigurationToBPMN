package com.ml2wf.v2.testutils;

import com.ml2wf.util.FileHandler;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.xml.XMLWorkflowFactory;
import io.vavr.control.Try;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLWorkflowTestBase extends TreeTestBase {

    /**
     * Meta workflows' directory.
     */
    protected static final String META_DIRECTORY = "./wf_meta/";

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
        File instanceDir = new File(
                Objects.requireNonNull(classLoader.getResource(META_DIRECTORY)).toURI());
        return new HashSet<>(FileUtils.listFiles(instanceDir, FileHandler.getWfExtensions(), true)).stream();
    }

    protected Pair<Workflow, Workflow> getReferenceInstanceWorkflows(File file) {
        Try<Workflow> tryReferenceWorkflow = workflowFactory.workflowFromFile(file);
        assertTrue(tryReferenceWorkflow.isSuccess());
        // the workflow to instantiate that will be compared to the reference one
        Try<Workflow> tryWorkflowToInstantiate = workflowFactory.workflowFromFile(file);
        assertTrue(tryWorkflowToInstantiate.isSuccess());
        Workflow workflowToInstantiate = tryWorkflowToInstantiate.get();
        // instantiating
        workflowToInstantiate.instantiate();
        return new Pair<>(tryReferenceWorkflow.get(), workflowToInstantiate);
    }

    protected Pair<Workflow, Workflow> getReferenceNormalizedWorkflows(File file) {
        Try<Workflow> tryReferenceWorkflow = workflowFactory.workflowFromFile(file);
        assertTrue(tryReferenceWorkflow.isSuccess());
        // the workflow to instantiate that will be compared to the reference one
        Try<Workflow> tryWorkflowToInstantiate = workflowFactory.workflowFromFile(file);
        assertTrue(tryWorkflowToInstantiate.isSuccess());
        Workflow workflowToInstantiate = tryWorkflowToInstantiate.get();
        // instantiating
        workflowToInstantiate.normalize();
        return new Pair<>(tryReferenceWorkflow.get(), workflowToInstantiate);
    }
}
