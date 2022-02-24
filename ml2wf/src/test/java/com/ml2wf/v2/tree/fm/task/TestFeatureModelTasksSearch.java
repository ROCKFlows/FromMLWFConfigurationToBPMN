package com.ml2wf.v2.tree.fm.task;

import com.ml2wf.v2.testutils.assertions.tree.fm.XMLFeatureModelTestBase;
import com.ml2wf.v2.tree.fm.FeatureModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestFeatureModelTasksSearch extends XMLFeatureModelTestBase {

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the search of a known feature model task from a feature model.")
    void testInstanceWorkflowKnownSearchTaskFromWorkflow(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);

        featureModel.iterator().forEachRemaining(System.out::println);
        /*Collection<Process> workflowProcesses = featureModel.getChildren();
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
        }*/
    }

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the search of an unknown workflow task from an instance workflow.")
    void testInstanceWorkflowSearchUnknownTaskFromWorkflow(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);

        /*Process process = new ArrayList<>(workflow.getChildren()).get(0);
        Optional<WorkflowTask> optSearchedProcess = process.getChildWithIdentity("unknown workflow identity");
        assertTrue(optSearchedProcess.isEmpty());
        optSearchedProcess = process.getChildMatching(p -> p.getName().equals("unknown workflow name"));
        assertTrue(optSearchedProcess.isEmpty());*/
    }
}
