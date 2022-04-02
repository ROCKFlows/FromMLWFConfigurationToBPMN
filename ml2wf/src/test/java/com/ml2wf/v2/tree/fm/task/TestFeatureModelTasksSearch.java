package com.ml2wf.v2.tree.fm.task;

import com.ml2wf.v2.testutils.assertions.tree.fm.XMLFeatureModelTestBase;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
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

class TestFeatureModelTasksSearch extends XMLFeatureModelTestBase {

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the search of a known feature model task from a featuremodel.")
    void testFeatureModeSearchKnownTaskFromFeatureModel(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);
        featureModel.iterator().forEachRemaining(task -> {
            Optional<FeatureModelTask> optTask = featureModel.getChildWithIdentity(task.getIdentity());
            assertTrue(optTask.isPresent());
            assertEquals(task, optTask.get());
            optTask = featureModel.getChildMatching(t -> t.getName().equals(task.getName()));
            assertTrue(optTask.isPresent());
            assertEquals(task, optTask.get());
        });
    }

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing the search of an unknown feature model task from a featuremodel.")
    void testFeatureModeSearchUnknownTaskFromFeatureModel(File file) {
        FeatureModel featureModel = getFeatureModelFromFile(file);
        Optional<FeatureModelTask> optTask = featureModel.getChildWithIdentity("Unknown identity");
        assertFalse(optTask.isPresent());
        optTask = featureModel.getChildMatching(t -> t.getName().equals("Unknown name"));
        assertFalse(optTask.isPresent());
    }
}
