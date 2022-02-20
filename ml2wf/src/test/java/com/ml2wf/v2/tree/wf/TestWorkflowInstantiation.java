package com.ml2wf.v2.tree.wf;

import com.ml2wf.conventions.Notation;
import com.ml2wf.v2.testutils.assertions.tree.wf.XMLWorkflowTestBase;
import com.ml2wf.v2.testutils.assertions.tree.wf.ForEachProcessAssertion;
import com.ml2wf.v2.testutils.assertions.tree.wf.ForEachWFTaskAssertion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowInstantiation extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation updates the workflow's tasks names as expected")
    void testInstantiationUpdatesWorkflowsNames(File file) {
        ForEachWFTaskAssertion.builder()
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
    @DisplayName("Testing that the instantiation does not update the workflow's processes names")
    void testInstantiationDoesNotUpdateProcessesNames(File file) {
        ForEachProcessAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachProcess((referenceProcess, instantiatedProcess) -> {
                    assertEquals(referenceProcess.getId(), instantiatedProcess.getId());
                    assertEquals(referenceProcess.getName(), instantiatedProcess.getName());
                })
                .afterProcessesIteration((referenceProcessIterator, instanceProcessIterator) ->
                        assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .build()
                .verify();
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing that the instantiation does not impact the iteration")
    void testInstantiationDoesNotImpactIteration(File file) {
        ForEachWFTaskAssertion.builder()
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
        ForEachWFTaskAssertion.builder()
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
        ForEachWFTaskAssertion.builder()
                .workflowPair(getReferenceInstanceWorkflows(file))
                .forEachTask((referenceTask, instantiatedTask) -> {
                    assertEquals(referenceTask.getId(), instantiatedTask.getId());
                    assertNotNull(instantiatedTask.getDocumentation());
                    assertTrue(instantiatedTask.getDocumentation().getContent()
                            .contains(Notation.REFERENCE_VOC + referenceTask.getName()));
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
