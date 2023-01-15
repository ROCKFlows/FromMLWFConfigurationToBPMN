package com.ml2wf.contract.storage.graph.converter;

import com.ml2wf.contract.storage.graph.dto.GraphStandardWorkflowTask;
import com.ml2wf.contract.storage.graph.dto.GraphTaskVersion;
import com.ml2wf.core.workflow.StandardWorkflow;
import com.ml2wf.core.workflow.StandardWorkflowTask;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Component
public interface IGraphStandardWorkflowConverter<T extends GraphStandardWorkflowTask<T, V>, V extends GraphTaskVersion> {

    default @NonNull StandardWorkflow toStandardWorkflow(@NonNull T graphStandardWorkflow) {
        return new StandardWorkflow(toStandardWorkflowTask(graphStandardWorkflow));
    }

    default List<StandardWorkflowTask> toStandardWorkflowTask(@NonNull T workflowTask) {
        List<StandardWorkflowTask> tasks = new ArrayList<>();
        tasks.add(new StandardWorkflowTask(
                workflowTask.getName(),
                workflowTask.getDescription(),
                workflowTask.isAbstract(),
                workflowTask.isOptional()
             )
        );
        if (workflowTask.getNextTask() != null) {
            tasks.addAll(toStandardWorkflowTask(workflowTask.getNextTask()));
        }
        return tasks;
    }

    default @NonNull T fromStandardWorkflow(@NonNull StandardWorkflow standardWorkflow) {
        if (standardWorkflow.getTasks().isEmpty()) {
            throw new IllegalStateException("Can't convert an empty workflow.");
        }
        ListIterator<StandardWorkflowTask> tasksIterator = standardWorkflow.getTasks()
                .listIterator(standardWorkflow.getTasks().size());
        T newPreviousGraphTasks = null;
        T previousGraphTasks = null;
        while (tasksIterator.hasPrevious()) {
            newPreviousGraphTasks = fromStandardWorkflowTask(tasksIterator.previous());
            newPreviousGraphTasks.setNextTask(previousGraphTasks);
            previousGraphTasks = newPreviousGraphTasks;
        }
        if (newPreviousGraphTasks == null) {
            // should not happen
            // TODO: refactor method to avoid this condition
            throw new RuntimeException("Unexpected null previous graph task when converting from standard workflow.");
        }
        return newPreviousGraphTasks;
    }

    @NonNull T fromStandardWorkflowTask(@NonNull StandardWorkflowTask standardWorkflowTask,
                                        @Nullable StandardWorkflowTask child);

    default @NonNull T fromStandardWorkflowTask(@NonNull StandardWorkflowTask standardWorkflowTask) {
        return fromStandardWorkflowTask(standardWorkflowTask, null);
    }
}
