package com.ml2wf.v2.tree.wf.process;

import com.ml2wf.v2.testutils.assertions.tree.wf.XMLWorkflowTestBase;
import com.ml2wf.v2.tree.wf.Process;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowProcessesAddition extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the addition of an empty workflow process to an instance workflow.")
    void testInstanceWorkflowAddEmptyProcessToWorkflow(File file) {
        System.out.println("file = " + file);
        Workflow workflow = getWorkflowFromFile(file);
        System.out.println(workflow.getChildren());
        // adding a new empty process
        Process emptyProcessToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAdded = workflow.appendDirectChild(emptyProcessToAdd);
        assertTrue(emptyProcessAdded.isRight());
        assertSame(emptyProcessToAdd, emptyProcessAdded.get());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd));
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the addition of a duplicated workflow process to an instance workflow should fail.")
    void testInstanceWorkflowAddDuplicatedProcessToWorkflowShouldFail(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        // adding a new empty process
        Process emptyProcessToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAdded = workflow.appendDirectChild(emptyProcessToAdd);
        assertTrue(emptyProcessAdded.isRight());
        assertSame(emptyProcessToAdd, emptyProcessAdded.get());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd));
        // adding a duplicated empty process
        Process emptyProcessToAddDuplicate = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAddedDuplicate = workflow.appendDirectChild(emptyProcessToAddDuplicate);
        assertTrue(emptyProcessAddedDuplicate.isLeft());
        assertEquals("Can't add duplicated process in a workflow.", emptyProcessAddedDuplicate.getLeft());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd)); // should not remove original process
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the addition of an empty workflow process to an instance workflow.")
    void testInstanceWorkflowAddNonEmptyProcessToWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        // adding a new empty process
        Process processToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        processToAdd.appendDirectChild(WorkflowTask.WorkflowTaskFactory.createTask("a task"));
        processToAdd.appendDirectChild(WorkflowTask.WorkflowTaskFactory.createTask("another task"));
        Either<String, Process> processAdded = workflow.appendDirectChild(processToAdd);
        assertTrue(processAdded.isRight());
        assertSame(processToAdd, processAdded.get());
        assertTrue(workflow.getChildren().contains(processToAdd));
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the addition of an empty workflow process to a meta-workflow.")
    void testMetaWorkflowAddEmptyProcessToWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        // adding a new empty process
        Process emptyProcessToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAdded = workflow.appendDirectChild(emptyProcessToAdd);
        assertTrue(emptyProcessAdded.isRight());
        assertSame(emptyProcessToAdd, emptyProcessAdded.get());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd));
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the addition of a duplicated workflow process to a meta-workflow should fail.")
    void testMetaWorkWorkflowAddDuplicatedProcessToWorkflowShouldFail(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        // adding a new empty process
        Process emptyProcessToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAdded = workflow.appendDirectChild(emptyProcessToAdd);
        assertTrue(emptyProcessAdded.isRight());
        assertSame(emptyProcessToAdd, emptyProcessAdded.get());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd));
        // adding a duplicated empty process
        Process emptyProcessToAddDuplicate = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        Either<String, Process> emptyProcessAddedDuplicate = workflow.appendDirectChild(emptyProcessToAddDuplicate);
        assertTrue(emptyProcessAddedDuplicate.isLeft());
        assertEquals("Can't add duplicated process in a workflow.", emptyProcessAddedDuplicate.getLeft());
        assertTrue(workflow.getChildren().contains(emptyProcessToAdd)); // should not remove original process
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the addition of an empty workflow process to a meta-workflow.")
    void testMetaWorkWorkflowAddNonEmptyProcessToWorkflow(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        // adding a new empty process
        Process processToAdd = new Process("id", "name", new ArrayList<>(), new ArrayList<>());
        processToAdd.appendDirectChild(WorkflowTask.WorkflowTaskFactory.createTask("a task"));
        processToAdd.appendDirectChild(WorkflowTask.WorkflowTaskFactory.createTask("another task"));
        Either<String, Process> processAdded = workflow.appendDirectChild(processToAdd);
        assertTrue(processAdded.isRight());
        assertSame(processToAdd, processAdded.get());
        assertTrue(workflow.getChildren().contains(processToAdd));
    }
}
