package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestWorkflowProcessesRemoval extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an instance workflow process from an instance workflow.")
    void testInstanceWorkflowRemoveProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            workflow.removeChild(workflowProcess);
            assertFalse(workflow.getChildren().contains(workflowProcess));
        }
        assertTrue(workflow.getChildren().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown process from an instance workflow.")
    void testInstanceWorkflowRemoveUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process unknownProcess = new Process("1545165781", "dz51sf4sq", new ArrayList<>(), new ArrayList<>());
        Optional<Process> optRemovedProcess = workflow.removeChild(unknownProcess);
        assertTrue(optRemovedProcess.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of a meta workflow process from a meta-workflow workflow.")
    void testMetaWorkflowRemoveProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            workflow.removeChild(workflowProcess);
            assertFalse(workflow.getChildren().contains(workflowProcess));
        }
        assertTrue(workflow.getChildren().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of an unknown process from a meta-workflow workflow.")
    void testMetaWorkflowRemoveUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process unknownProcess = new Process("1545165781", "dz51sf4sq", new ArrayList<>(), new ArrayList<>());
        Optional<Process> optRemovedProcess = workflow.removeChild(unknownProcess);
        assertTrue(optRemovedProcess.isEmpty());
    }
}
