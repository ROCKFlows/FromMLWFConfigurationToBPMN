package com.ml2wf.v2.tree.fm.task;

import com.ml2wf.v2.testutils.assertions.tree.fm.XMLFeatureModelTestBase;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestFeatureModelTasksAddition extends XMLFeatureModelTestBase {

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the addition of an instance workflow task to an instance workflow's process.")
    void testInstanceWorkflowAddTaskToProcess(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);
        Collection<FeatureModelTask> featureModelTasks = featureModel.getChildren();
        assertFalse(featureModelTasks.isEmpty(), "Unexpected feature model tasks list size.");
        // adding to root task
        FeatureModelTask rootTask = new ArrayList<>(featureModelTasks).get(0);
        FeatureModelTask taskToAddA = FeatureModelTask.FeatureModelTaskFactory.createTask("ADDED_A", false, false);
        Either<String, FeatureModelTask> addedTaskA = rootTask.appendDirectChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(rootTask.getChildren().contains(taskToAddA));
        // adding to another task (not the root)
        List<FeatureModelTask> subTasks = new ArrayList<>(featureModelTasks).get(0).getChildren();
        if (subTasks.isEmpty()) {
            return; // the file does not contain enough tasks
        }
        FeatureModelTask anotherTask = subTasks.get(0);
        FeatureModelTask taskToAddB = FeatureModelTask.FeatureModelTaskFactory.createTask("ADDED_A", true, false);
        Either<String, FeatureModelTask> addedTaskB = anotherTask.appendDirectChild(taskToAddB);
        assertTrue(addedTaskB.isRight());
        assertSame(taskToAddB, addedTaskB.get());
        assertTrue(anotherTask.getChildren().contains(taskToAddB));
    }

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the addition of a duplicated instance workflow task to an instance workflow's process.")
    void testInstanceWorkflowAddDuplicatedTaskToProcess(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);
        Collection<FeatureModelTask> featureModelTasks = featureModel.getChildren();
        assertFalse(featureModelTasks.isEmpty(), "Unexpected feature model tasks list size.");
        // adding to root task
        FeatureModelTask rootTask = new ArrayList<>(featureModelTasks).get(0);
        FeatureModelTask taskToAddA = FeatureModelTask.FeatureModelTaskFactory.createTask("ADDED_A", false, false);
        Either<String, FeatureModelTask> addedTaskA = rootTask.appendDirectChild(taskToAddA);
        assertTrue(addedTaskA.isRight());
        assertSame(taskToAddA, addedTaskA.get());
        assertTrue(rootTask.getChildren().contains(taskToAddA));
        // adding a second task with same name is not accepted
        FeatureModelTask taskToAddADuplicate = FeatureModelTask.FeatureModelTaskFactory.createTask("ADDED_A", false, true);
        Either<String, FeatureModelTask> addedTaskADuplicate = rootTask.appendDirectChild(taskToAddADuplicate);
        assertTrue(addedTaskADuplicate.isLeft());
        assertEquals("TODO", addedTaskADuplicate.getLeft());
        // TODO: should we allow the addition of a task with same name but at a different level in the tree ?
    }
}
