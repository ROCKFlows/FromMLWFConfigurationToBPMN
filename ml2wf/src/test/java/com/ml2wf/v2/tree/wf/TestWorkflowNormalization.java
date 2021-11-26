package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import com.ml2wf.v2.testutils.assertions.tree.wf.ForEachTaskAssertion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowNormalization extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the normalization updates the meta workflow's tasks names as expected")
    void testNormalizationUpdatesNamesForMetaWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    assertFalse(instantiatedTask.getName().contains(" "));
                    assertEquals(referenceTask.getName().replace(" ", "_"),
                            instantiatedTask.getName());
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
    @MethodSource("instancesFiles")
    @DisplayName("Testing that the normalization updates the instance workflow's tasks names as expected")
    void testNormalizationUpdatesNamesForInstanceWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    assertFalse(instantiatedTask.getName().contains(" "));
                    assertEquals(referenceTask.getName().replace(" ", "_"),
                            instantiatedTask.getName());
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
    @DisplayName("Testing that the normalization does not impact the meta workflow iteration")
    void testNormalizationDoesNotImpactIterationForMetaWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
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
    @MethodSource("instancesFiles")
    @DisplayName("Testing that the normalization does not impact the instance workflow iteration")
    void testNormalizationDoesNotImpactIterationForInstanceWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
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
    @DisplayName("Testing that the normalization does not impact the meta workflow documentation")
    void testNormalizationDoesNotImpactTheDocumentationForMetaWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
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
    @MethodSource("instancesFiles")
    @DisplayName("Testing that the normalization does not impact the meta instance documentation")
    void testNormalizationDoesNotImpactTheDocumentationForInstanceWorkflows(File file) {
        ForEachTaskAssertion.builder()
                .workflowPair(getReferenceNormalizedWorkflows(file))
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
}
