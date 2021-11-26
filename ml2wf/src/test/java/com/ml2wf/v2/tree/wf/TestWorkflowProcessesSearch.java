package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowProcessesSearch extends XMLWorkflowTestBase {

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the search of a known instance workflow process from an instance workflow.")
    void testInstanceWorkflowRemoveProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithName(workflowProcess.getName());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown process from an instance workflow.")
    void testInstanceWorkflowRemoveUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Optional<Process> optSearchedProcess = workflow.getChildWithName("unknown workflow name");
        assertTrue(optSearchedProcess.isEmpty());
    }

    // same tests for meta workflow

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow process from a meta-workflow workflow.")
    void testMetaWorkflowSearchProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithName(workflowProcess.getName());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an unknown process from a meta-workflow workflow.")
    void testMetaWorkflowSearchUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Optional<Process> optSearchedProcess = workflow.getChildWithName("unknown workflow name");
        assertTrue(optSearchedProcess.isEmpty());
    }
}
