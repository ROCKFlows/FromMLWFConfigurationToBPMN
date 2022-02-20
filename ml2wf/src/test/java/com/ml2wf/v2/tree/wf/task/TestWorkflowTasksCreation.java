package com.ml2wf.v2.tree.wf.task;

import com.ml2wf.conventions.Notation;
import com.ml2wf.v2.testutils.assertions.tree.wf.XMLWorkflowTestBase;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestWorkflowTasksCreation extends XMLWorkflowTestBase {

    @Test
    @DisplayName("Testing the creation of a workflow task with a name.")
    void testCreationWithName() {
        int nbCreatedTasks = WorkflowTask.getTaskCounter();
        String name = "taskA";
        WorkflowTask workflowTask = WorkflowTask.WorkflowTaskFactory.createTask(name);
        assertEquals(name, workflowTask.getName());
        assertEquals(Notation.TASK_ID_PREFIX + (nbCreatedTasks + 1) + name, workflowTask.getId());
        assertEquals(Notation.TASK_ID_PREFIX + (nbCreatedTasks + 1) + name, workflowTask.getIdentity());
        assertNotNull(workflowTask.getDocumentation());
        assertEquals("", workflowTask.getDocumentation().getContent());
    }

    @Test
    @DisplayName("Testing the creation of a workflow task with a name and a description.")
    void testCreationWithNameAndDocumentation() {
        int nbCreatedTasks = WorkflowTask.getTaskCounter();
        String name = "taskA";
        String documentationContent = "a simple documentation";
        WorkflowTask workflowTask = WorkflowTask.WorkflowTaskFactory.createTask(name, documentationContent);
        assertEquals(name, workflowTask.getName());
        assertEquals(Notation.TASK_ID_PREFIX + (nbCreatedTasks + 1) + name, workflowTask.getId());
        assertEquals(Notation.TASK_ID_PREFIX + (nbCreatedTasks + 1) + name, workflowTask.getIdentity());
        assertNotNull(workflowTask.getDocumentation());
        assertEquals(documentationContent, workflowTask.getDocumentation().getContent());
    }
}
