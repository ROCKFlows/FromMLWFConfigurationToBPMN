package com.ml2wf.generation;

import com.ml2wf.testutils.assertions.ForEachTaskAssertion;
import com.ml2wf.testutils.XMLTestBase;
import com.ml2wf.util.Pair;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.factory.IWorkflowFactory;
import com.ml2wf.v2.xml.XMLWorkflowFactory;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowInstantiation extends XMLTestBase {

    private final IWorkflowFactory workflowFactory = new XMLWorkflowFactory();

    private Pair<Workflow, Workflow> getReferenceInstanceWorkflows(File file) {
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

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation updates the workflow's tasks names as expected")
    void testInstantiationUpdatesNames(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    assertEquals(instantiatedTask.getName().replace("_TODO", ""), referenceTask.getName());
                })
                .afterTasksIteration((referenceProcessIterator, instanceProcessIterator) ->
                    assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .afterProcessesIteration((referenceInstanceProcess, referenceInstanceWorkflows) ->
                    assertFalse(referenceInstanceProcess.hasNext() || referenceInstanceWorkflows.hasNext())
                )
                .build()
                .verify();
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation does not impact the iteration")
    void testInstantiationDoesNotImpactIteration(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    // note that we don't test the tasks' names as it is not the purpose of this test
                })
                .afterTasksIteration((referenceProcessIterator, instanceProcessIterator) ->
                        assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .afterProcessesIteration((referenceInstanceProcess, referenceInstanceWorkflows) ->
                        assertFalse(referenceInstanceProcess.hasNext() || referenceInstanceWorkflows.hasNext())
                )
                .build()
                .verify();
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation does not loose the documentation")
    void testInstantiationDoesNotLooseTheDocumentation(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    if (referenceTask.getDocumentation() != null) {
                        assertEquals(referenceTask.getDocumentation().getId(),
                                instantiatedTask.getDocumentation().getId());
                        assertTrue(instantiatedTask.getDocumentation().getContent()
                                .contains(referenceTask.getDocumentation().getContent()));
                    }
                })
                .afterTasksIteration((referenceProcessIterator, instanceProcessIterator) ->
                        assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .afterProcessesIteration((referenceInstanceProcess, referenceInstanceWorkflows) ->
                        assertFalse(referenceInstanceProcess.hasNext() || referenceInstanceWorkflows.hasNext())
                )
                .build()
                .verify();
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation sets the right references")
    void testInstantiationSetsTheRightReferences(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    assertNotNull(instantiatedTask.getDocumentation());
                    assertTrue(instantiatedTask.getDocumentation().getContent()
                            .contains(String.format("refersTo: %s", referenceTask.getName())));
                })
                .afterTasksIteration((referenceProcessIterator, instanceProcessIterator) ->
                        assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .afterProcessesIteration((referenceInstanceProcess, referenceInstanceWorkflows) ->
                        assertFalse(referenceInstanceProcess.hasNext() || referenceInstanceWorkflows.hasNext())
                )
                .build()
                .verify();
    }
}
