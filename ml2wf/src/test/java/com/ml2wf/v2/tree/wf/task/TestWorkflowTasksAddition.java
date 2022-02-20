package com.ml2wf.v2.tree.wf.task;

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
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TestWorkflowTasksAddition extends XMLWorkflowTestBase {

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the addition of an instance workflow task to an instance workflow's process.")
    void testInstanceWorkflowAddTaskToProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        assertFalse(workflowProcesses.isEmpty(), "Unexpected workflow processes list size.");
        // adding to first process
        Process process = new ArrayList<>(workflowProcesses).get(0);
        WorkflowTask taskToAddA = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskA = process.appendChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(process.getChildren().contains(taskToAddA));
    }

    @ParameterizedTest
    @MethodSource("instancesFiles")
    @DisplayName("Testing the addition of a duplicated instance workflow task to an instance workflow's process.")
    void testInstanceWorkflowAddDuplicatedTaskToProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        assertFalse(workflowProcesses.isEmpty(), "Unexpected workflow processes list size.");
        // adding to first process
        Process process = new ArrayList<>(workflowProcesses).get(0);
        WorkflowTask taskToAddA = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskA = process.appendChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(process.getChildren().contains(taskToAddA));
        // adding a second task with same name is accepted
        WorkflowTask taskToAddADuplicate = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskADuplicate = process.appendChild(taskToAddADuplicate);
        assertTrue(addedTaskADuplicate.isRight());
        assertSame(taskToAddADuplicate, addedTaskADuplicate.get());
        assertSame(taskToAddADuplicate, addedTaskADuplicate.get());
        assertEquals(2L, process.getChildren().stream()
                .filter(p -> p.getName().equals(taskToAddADuplicate.getName()))
                .count());
    }

    // same tests for meta workflow

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the addition of an meta workflow task to a meta-workflow's process.")
    void testMetaWorkflowAddTaskToProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        assertFalse(workflowProcesses.isEmpty(), "Unexpected workflow processes list size.");
        // adding to first process
        Process process = new ArrayList<>(workflowProcesses).get(0);
        WorkflowTask taskToAddA = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskA = process.appendChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(process.getChildren().contains(taskToAddA));
    }

    @ParameterizedTest
    @MethodSource("metaFiles")
    @DisplayName("Testing the addition of a duplicated meta workflow task to a meta-workflow's process.")
    void testMetaWorkflowAddDuplicatedTaskToProcess(File file) {
        Workflow workflow = getWorkflowFromFile(file);
        Collection<Process> workflowProcesses = workflow.getChildren();
        assertFalse(workflowProcesses.isEmpty(), "Unexpected workflow processes list size.");
        // adding to first process
        Process process = new ArrayList<>(workflowProcesses).get(0);
        WorkflowTask taskToAddA = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskA = process.appendChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(process.getChildren().contains(taskToAddA));
        // adding a second task with same name is accepted
        WorkflowTask taskToAddADuplicate = WorkflowTask.WorkflowTaskFactory.createTask("ADDED_A");
        Either<String, WorkflowTask> addedTaskADuplicate = process.appendChild(taskToAddADuplicate);
        assertTrue(addedTaskADuplicate.isRight());
        assertSame(taskToAddADuplicate, addedTaskADuplicate.get());
        assertSame(taskToAddADuplicate, addedTaskADuplicate.get());
        assertEquals(2L, process.getChildren().stream()
                .filter(p -> p.getName().equals(taskToAddADuplicate.getName()))
                .count());
    }
}
