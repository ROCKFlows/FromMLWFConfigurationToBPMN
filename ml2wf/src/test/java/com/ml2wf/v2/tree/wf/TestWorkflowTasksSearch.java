package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestWorkflowTasksSearch extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the search of a known instance workflow task from an instance workflow.")
    void testInstanceWorkflowKnownSearchTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getChildren())) {
                assertTrue(workflowProcess.getChildren().contains(task));
                Optional<WorkflowTask> optTask = workflowProcess.getChildWithIdentity(task.getIdentity());
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
                optTask = workflowProcess.getChildMatching(t -> t.getName().equals(task.getName()));
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown workflow task from an instance workflow.")
    void testInstanceWorkflowRemoveUnknownTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process process = new ArrayList<>(workflow.getChildren()).get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = process.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow task from a meta-workflow workflow.")
    void testMetaWorkflowSearchKnownTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getChildren())) {
                assertTrue(workflowProcess.getChildren().contains(task));
                Optional<WorkflowTask> optTask = workflowProcess.getChildWithIdentity(task.getIdentity());
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
                optTask = workflowProcess.getChildMatching(t -> t.getName().equals(task.getName()));
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an known workflow task from a meta-workflow workflow.")
    void testMetaWorkflowSearchUnknownTaskFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Process process = new ArrayList<>(workflow.getChildren()).get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = process.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }

    // after instantiation

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow task after instantiation of the meta-workflow.")
    void testMetaWorkflowSearchKnownTaskFromWorkflowAfterInstantiation(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.instantiate();
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            for (WorkflowTask task : new ArrayList<>(workflowProcess.getChildren())) {
                assertTrue(workflowProcess.getChildren().contains(task));
                Optional<WorkflowTask> optTask = workflowProcess.getChildWithIdentity(task.getIdentity());
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
                optTask = workflowProcess.getChildMatching(t -> t.getName().equals(task.getName()));
                assertTrue(optTask.isPresent());
                assertEquals(task, optTask.get());
            }
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an known workflow task after instantiation of the meta-workflow.")
    void testMetaWorkflowSearchUnknownTaskFromWorkflowAfterInstantiation(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.instantiate();
        Process process = new ArrayList<>(workflow.getChildren()).get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = process.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }
}
