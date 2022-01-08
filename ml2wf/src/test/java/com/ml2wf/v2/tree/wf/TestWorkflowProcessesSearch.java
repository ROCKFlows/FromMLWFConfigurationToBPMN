package com.ml2wf.v2.tree.wf;

import com.ml2wf.v2.testutils.XMLWorkflowTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowProcessesSearch extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the search of a known instance workflow process from an instance workflow.")
    void testInstanceWorkflowSearchProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithIdentity(workflowProcess.getIdentity());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
            optProcess = workflow.getChildMatching(p -> p.getName().equals(workflowProcess.getName()));
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown process from an instance workflow.")
    void testInstanceWorkflowSearchUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Optional<Process> optSearchedProcess = workflow.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = workflow.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow process from a meta-workflow workflow.")
    void testMetaWorkflowSearchProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithIdentity(workflowProcess.getIdentity());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
            optProcess = workflow.getChildMatching(p -> p.getName().equals(workflowProcess.getName()));
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an unknown process from a meta-workflow workflow.")
    void testMetaWorkflowSearchUnknownProcessFromWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Optional<Process> optSearchedProcess = workflow.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = workflow.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }

    // after normalization

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the search of a known instance workflow process from an instance workflow after normalization.")
    void testInstanceWorkflowSearchProcessFromWorkflowAfterNormalization(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.normalize();
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithIdentity(workflowProcess.getIdentity());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
            optProcess = workflow.getChildMatching(p -> p.getName().equals(workflowProcess.getName()));
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the removal of an unknown process from an instance workflow after normalization.")
    void testInstanceWorkflowSearchUnknownProcessFromWorkflowAfterNormalization(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.normalize();
        Optional<Process> optSearchedProcess = workflow.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = workflow.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of a known workflow process from a meta-workflow workflow after normalization.")
    void testMetaWorkflowSearchProcessFromWorkflowAfterNormalization(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.normalize();
        Collection<Process> workflowProcesses = workflow.getChildren();
        for (Process workflowProcess : new ArrayList<>(workflowProcesses)) {
            assertTrue(workflow.getChildren().contains(workflowProcess));
            Optional<Process> optProcess = workflow.getChildWithIdentity(workflowProcess.getIdentity());
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
            optProcess = workflow.getChildMatching(p -> p.getName().equals(workflowProcess.getName()));
            assertTrue(optProcess.isPresent());
            assertEquals(workflowProcess, optProcess.get());
        }
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the search of an unknown process from a meta-workflow workflow after normalization.")
    void testMetaWorkflowSearchUnknownProcessFromWorkflowAfterNormalization(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        workflow.normalize();
        Optional<Process> optSearchedProcess = workflow.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = workflow.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());
    }
}
