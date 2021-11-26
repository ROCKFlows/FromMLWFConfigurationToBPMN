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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestWorkflowTasksSearch extends XMLWorkflowTestBase {

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the search of a known instance workflow task from an instance workflow.")
    void testInstanceWorkflowRemoveTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getTasks())) {
                assertTrue(workflowProcess.getTasks().contains(task));
                Optional<WorkflowTask> optTask = workflowProcess.getChildWithName(task.getName());
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
            }
        }
    }

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown workflow task from an instance workflow.")
    void testInstanceWorkflowRemoveUnknownTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process process = workflow.getChildren().get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithName("unknown workflow name");
        assertTrue(optSearchedProcess.isEmpty());
    }

    // same tests for meta workflow

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow task from a meta-workflow workflow.")
    void testMetaWorkflowSearchTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getTasks())) {
                assertTrue(workflowProcess.getTasks().contains(task));
                Optional<WorkflowTask> optTask = workflowProcess.getChildWithName(task.getName());
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
            }
        }
    }

    @Disabled("TODO: waiting for internalMemory implementation")
    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an workflow task from a meta-workflow workflow.")
    void testMetaWorkflowSearchTaskProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process process = workflow.getChildren().get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithName("unknown workflow name");
        assertTrue(optSearchedProcess.isEmpty());
    }
}
