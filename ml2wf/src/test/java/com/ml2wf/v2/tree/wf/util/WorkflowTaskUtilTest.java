package com.ml2wf.v2.tree.wf.util;

import com.ml2wf.conventions.Notation;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowTaskUtilTest {

    // TODO: use generators to test more cases

    // "is optional" tests

    @Test
    void testIsOptional_noDocumentation() {
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1");
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
    }

    @Test
    void testIsOptional_noDocumentation_optNotationInName() {
        var name = String.format("%s:%s Task_1", Notation.OPTIONALITY, true);
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask(name);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
        name = String.format("%s:%s Task_1", Notation.OPTIONALITY, false);
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask(name);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
    }

    @Test
    void testIsOptional_withDocumentation() {
        var description = "a description";
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1", description);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
        description = "a task that is optional";
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_3", description);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
        description = "a task that is not optional";
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_4", description);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
        description = String.format("a task that is %s:%s", Notation.OPTIONALITY, false);
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_5", description);
        assertFalse(WorkflowTaskUtil.isOptional(wfTask));
        description = String.format("a task that is %s:%s", Notation.OPTIONALITY, true);
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_6", description);
        assertTrue(WorkflowTaskUtil.isOptional(wfTask));
    }

    // "is abstract" tests

    @Test
    void testIsAbstract_noDocumentation() {
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1");
        assertFalse(WorkflowTaskUtil.isAbstract(wfTask));
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1" + Notation.GENERIC_VOC);
        assertTrue(WorkflowTaskUtil.isAbstract(wfTask));
    }

    @Test
    void testIsAbstract_withDocumentation() {
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1", "a description");
        assertFalse(WorkflowTaskUtil.isAbstract(wfTask));
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_2", "a description for Task_B_Step");
        assertFalse(WorkflowTaskUtil.isAbstract(wfTask));
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_3" + Notation.GENERIC_VOC, "a description");
        assertTrue(WorkflowTaskUtil.isAbstract(wfTask));
    }

    // "reference retrieval" tests

    @Test
    void testGetReference_noDocumentation() {
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1");
        assertTrue(WorkflowTaskUtil.getReferenceName(wfTask).isEmpty());
    }

    @Test
    void testGetReference_noDocumentation_referenceNotationInName() {
        var name = String.format("Task_1 %s%s", Notation.REFERENCE_VOC, "MetaTask_A");
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask(name);
        assertTrue(WorkflowTaskUtil.getReferenceName(wfTask).isEmpty());
    }

    @Test
    void testGetReference_withDocumentation() {
        var description = "a description";
        var wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_1", description);
        assertFalse(WorkflowTaskUtil.isAbstract(wfTask));
        description = String.format("a task that refers to %s", "MetaTask_B");
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_2", description);
        assertFalse(WorkflowTaskUtil.isAbstract(wfTask));
        description = String.format("a task that %s%s", Notation.REFERENCE_VOC, "MetaTask_C");
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_3", description);
        Optional<String> optRef = WorkflowTaskUtil.getReferenceName(wfTask);
        assertTrue(optRef.isPresent());
        assertEquals("MetaTask_C", optRef.get());
        description = String.format("a task that %s%s and another information", Notation.REFERENCE_VOC, "MetaTask_D");
        wfTask = WorkflowTask.WorkflowTaskFactory.createTask("Task_4", description);
        optRef = WorkflowTaskUtil.getReferenceName(wfTask);
        assertTrue(optRef.isPresent());
        assertEquals("MetaTask_D", optRef.get());
    }
}
