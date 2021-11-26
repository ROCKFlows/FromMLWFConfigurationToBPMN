package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import com.ml2wf.v2.testutils.assertions.tree.wf.ForEachProcessAssertion;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowTasksRemoval extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an instance workflow task from an instance workflow's process.")
    void testInstanceWorkflowRemoveTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : workflowProcesses) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getTasks())) {
                assertTrue(workflowProcess.getTasks().contains(task));
                workflowProcess.removeChild(task);
                assertFalse(workflowProcess.getTasks().contains(task));
            }
            assertTrue(workflowProcess.getTasks().isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown instance workflow task from an instance workflow's process.")
    void testInstanceWorkflowRemoveUnknownTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        WorkflowTask unknownWorkflow = WorkflowTask.WorkflowTaskFactory.createTask("UNKNOWN TASK dzs41514sq");
        Optional<WorkflowTask> optRemovedTask = workflow.getChildren().get(0).removeChild(unknownWorkflow);
        assertTrue(optRemovedTask.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of an instance workflow task from a meta-workflow's process.")
    void testMetaWorkflowRemoveTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        List<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : workflowProcesses) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getTasks())) {
                assertTrue(workflowProcess.getTasks().contains(task));
                workflowProcess.removeChild(task);
                assertFalse(workflowProcess.getTasks().contains(task));
            }
            assertTrue(workflowProcess.getTasks().isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of an unknown instance workflow task from a meta-workflow's process.")
    void testMetaWorkflowRemoveDuplicatedTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        WorkflowTask unknownWorkflow = WorkflowTask.WorkflowTaskFactory.createTask("UNKNOWN TASK dzs41514sq");
        Optional<WorkflowTask> optRemovedTask = workflow.getChildren().get(0).removeChild(unknownWorkflow);
        assertTrue(optRemovedTask.isEmpty());
    }
}
