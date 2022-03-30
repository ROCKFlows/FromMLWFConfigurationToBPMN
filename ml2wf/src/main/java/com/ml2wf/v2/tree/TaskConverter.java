package com.ml2wf.v2.tree;

import com.ml2wf.util.XMLManager;
import com.ml2wf.v2.tree.fm.FeatureModel;
import com.ml2wf.v2.tree.fm.FeatureModelTask;
import com.ml2wf.v2.tree.wf.Workflow;
import com.ml2wf.v2.tree.wf.WorkflowTask;
import com.ml2wf.v2.tree.wf.util.WorkflowTaskUtil;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utils class providing methods for converting tasks :
 *
 * <p>
 *
 * <ul>
 *     <li>toFeatureModelTask : {@link WorkflowTask} ->  {@link FeatureModelTask}</li>
 *     <li>toWorkflowTask : {@link FeatureModelTask} ->  {@link WorkflowTask}</li>
 * </ul>
 *
 * @see WorkflowTask
 * @see FeatureModelTask
 *
 * @since 1.1.0
 */
@Log4j2
public final class TaskConverter {

    // TODO: support nested

    // TODO: split into 2 classes implementing the same interface (+ same for cleanup ?)

    @NonNull private final FeatureModel featureModel;
    @NonNull private final Workflow workflow;

    public TaskConverter(@NonNull FeatureModel featureModel, @NonNull Workflow workflow) {
        this.featureModel = featureModel;
        this.workflow = workflow;
    }

    private FeatureModelTask postFMConversionCleanUp(FeatureModelTask featureModelTask) {
        // TODO: to improve (replace opt + ref + _Step)
        featureModelTask.setName(XMLManager.sanitizeName(featureModelTask.getName()));
        // TODO: sanitize documentation
        return featureModelTask;
    }

    /**
     * Returns a {@link FeatureModelTask} instance based on the given {@link WorkflowTask}.
     *
     * @param workflowTask  the {@link WorkflowTask}'s instance
     *
     * @return the resulting {@link FeatureModelTask}
     */
    public FeatureModelTask toFeatureModelTask(WorkflowTask workflowTask) {
        log.debug("Converting WorkflowTask [{}] to FeatureModelTask.", workflowTask.getName());
        List<String> descriptions = Stream.of(workflowTask.getDocumentation().getContent())
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.toList());
        Optional<String> optReferenceName = WorkflowTaskUtil.getReferenceName(workflowTask);
        var createdFmTask = FeatureModelTask.FeatureModelTaskFactory.createTask(
                workflowTask.getName(), workflowTask.isAbstract(), !workflowTask.isOptional(), descriptions);
        if (optReferenceName.isPresent()) {
            Optional<FeatureModelTask> optReferenceFMTask = featureModel.getChildWithIdentity(optReferenceName.get());
            optReferenceFMTask.ifPresentOrElse(r -> r.appendDirectChild(createdFmTask), () -> {
                log.error("Unknown reference {}. Skipping...", optReferenceName);
                // TODO: attach to Unmanaged node once implemented
            });
        }
        return postFMConversionCleanUp(createdFmTask);
    }

    /**
     * Returns a {@link WorkflowTask} instance based on the given {@link FeatureModelTask}.
     *
     * @param featureModel  the {@link FeatureModelTask}'s instance
     *
     * @return the resulting {@link WorkflowTask}
     */
    public WorkflowTask toWorkflowTask(FeatureModelTask featureModel) {
        log.debug("Converting WorkflowTask [{}] to WorkflowTask.", featureModel.getName());
        String description = featureModel.getDescriptions().stream()
                .map(FeatureModelTask.Description::toString)
                .collect(Collectors.joining("\n"));
        return WorkflowTask.WorkflowTaskFactory.createTask(featureModel.getName(), description);
    }
}

