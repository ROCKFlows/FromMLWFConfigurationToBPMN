package com.ml2wf.v2.tree.fm;

import com.ml2wf.v2.testutils.assertions.tree.fm.XMLFeatureModelTestBase;
import com.ml2wf.v2.testutils.assertions.tree.fm.ForEachFMTaskAssertion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TestFeatureModelNormalization extends XMLFeatureModelTestBase {

    @ParameterizedTest
    @MethodSource("fmFiles")
    @DisplayName("Testing that the normalization updates the feature model's tasks names as expected")
    void testNormalizationUpdatesNamesForMetaWorkflows(File file) {
        ForEachFMTaskAssertion.builder()
                .featureModelPair(getReferenceNormalizedFeatureModels(file))
                .forEachTask((referenceTask, normalizedTask) -> {
                    assertEquals(referenceTask.getIdentity().replace(" ", "_"), normalizedTask.getIdentity());
                    assertFalse(normalizedTask.getName().contains(" "));
                    assertEquals(referenceTask.getName().replace(" ", "_"), normalizedTask.getName());
                    assertEquals(referenceTask.isAbstract(), normalizedTask.isAbstract());
                    assertEquals(referenceTask.isMandatory(), normalizedTask.isMandatory());
                    assertEquals(referenceTask.getDescriptions(), normalizedTask.getDescriptions());
                })
                .afterTasksIteration((referenceProcessIterator, instanceProcessIterator) ->
                        assertFalse(referenceProcessIterator.hasNext() || instanceProcessIterator.hasNext())
                )
                .build()
                .verify();
    }
}
