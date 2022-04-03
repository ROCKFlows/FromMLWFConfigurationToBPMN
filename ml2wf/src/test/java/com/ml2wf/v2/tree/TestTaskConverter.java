package com.ml2wf.v2.tree;

import com.ml2wf.v2.testutils.assertions.tree.wf.XMLWorkflowTestBase;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TestTaskConverter extends XMLWorkflowTestBase {

    private FeatureModel featureModel;
    private Workflow workflow;
    private TaskConverter taskConverter;

    @BeforeEach
    private void setUp() {
        featureModel = FeatureModel.FeatureModelFactory.empty();
        workflow = Workflow.WorkflowFactory.createWorkflow();
        taskConverter = new TaskConverter(featureModel, workflow);
    }

    @AfterEach
    private void tearDown() {
        featureModel = null;
        workflow = null;
        taskConverter = null;
    }

    // TODO: test cases :
    //  - with spaces
    //  - nested
    //  - with parent

    // WFTask => FMTask

    private static Stream<Arguments> generateToFeatureModelTaskCases() {
        return Stream.of(
                // workflowName expectedFeatureName isAbstract isMandatory descriptions
                // TODO: to improve
                arguments("task_A", "task_A", false, false, List.of("@Optional:true ")),
                arguments("originalWFTask", "originalWFTask", false, true, List.of()),
                arguments("originalWFTask_Step", "originalWFTask", true, false, List.of("@Optional:true")),
                arguments("originalWFTask_Step", "originalWFTask", true, false, List.of("@Optional:true zdfzeezfze\nrefersTo:")),
                arguments("originalWFTask_Step", "originalWFTask", true, true, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("generateToFeatureModelTaskCases")
    @DisplayName("Test Workflow->FeatureModel conversion")
    void testToFeatureModelTask(String workflowName, String expectedFeatureName, boolean isAbstract,
                                boolean isMandatory, List<String> descriptions) {
        var workflowTask = (descriptions.isEmpty()) ?
                WorkflowTask.WorkflowTaskFactory.createTask(workflowName) :
                WorkflowTask.WorkflowTaskFactory.createTask(workflowName, descriptions.get(0)); // get first as we currently only support one doc
        var expectedFMTask = FeatureModelTask.FeatureModelTaskFactory.createTask(
                expectedFeatureName, isAbstract, isMandatory, descriptions);
        var convertedFeatureTask = taskConverter.toFeatureModelTask(workflowTask);
        // checking conversion
        assertEquals(expectedFMTask, convertedFeatureTask);
        // checking append to FM
        /* TODO: check append to FM
        featureModel.getChildWithIdentity(expectedFeatureName).ifPresentOrElse(
                c -> assertSame(convertedFeatureTask, c),
                () -> {
                    throw new AssertionFailedError("Expected result FeatureModelTask to be inserted in the FeatureModel.");
                }
        );
        */
    }

    // FMTask => WFTask

    private static Stream<Arguments> generateToWorkflowTaskCases() {
        return Stream.of(
                // expectedWorkflowName featureName isAbstract isMandatory descriptions
                // TODO: to improve
                arguments("task_A", "task_A", false, false, List.of("@Optional:true ")),
                arguments("originalWFTask", "originalWFTask", false, true, List.of()),
                arguments("originalWFTask", "originalWFTask", true, false, List.of("")),
                arguments("originalWFTask", "originalWFTask", true, false, List.of("dzadaz")),
                arguments("originalWFTask", "originalWFTask", true, true, List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("generateToWorkflowTaskCases")
    @DisplayName("Test FeatureModel->Workflow conversion")
    void testToWorkflowTask(String featureName, String expectedWorkflowName, boolean isAbstract, boolean isMandatory,
                            List<String> descriptions) {
        var featureModelTask = (descriptions.isEmpty()) ?
                FeatureModelTask.FeatureModelTaskFactory.createTask(featureName, isAbstract, isMandatory) :
                FeatureModelTask.FeatureModelTaskFactory.createTask(featureName, isAbstract, isMandatory, descriptions);
        var expectedWFTask = (descriptions.isEmpty()) ?
                WorkflowTask.WorkflowTaskFactory.createTask(featureName) :
                WorkflowTask.WorkflowTaskFactory.createTask(featureName, descriptions.get(0)); // get first as we currently only support one doc
        var convertedWFTask = taskConverter.toWorkflowTask(featureModelTask);
        // checking conversion
        assertEquals(expectedWFTask, convertedWFTask);
    }
}
