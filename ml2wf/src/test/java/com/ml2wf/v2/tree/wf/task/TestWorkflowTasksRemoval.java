package com.ml2wf.v2.tree.wf.task;

import com.ml2wf.v2.testutils.assertions.tree.wf.XMLWorkflowTestBase;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowTasksRemoval extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an instance workflow task from an instance workflow's process.")
    void testInstanceWorkflowRemoveTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : workflowProcesses) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getChildren())) {
                assertTrue(workflowProcess.getChildren().contains(task));
                workflowProcess.removeChild(task);
                assertFalse(workflowProcess.getChildren().contains(task));
            }
            assertTrue(workflowProcess.getChildren().isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown instance workflow task from an instance workflow's process.")
    void testInstanceWorkflowRemoveUnknownTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        WorkflowTask unknownWorkflow = WorkflowTask.WorkflowTaskFactory.createTask("UNKNOWN TASK dzs41514sq");
        Optional<WorkflowTask> optRemovedTask = new ArrayList<>(workflow.getChildren()).get(0)
                .removeChild(unknownWorkflow);
        assertTrue(optRemovedTask.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of an instance workflow task from a meta-workflow's process.")
    void testMetaWorkflowRemoveTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : workflowProcesses) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getChildren())) {
                assertTrue(workflowProcess.getChildren().contains(task));
                workflowProcess.removeChild(task);
                assertFalse(workflowProcess.getChildren().contains(task));
            }
            assertTrue(workflowProcess.getChildren().isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the removal of an unknown instance workflow task from a meta-workflow's process.")
    void testMetaWorkflowRemoveDuplicatedTaskFromProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        WorkflowTask unknownWorkflow = WorkflowTask.WorkflowTaskFactory.createTask("UNKNOWN TASK dzs41514sq");
        Optional<WorkflowTask> optRemovedTask = new ArrayList<>(workflow.getChildren()).get(0)
                .removeChild(unknownWorkflow);
        assertTrue(optRemovedTask.isEmpty());
    }
}
